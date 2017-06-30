package kyleilantzis.github.com.mymaps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.measureNanoTime

class MapActivity : AppCompatActivity() {

    companion object {

        val EXTRA_TYPE = "extra-type"

        val TYPE_MAPBOX_1000_POINTS = 0

        val TYPE_GOOGLEMAPS_1000_POINTS = 10

        fun start(ctx: Context, type: Int) {
            ctx.startActivity(Intent(ctx, Class.forName("kyleilantzis.github.com.mymaps.MapActivity")).putExtra(EXTRA_TYPE, type))
        }
    }

    val LAT = 40.73581
    val LON = -73.99155
    val ZOOM = 14.0

    lateinit var mapView: MapView
    lateinit var map: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, BuildConfig.MAPBOX_KEY)

        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            map = it

            val target = LatLng(LAT, LON)

            map.cameraPosition = CameraPosition.Builder(map.cameraPosition)
                    .target(target)
                    .zoom(ZOOM)
                    .build()

            val elapsed = measureNanoTime {
                randomPoints(target).forEachIndexed { i, latlon ->
                    map.addMarker(MarkerOptions().title(Integer.toString(i)).position(latlon))
                }
            }

            val millis = TimeUnit.NANOSECONDS.toMillis(elapsed)
            Log.d("KYLE", "elapsed $millis millis")
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

    fun randomPoints(start: LatLng, count: Int = 1000): List<LatLng> {

        val rand = Random()

        var lat = start.latitude
        var lon = start.longitude

        val list = ArrayList<LatLng>()

        for (i in 0..count) {

            val nextLatSign = if (rand.nextBoolean()) 1 else -1
            val nextLonSign = if (rand.nextBoolean()) 1 else -1

            val stepLat = rand.nextInt(1000) / (1000.0 * 100.0)
            val stepLon = rand.nextInt(1000) / (1000.0 * 100.0)

            val nextLat = lat + nextLatSign * stepLat
            val nextLon = lon + nextLonSign * stepLon

            list.add(LatLng(nextLat, nextLon))

            lat = nextLat
            lon = nextLon
        }

        return list
    }
}
