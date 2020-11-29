package com.example.startupapp


import android.os.Bundle
import android.os.PersistableBundle
import android.text.Selection.setSelection
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mMap: GoogleMap? = null
    private var searchBtn: Button? = null
    private var editTxt: EditText? = null
    private val apiKey = "AIzaSyCPD3p0R_wNvtK_IktkKZCjSsT99UUES8A"
    private lateinit var mPlacesClient: PlacesClient
    private var placeAdapter:PlaceArrayAdapter? = null

    private val LATLNG = mapOf("Lat" to 35.1799817, "Lng" to 128.1076213)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Places.initialize(this, apiKey)
        mPlacesClient = Places.createClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        placeAdapter = PlaceArrayAdapter(this, R.layout.layout_item_places, mPlacesClient)
        var autoCompleteEditText = findViewById(R.id.autoCompleteEditText) as AutoCompleteTextView
        autoCompleteEditText.setAdapter(placeAdapter)

        searchBtn = findViewById(R.id.search_btn) as Button
        searchBtn?.setOnClickListener {
            Log.d("TEST", autoCompleteEditText?.text.toString())
        }

        autoCompleteEditText.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->

            val place = parent.getItemAtPosition(position) as PlaceDataModel
            autoCompleteEditText.apply {
                Log.d("TEST",place.toString())
                setText(place.fullText)
                setSelection(autoCompleteEditText.length())
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val JINJU = LatLng((LATLNG["Lat"] ?: error("didn't Double Type")).toDouble(), (LATLNG["Lng"]
                ?: error("didn't Double Type")).toDouble())
        val markerOptions = MarkerOptions()
        markerOptions.position(JINJU)
        markerOptions.title("JINJU")
        markerOptions.snippet("GYEUNANM")
        mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(JINJU, 10f))
    }

    override fun onClick(v: View?) {

    }

}