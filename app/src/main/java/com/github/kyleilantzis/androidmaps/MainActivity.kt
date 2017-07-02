package com.github.kyleilantzis.androidmaps

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupButton(R.id.googlemaps_points_btn, R.string.googlemaps_points, MapActivity.TYPE_GOOGLEMAPS, MapActivity.ACTION_1000_POINTS)

        setupButton(R.id.mapbox_points_btn, R.string.mapbox_points, MapActivity.TYPE_MAPBOX, MapActivity.ACTION_1000_POINTS)

        setupButton(R.id.googlemaps_similar_points_btn, R.string.googlemaps_similar_points, MapActivity.TYPE_GOOGLEMAPS, MapActivity.ACTION_1000_SIMILAR_POINTS)

        setupButton(R.id.mapbox_similar_points_btn, R.string.mapbox_similar_points, MapActivity.TYPE_MAPBOX, MapActivity.ACTION_1000_SIMILAR_POINTS)

        setupButton(R.id.googlemaps_different_points_btn, R.string.googlemaps_different_points, MapActivity.TYPE_GOOGLEMAPS, MapActivity.ACTION_1000_DIFFERENT_POINTS)

        setupButton(R.id.mapbox_different_points_btn, R.string.mapbox_different_points, MapActivity.TYPE_MAPBOX, MapActivity.ACTION_1000_DIFFERENT_POINTS)

        (findViewById(R.id.test_results_btn) as Button).setOnClickListener { startActivity(Intent(this, TestResultsActivity::class.java)) }

        (findViewById(R.id.test_results_btn) as Button).setOnLongClickListener {
            val file = getFileStreamPath(TestResultsActivity.FILE_NAME)
            file.delete()
            Snackbar.make(it, "File $file deleted.", Snackbar.LENGTH_SHORT).show()
            true
        }
    }

    fun setupButton(btnId: Int, stringId: Int, type: Int, action: Int) {
        (findViewById(btnId) as Button).apply {
            text = getString(stringId, BuildConfig.POINTS)
            setOnClickListener { MapActivity.start(context, type, action) }
        }
    }
}
