package kyleilantzis.github.com.mymaps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.mapbox.mapboxsdk.Mapbox
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.measureNanoTime

class MapActivity : AppCompatActivity() {

    companion object {

        val TAG = "MyMaps"

        val EXTRA_TYPE = "type"
        val EXTRA_ACTION = "action"

        val TYPE_MAPBOX = 0
        val TYPE_GOOGLEMAPS = 1

        val ACTION_1000_POINTS = 0

        fun start(ctx: Context, type: Int, action: Int) {
            val i = Intent(ctx, Class.forName("kyleilantzis.github.com.mymaps.MapActivity"))
                    .putExtra(EXTRA_TYPE, type)
                    .putExtra(EXTRA_ACTION, action)
            ctx.startActivity(i)
        }
    }

    // New York City
    val LAT = 40.73581
    val LON = -73.99155

    lateinit var layout: View
    lateinit var mapView: CommonMapView
    lateinit var map: CommonMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(applicationContext, BuildConfig.MAPBOX_KEY)

        setContentView(R.layout.activity_map)

        layout = findViewById(R.id.layout)

        val type = intent.getIntExtra(EXTRA_TYPE, TYPE_MAPBOX)
        val action = intent.getIntExtra(EXTRA_ACTION, ACTION_1000_POINTS)

        when (type) {

            TYPE_MAPBOX -> {
                Log.i(TAG, "map type mapbox")
                val view = findViewById(R.id.mapboxMapView) as com.mapbox.mapboxsdk.maps.MapView
                view.visibility = View.VISIBLE
                mapView = mapViewFrom(view)
            }

            TYPE_GOOGLEMAPS -> {
                Log.i(TAG, "map type google maps")
                val view = findViewById(R.id.googlemapsMapView) as com.google.android.gms.maps.MapView
                view.visibility = View.VISIBLE
                mapView = mapViewFrom(view)
            }
        }

        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            map = it

            val target = LAT to LON
            map.target = target

            when (action) {

                ACTION_1000_POINTS -> {

                    Log.i(TAG, "starting action 1000 points...")

                    val randomPoints = randomPoints(target)

                    val elapsed = measureNanoTime {
                        randomPoints.forEachIndexed { i, latlon ->

                            val icon = null
                            val title = Integer.toString(i)

                            map.addMarker(icon, title, latlon)
                        }
                    }

                    val millis = TimeUnit.NANOSECONDS.toMillis(elapsed)
                    Log.i(TAG, "action 1000 points: $millis millis")
                    showSnackbar("Adding 1000 points took $millis millis")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show()
    }

    fun randomPoints(start: Pair<Double, Double>, count: Int = 1000): List<Pair<Double, Double>> {

        val rand = Random()

        var lat = start.first
        var lon = start.second

        val list = ArrayList<Pair<Double, Double>>()

        for (i in 0..count) {

            val nextLatSign = if (rand.nextBoolean()) 1 else -1
            val nextLonSign = if (rand.nextBoolean()) 1 else -1

            val stepLat = rand.nextInt(1000) / (1000.0 * 100.0)
            val stepLon = rand.nextInt(1000) / (1000.0 * 100.0)

            val nextLat = lat + nextLatSign * stepLat
            val nextLon = lon + nextLonSign * stepLon

            list.add(nextLat to nextLon)

            lat = nextLat
            lon = nextLon
        }

        return list
    }
}
