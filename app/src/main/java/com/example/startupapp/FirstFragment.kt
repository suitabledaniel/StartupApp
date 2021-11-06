package com.example.startupapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.lang.Exception

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mPlacesClient: PlacesClient
    private val apiKey = "AIzaSyCPD3p0R_wNvtK_IktkKZCjSsT99UUES8A"
    private var placeAdapter:PlaceArrayAdapter? = null
    private var mMap: GoogleMap? = null
    private var searchBtn: Button? = null
    private var mPosition : Int? = null
    private val LATLNG = mapOf("Lat" to 35.1799817, "Lng" to 128.1076213)
    private var mFragmentView : View? = null;

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*Create PlaceClient for init*/
        Places.initialize(container!!.context, apiKey)
        mFragmentView = inflater.inflate(R.layout.fragment_first, container, false)
        mPlacesClient = Places.createClient(container!!.context)
        /*Create mapFragement for init */
        val mapFragment = requireFragmentManager().findFragmentById(R.id.mapview) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        placeAdapter = PlaceArrayAdapter(container!!.context, R.layout.layout_item_places, mPlacesClient)
        //var autoCompleteEditText = requireView().findViewById(R.id.autoCompleteEditText) as AutoCompleteTextView
        var autoCompleteEditText = mFragmentView!!.findViewById(R.id.autoCompleteEditText) as AutoCompleteTextView

        autoCompleteEditText.setAdapter(placeAdapter)
        searchBtn = mFragmentView!!.findViewById(R.id.search_btn) as Button
        searchBtn?.setOnClickListener {
            Log.d("TEST::SearchBtnClick", autoCompleteEditText?.text.toString())
        }

        autoCompleteEditText.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->

            val place = parent.getItemAtPosition(position) as PlaceDataModel
            autoCompleteEditText.apply {
                Log.d("TEST::autoComplete", place.placeId.toString())
                Log.d("TEST::autoComplete", position.toString())

                val placeFields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.WEBSITE_URI, Place.Field.RATING, Place.Field.LAT_LNG)
                val request = FetchPlaceRequest.newInstance(place.placeId.toString(), placeFields)
                mPlacesClient.fetchPlace(request)
                        .addOnSuccessListener { response: FetchPlaceResponse ->
                            val place = response.place
                            Log.i("TEST::PlacesClient", "Place Found : ${place.name}")
                            Log.i("TEST::PlacesClient", "Place Found : ${place.address}")
                            Log.i("TEST::PlacesClient", "Place Found : ${place.websiteUri}")
                            Log.i("TEST::PlacesClient", "Place Found : ${place.latLng}")
                            Log.i("TEST::PlacesClient", "Place Found : ${place.rating}")
                            Log.i("TEST::PlacesClient", "Place Found : ${place.isOpen}")
                            setMarkerDetailPlace(place);
                        }.addOnFailureListener{exception: Exception ->
                            if (exception is ApiException) {
                                Log.e("TEST::::PlacesClient", "Place not found: ${exception.message}")
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code")
                            }
                        }
                mPosition = position
                setText(place.fullText)
                setSelection(autoCompleteEditText.length())
            }
        }
        return mFragmentView;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
        TODO("Not yet implemented")
    }
}