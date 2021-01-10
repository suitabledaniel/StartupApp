package com.example.startupapp


import RecyclerTouchListener
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customnavigationdrawerexample.DemoFragment
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


class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mMap: GoogleMap? = null
    private var searchBtn: Button? = null
    private var placeAdapter:PlaceArrayAdapter? = null

    private lateinit var mPlacesClient: PlacesClient
    private val apiKey = "AIzaSyCPD3p0R_wNvtK_IktkKZCjSsT99UUES8A"


    private var mPosition : Int? = null
    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    private lateinit var navigation_rv: RecyclerView
    private var items = arrayListOf(
        NavigationItemModel(R.drawable.ic_home, "Home"),
        NavigationItemModel(R.drawable.ic_music, "Music"),
        NavigationItemModel(R.drawable.ic_movie, "Movies"),
        NavigationItemModel(R.drawable.ic_book, "Books"),
        NavigationItemModel(R.drawable.ic_profile, "Profile"),
        NavigationItemModel(R.drawable.ic_settings, "Settings"),
        NavigationItemModel(R.drawable.ic_social, "Like us on facebook")
    )

    private val LATLNG = mapOf("Lat" to 35.1799817, "Lng" to 128.1076213)


    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Places.initialize(this, apiKey)

        /*Create PlaceClient for init*/
        mPlacesClient = Places.createClient(this)
        /*Create mapFragement for init */
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        placeAdapter = PlaceArrayAdapter(this, R.layout.layout_item_places, mPlacesClient)
        var autoCompleteEditText = findViewById(R.id.autoCompleteEditText) as AutoCompleteTextView
        autoCompleteEditText.setAdapter(placeAdapter)

        /*Create Navigation View */
        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(findViewById(R.id.toolbar))

        navigation_rv.findViewById<RecyclerView>(R.id.navigation_rv).layoutManager = LinearLayoutManager(this)
        navigation_rv.findViewById<RecyclerView>(R.id.navigation_rv).setHasFixedSize(true)

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        // # Home Fragment
                        val homeFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, homeFragment).commit()
                    }
                    1 -> {
                        // # Music Fragment
                        val musicFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, musicFragment).commit()
                    }
                    2 -> {
                        // # Movies Fragment
                        val moviesFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, moviesFragment).commit()
                    }
                    3 -> {
                        // # Books Fragment
                        val booksFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, booksFragment).commit()
                    }
                    4 -> {
                        // # Profile Activity
                        //val intent = Intent(this@MainActivity, DemoActivity::class.java)
                       // startActivity(intent)
                    }
                    5 -> {
                        // # Settings Fragment
                        val settingsFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.activity_main_content_id, settingsFragment).commit()
                    }
                    6 -> {
                        // # Open URL in browser
                        val uri: Uri = Uri.parse("https://johnc.co/fb")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                if (position != 6 && position != 4) {
                    updateAdapter(position)
                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))
        updateAdapter(0)
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()


        val homeFragment = DemoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id, homeFragment).commit()

        // Set Header Image
        findViewById<ImageView>(R.id.navigation_header_img).setImageResource(R.drawable.logo)

        // Set background of Drawer
        findViewById<ImageView>(R.id.navigation_layout).setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_primary))


        searchBtn = findViewById(R.id.search_btn) as Button
        searchBtn?.setOnClickListener {
            Log.d("TEST::SearchBtnClick", autoCompleteEditText?.text.toString())
        }

        autoCompleteEditText.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->

            val place = parent.getItemAtPosition(position) as PlaceDataModel
            autoCompleteEditText.apply {
                Log.d("TEST::autoComplete", place.placeId.toString())
                Log.d("TEST::autoComplete", position.toString())

                val placeFields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.OPENING_HOURS, Place.Field.WEBSITE_URI, Place.Field.RATING, Place.Field.LAT_LNG)
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
    }

    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Checking for fragment count on back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
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