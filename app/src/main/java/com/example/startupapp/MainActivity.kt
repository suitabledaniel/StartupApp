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

import java.lang.Exception


class MainActivity : AppCompatActivity() {


    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    private lateinit var navigation_rv: RecyclerView
    private lateinit var navigation_layout: LinearLayout
    private var items = arrayListOf(
        NavigationItemModel(R.drawable.ic_home, "Home"),
        NavigationItemModel(R.drawable.ic_music, "Music"),
        NavigationItemModel(R.drawable.ic_movie, "Movies"),
        NavigationItemModel(R.drawable.ic_book, "Books"),
        NavigationItemModel(R.drawable.ic_profile, "Profile"),
        NavigationItemModel(R.drawable.ic_settings, "Settings"),
        NavigationItemModel(R.drawable.ic_social, "Like us on facebook")
    )



    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*Create Navigation View */
        drawerLayout = findViewById(R.id.drawer_layout)
        setSupportActionBar(findViewById(R.id.toolbar))

        navigation_rv = findViewById(R.id.navigation_rv) as RecyclerView
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        // # Home Fragment
                        val homeFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                           .replace(R.id.main_activity_content_id, homeFragment).commit()
                    }
                    1 -> {
                        // # Camera Fragment
                        val firstFragment = FirstFragment()
                        supportFragmentManager.beginTransaction()
                               .replace(R.id.main_activity_content_id, firstFragment).commit()
                    }
                    2 ->{
                        // # Movies Fragment
                        val moviesFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                          .replace(R.id.main_activity_content_id, moviesFragment).commit()
                    }
                    3 -> {
                      // # Books Fragment
                        val booksFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_activity_content_id, booksFragment).commit()
                    }
                    4 -> {
                        // # Profile Activity
                        //val intent = Intent(this@MainActivity, DemoActivity::class.java)
                        //startActivity(intent)
                    }
                    5 -> {
                        // # Settings Fragment
                        val settingsFragment = DemoFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_activity_content_id, settingsFragment).commit()
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
            .replace(R.id.main_activity_content_id, homeFragment).commit()

        // Set Header Image
        findViewById<ImageView>(R.id.navigation_header_img).setImageResource(R.drawable.logo)

        // Set background of Drawer
        navigation_layout = findViewById(R.id.navigation_layout) as LinearLayout
        navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_primary))

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


    private fun getLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        ), 1000)
    }
}