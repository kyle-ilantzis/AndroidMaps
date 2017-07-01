package com.github.kyleilantzis.androidmaps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        (findViewById(R.id.googleMaps_1000_points_btn) as Button).setOnClickListener {
            MapActivity.start(this, MapActivity.TYPE_GOOGLEMAPS, MapActivity.ACTION_1000_POINTS)
        }

        (findViewById(R.id.mapbox_1000_points_btn) as Button).setOnClickListener {
            MapActivity.start(this, MapActivity.TYPE_MAPBOX, MapActivity.ACTION_1000_POINTS)
        }

        (findViewById(R.id.googleMaps_1000_similar_points_btn) as Button).setOnClickListener {
            MapActivity.start(this, MapActivity.TYPE_GOOGLEMAPS, MapActivity.ACTION_1000_SIMILAR_POINTS)
        }

        (findViewById(R.id.mapbox_1000_similar_points_btn) as Button).setOnClickListener {
            MapActivity.start(this, MapActivity.TYPE_MAPBOX, MapActivity.ACTION_1000_SIMILAR_POINTS)
        }

        (findViewById(R.id.googleMaps_1000_different_points_btn) as Button).setOnClickListener {
            MapActivity.start(this, MapActivity.TYPE_GOOGLEMAPS, MapActivity.ACTION_1000_DIFFERENT_POINTS)
        }

        (findViewById(R.id.mapbox_1000_different_points_btn) as Button).setOnClickListener {
            MapActivity.start(this, MapActivity.TYPE_MAPBOX, MapActivity.ACTION_1000_DIFFERENT_POINTS)
        }
    }
}
