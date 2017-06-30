package com.github.kyleilantzis.androidmaps

import android.graphics.Bitmap
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.maps.MapboxMap

fun mapViewFrom(view: com.mapbox.mapboxsdk.maps.MapView): CommonMapView =
        MapboxCommonMapView(view)


fun mapViewFrom(view: com.google.android.gms.maps.MapView): CommonMapView =
        GoogleCommonMapView(view)

interface CommonMapView {

    val view: Any

    fun getMapAsync(callback: (CommonMap) -> Unit)

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onSaveInstanceState(outState: Bundle)

    fun onStop()

    fun onLowMemory()

    fun onDestroy()
}

interface CommonMap {

    val map: Any

    var target: Pair<Double, Double>

    fun addMarker(icon: CommonIcon?, title: String?, latlon: Pair<Double, Double>)

    fun makeCommonIcon(bmp: Bitmap): CommonIcon
}

interface CommonIcon {
}

class MapboxCommonMapView(override val view: com.mapbox.mapboxsdk.maps.MapView) :
        CommonMapView {

    override fun getMapAsync(callback: (CommonMap) -> Unit) {
        view.getMapAsync {
            val iconFactory = IconFactory.getInstance(view.context)
            callback(MapboxCommonMap(it, iconFactory))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        view.onCreate(savedInstanceState)
    }

    override fun onStart() {
        view.onStart()
    }

    override fun onResume() {
        view.onResume()
    }

    override fun onPause() {
        view.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        view.onSaveInstanceState(outState)
    }

    override fun onStop() {
        view.onStop()
    }

    override fun onLowMemory() {
        view.onLowMemory()
    }

    override fun onDestroy() {
        view.onDestroy()
    }
}

class MapboxCommonMap(override val map: MapboxMap, val iconFactory: IconFactory) :
        CommonMap {

    override var target: Pair<Double, Double>
        get() {
            val t = map.cameraPosition.target
            return t.latitude to t.longitude
        }
        set(value) {
            map.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(com.mapbox.mapboxsdk.geometry.LatLng(value.first, value.second), 14.0))
        }

    override fun addMarker(icon: CommonIcon?, title: String?, latlon: Pair<Double, Double>) {

        val options = com.mapbox.mapboxsdk.annotations.MarkerOptions()

        if (icon != null) {
            options.icon((icon as MapboxCommonIcon).icon)
        }

        options.title(title)
        options.position = com.mapbox.mapboxsdk.geometry.LatLng(latlon.first, latlon.second)

        map.addMarker(options)
    }

    override fun makeCommonIcon(bmp: Bitmap): CommonIcon {
        val icon = iconFactory.fromBitmap(bmp)
        return MapboxCommonIcon(icon)
    }
}

data class MapboxCommonIcon(val icon: Icon) : CommonIcon

class GoogleCommonMapView(override val view: com.google.android.gms.maps.MapView) :
        CommonMapView {

    override fun getMapAsync(callback: (CommonMap) -> Unit) {
        view.getMapAsync { callback(GoogleCommonMap(it)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        view.onCreate(savedInstanceState)
    }

    override fun onStart() {
        view.onStart()
    }

    override fun onResume() {
        view.onResume()
    }

    override fun onPause() {
        view.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        view.onSaveInstanceState(outState)
    }

    override fun onStop() {
        view.onStop()
    }

    override fun onLowMemory() {
        view.onLowMemory()
    }

    override fun onDestroy() {
        view.onDestroy()
    }
}

class GoogleCommonMap(override val map: GoogleMap) :
        CommonMap {

    override var target: Pair<Double, Double>
        get() {
            val t = map.cameraPosition.target
            return t.latitude to t.longitude
        }
        set(value) {
            map.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(com.google.android.gms.maps.model.LatLng(value.first, value.second), 14.0F))
        }

    override fun addMarker(icon: CommonIcon?, title: String?, latlon: Pair<Double, Double>) {

        val options = com.google.android.gms.maps.model.MarkerOptions()

        if (icon != null) {
            options.icon((icon as GoogleCommonIcon).desc)
        }

        options.title(title)
        options.position(com.google.android.gms.maps.model.LatLng(latlon.first, latlon.second))

        map.addMarker(options)
    }

    override fun makeCommonIcon(bmp: Bitmap): CommonIcon {
        val desc = BitmapDescriptorFactory.fromBitmap(bmp)
        return GoogleCommonIcon(desc)
    }
}

data class GoogleCommonIcon(val desc: BitmapDescriptor) : CommonIcon
