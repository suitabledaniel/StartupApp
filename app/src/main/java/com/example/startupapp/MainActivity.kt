package com.example.startupapp


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions;
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
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

}