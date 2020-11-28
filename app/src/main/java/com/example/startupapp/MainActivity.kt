package com.example.startupapp


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions;
class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mMap: GoogleMap? = null
    private var searchBtn: Button? = null
    private var editTxt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        editTxt = findViewById(R.id.search_edit_txt)
        searchBtn = findViewById(R.id.search_btn)
        searchBtn!!.setOnClickListener {
            Log.d("EditText",editTxt.toString())
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val JINJU = LatLng( 35.1799817, 128.1076213)
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