package com.example.startupapp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

class MainActivity : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_first, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }


    override fun onMapReady(googleMap : GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }
        val marker = LatLng(35.241615, 128.695587)
        mMap.addMarker(MarkerOptions().position(marker).title("Marker KIM"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }
}