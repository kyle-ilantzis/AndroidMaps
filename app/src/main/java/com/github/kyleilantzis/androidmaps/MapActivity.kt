package com.github.kyleilantzis.androidmaps

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.github.kyleilantzis.androidmaps.espresso.EspressoIdlingResource
import com.mapbox.mapboxsdk.Mapbox
import java.util.*
import kotlin.system.measureTimeMillis

class MapActivity : AppCompatActivity() {

    companion object {

        @Volatile var markerMillis: Long = 0
        @Volatile var iconMillis: Long = 0
        @Volatile var actionDone: Boolean = false

        val TAG = "MyMaps"

        val EXTRA_TYPE = "type"
        val EXTRA_ACTION = "action"

        val TYPE_MAPBOX = 0
        val TYPE_GOOGLEMAPS = 1

        val ACTION_1000_POINTS = 0
        val ACTION_1000_SIMILAR_POINTS = 1
        val ACTION_1000_DIFFERENT_POINTS = 2

        fun start(ctx: Context, type: Int, action: Int) {
            val i = Intent(ctx, Class.forName("com.github.kyleilantzis.androidmaps.MapActivity"))
                    .putExtra(EXTRA_TYPE, type)
                    .putExtra(EXTRA_ACTION, action)
            ctx.startActivity(i)
        }
    }

    val POINTS = BuildConfig.POINTS

    lateinit var layout: View
    lateinit var mapView: CommonMapView
    lateinit var map: CommonMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EspressoIdlingResource.increment()
        MapActivity.markerMillis = 0
        MapActivity.iconMillis = 0
        MapActivity.actionDone = false

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
            map.target = BuildConfig.LAT to BuildConfig.LON

            when (action) {

                ACTION_1000_POINTS -> _1000_points()
                ACTION_1000_SIMILAR_POINTS -> _1000_similar_points()
                ACTION_1000_DIFFERENT_POINTS -> _1000_different_points()
            }

            actionDone = true
            EspressoIdlingResource.decrement()
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

    fun _1000_points() {

        Log.i(TAG, "starting action $POINTS points...")

        val randomPoints = randomPoints()

        val elapsed = measureTimeMillis {
            randomPoints.forEachIndexed { i, latlon ->

                val icon = null
                val title = Integer.toString(i)

                map.addMarker(icon, title, latlon)
            }
        }

        iconMillis = 0
        markerMillis = elapsed

        Log.i(TAG, "action $POINTS points: $elapsed millis")
        showSnackbar("Adding $POINTS points took $elapsed millis")

    }

    fun _1000_similar_points() {

        Log.i(TAG, "starting action $POINTS similar points...")

        val startIcons = System.currentTimeMillis()

        val icon1 = makeTriangleIcon(Color.RED)
        val icon2 = makeTriangleIcon(Color.BLUE)
        val icon3 = makeTriangleIcon(Color.YELLOW)
        val icons = arrayOf(icon1, icon2, icon3)

        val elapsedIcons = System.currentTimeMillis() - startIcons
        Log.i(TAG, "action $POINTS similar points: icons took $elapsedIcons millis")

        val random = Random()
        val randomPoints = randomPoints()

        val elapsed = measureTimeMillis {
            randomPoints.forEachIndexed { i, latlon ->

                val icon = icons[random.nextInt(icons.size)]
                val title = Integer.toString(i)

                map.addMarker(icon, title, latlon)
            }
        }

        iconMillis = elapsedIcons
        markerMillis = elapsed

        Log.i(TAG, "action $POINTS similar points: $elapsed millis")
        showSnackbar("$POINTS similar points took $elapsedIcons millis to create the icons and $elapsed millis to add the markers")
    }

    fun _1000_different_points() {

        Log.i(TAG, "starting action $POINTS different points...")

        val random = Random()

        val startIcons = System.currentTimeMillis()

        val icons = Array<CommonIcon>(POINTS, {
            val color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
            makeTriangleIcon(color)
        })

        val elapsedIcons = System.currentTimeMillis() - startIcons
        Log.i(TAG, "action $POINTS different points: icons took $elapsedIcons millis")


        val randomPoints = randomPoints()

        val elapsed = measureTimeMillis {
            randomPoints.forEachIndexed { i, latlon ->

                val icon = icons[i]
                val title = Integer.toString(i)

                map.addMarker(icon, title, latlon)
            }
        }

        iconMillis = elapsedIcons
        markerMillis = elapsed

        Log.i(TAG, "action $POINTS different points: $elapsed millis")
        showSnackbar("$POINTS different points took $elapsedIcons millis to create the icons and $elapsed millis to add the markers")
    }

    fun showSnackbar(message: String) {
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show()
    }

    fun makeTriangleIcon(color: Int): CommonIcon {

        val density = resources.displayMetrics.density
        val w = 16 * density
        val h = 24 * density

        val bmp = Bitmap.createBitmap(w.toInt(), h.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = color

        // A triangle polygon with the point downwards
        val path = Path()
        path.moveTo(0F, 0F)
        path.lineTo(w, 0F)
        path.lineTo(w / 2, h)
        path.lineTo(0F, 0F)

        canvas.drawPath(path, paint)

        return map.makeCommonIcon(bmp)
    }

    fun randomPoints(): List<Pair<Double, Double>> {

        val rand = Random()

        var lat = BuildConfig.LAT
        var lon = BuildConfig.LON

        val list = ArrayList<Pair<Double, Double>>()

        for (i in 0 until BuildConfig.POINTS) {

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
