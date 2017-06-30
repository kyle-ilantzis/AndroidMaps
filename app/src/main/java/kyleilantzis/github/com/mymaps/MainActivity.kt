package kyleilantzis.github.com.mymaps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val b1 = findViewById(R.id.googleMapsBtn) as Button
        b1.setOnClickListener {  }

        val b2 = findViewById(R.id.mapboxBtn) as Button
        b2.setOnClickListener { MapActivity.start(this, MapActivity.TYPE_MAPBOX_1000_POINTS) }
    }
}
