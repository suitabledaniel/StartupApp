package com.example.startupapp


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.lang.Exception


class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mMap: GoogleMap? = null
    private var searchBtn: Button? = null
    private val apiKey = "AIzaSyCPD3p0R_wNvtK_IktkKZCjSsT99UUES8A"
    private lateinit var mPlacesClient: PlacesClient
    private var placeAdapter:PlaceArrayAdapter? = null

    private var mPosition : Int? = null

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
                Log.d("TEST", place.placeId.toString())
                Log.d("TEST", position.toString())

                val placeFields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.OPENING_HOURS, Place.Field.WEBSITE_URI, Place.Field.RATING, Place.Field.LAT_LNG)
                val request = FetchPlaceRequest.newInstance(place.placeId.toString(), placeFields)
                mPlacesClient.fetchPlace(request)
                        .addOnSuccessListener { response: FetchPlaceResponse ->
                        val place = response.place
                        Log.i("TEST", "Place Found : ${place.name}")
                        Log.i("TEST", "Place Found : ${place.address}")
                        Log.i("TEST", "Place Found : ${place.websiteUri}")
                        Log.i("TEST", "Place Found : ${place.latLng}")
                        Log.i("TEST", "Place Found : ${place.rating}")
                        Log.i("TEST", "Place Found : ${place.isOpen}")
                            setMarkerDetailPlace(place);
                        }.addOnFailureListener{exception: Exception ->
                    if (exception is ApiException) {
                        Log.e("TEST", "Place not found: ${exception.message}")
                        val statusCode = exception.statusCode
                        TODO("Handle error with given status code")
                    }
                }
                mPosition = position
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
    private fun setMarkerDetailPlace(place : Place){
        if(place.latLng != null){
            val newPlace = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(newPlace)
            markerOptions.title(place.name.toString())
            if (place.address != null) markerOptions.snippet(place.address)
            mMap!!.addMarker(markerOptions)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(newPlace, 10f))
        }else {
            Log.e("TEST", "Place not found!")
        }
    }
    private fun getLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        ), 1000)
    }
}