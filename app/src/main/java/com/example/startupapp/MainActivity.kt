package com.example.startupapp


import android.os.Bundle
import android.util.Log
import android.view.View
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
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mMap: GoogleMap? = null
    private var searchBtn: Button? = null
    private var editTxt: EditText? = null
    private val apiKey = "AIzaSyCPD3p0R_wNvtK_IktkKZCjSsT99UUES8A"
    private val LATLNG = mapOf("Lat" to 35.1799817, "Lng" to 128.1076213)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)
        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
        //var placeFiled = listOf<Place.Field>(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);


        editTxt = findViewById(R.id.search_edit_txt)
        searchBtn = findViewById(R.id.search_btn)
        searchBtn!!.setOnClickListener {
            Log.d("EditText", editTxt!!.text.toString())
            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setQuery(editTxt!!.text.toString())
                    .build()

            val autocompletePredictions = placesClient.findAutocompletePredictions(request)

            // This method should have been called off the main UI thread. Block and wait for at most
            // 60s for a result from the API.

            // This method should have been called off the main UI thread. Block and wait for at most
            // 60s for a result from the API.
            try {
                Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: TimeoutException) {
                e.printStackTrace()
            }

            /*return*/ if (autocompletePredictions.isSuccessful) {
                val findAutocompletePredictionsResponse = autocompletePredictions.result
                if (findAutocompletePredictionsResponse != null) for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                    Log.i(TAG, prediction.placeId)
                    //resultList.add(PlaceAutocomplete(prediction.placeId, prediction.getPrimaryText(STYLE_NORMAL).toString(), prediction.getFullText(STYLE_BOLD).toString()))
                }
                //resultList
            } else {
                //resultList
            }

        }
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