package com.example.examtemplate

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import tech.gusavila92.websocketclient.WebSocketClient
import java.net.URI
import java.net.URISyntaxException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var listFragment: ListFragment
    lateinit var addFragment: AddFragment
    lateinit var levelFilterListFragment: LevelFilterListFragment
    lateinit var timeRangeListFragment: TimeRangeListFragment
    private var webSocketClient: WebSocketClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_Layout)

        toolbar.title = "Template"

        createWebSocketClient()

        val drawerToggle: ActionBarDrawerToggle = object: ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                (R.string.open),
                (R.string.close)
        ) {

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val nav_view: NavigationView = findViewById(R.id.nav_view)

        nav_view.setNavigationItemSelectedListener(this)

        listFragment = ListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, listFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.itemList -> {
                listFragment = ListFragment()
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, listFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
            }
            R.id.addItem -> {
                addFragment = AddFragment()
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, addFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
            }
            R.id.levelItemList -> {
                levelFilterListFragment = LevelFilterListFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, levelFilterListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.timeRangeItemList -> {
                timeRangeListFragment = TimeRangeListFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, timeRangeListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_Layout)

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_Layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    private fun createWebSocketClient() {
        val uri: URI
        uri = try {
            // Connect to local host
            URI("ws://86.124.3.59:4200")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket", "Session is starting")
                webSocketClient?.send("Hello World!")
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket", "Message received")
                runOnUiThread {
                    try {
                        Toast.makeText(this@MainActivity, "Item added $s", Toast.LENGTH_SHORT).show()
                        println("works")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onBinaryReceived(data: ByteArray) {}
            override fun onPingReceived(data: ByteArray) {}
            override fun onPongReceived(data: ByteArray) {}
            override fun onException(e: Exception) {
                println(e.message)
            }

            override fun onCloseReceived() {
                Log.i("WebSocket", "Closed ")
                println("onCloseReceived")
            }
        }
        (webSocketClient as WebSocketClient).setConnectTimeout(10000)
        (webSocketClient as WebSocketClient).setReadTimeout(60000)
        (webSocketClient as WebSocketClient).enableAutomaticReconnection(5000)
        (webSocketClient as WebSocketClient).connect()
    }
}