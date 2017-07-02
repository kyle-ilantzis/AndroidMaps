package com.github.kyleilantzis.androidmaps

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.kyleilantzis.androidmaps.espresso.EspressoIdlingResource
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    val POINTS = BuildConfig.POINTS

    companion object {

        @JvmStatic
        val testResults = ArrayList<TestResult>()

        @AfterClass
        @JvmStatic
        fun afterClass() {

            val printWriter = PrintWriter(InstrumentationRegistry.getTargetContext().openFileOutput(TestResultsActivity.FILE_NAME, 0))

            for (testResult in testResults) {

                val name = testResult.name
                val repeated = testResult.repeated
                val markers = testResult.markerMillis
                val icons = testResult.iconMillis

                printWriter.println("==== ====<br/>")
                printWriter.println("==== $name ====<br/>")
                printWriter.println("==== ====<br/>")

                printWriter.println("<br/>")
                printWriter.println("repeated: $repeated<br/>")

                printWriter.println("<br/>")
                printWriter.println("markers: " + Arrays.toString(markers) + "<br/>")
                printWriter.println("markers average: " + markers.sum() / markers.size.toDouble() + "<br/>")

                printWriter.println("<br/>")
                printWriter.println("icons: " + Arrays.toString(icons) + "<br/>")
                printWriter.println("icons average: " + icons.sum() / icons.size.toDouble() + "<br/>")

                printWriter.println("==== ====<br/>")
                printWriter.println("==== // $name ====<br/>")
                printWriter.println("==== ====<br/>")
            }

            printWriter.close()
        }
    }

    @Before
    fun before() {
        Espresso.registerIdlingResources(EspressoIdlingResource.createIdlingResource())
    }

    @After
    fun after() {
        EspressoIdlingResource.idlingResource?.also {
            Espresso.unregisterIdlingResources(it)
        }
    }

    @Test()
    fun test_googlemaps_points() {
        doActionAndMeasureTime("Google Maps $POINTS points", R.id.googlemaps_points_btn)
    }

    @Test()
    fun test_mapbox_points() {
        doActionAndMeasureTime("Mapbox $POINTS points", R.id.mapbox_points_btn)
    }

    @Test()
    fun test_googlemaps_similar_points() {
        doActionAndMeasureTime("Google Maps $POINTS similar points", R.id.googlemaps_similar_points_btn)
    }

    @Test()
    fun test_mapbox_similar_points() {
        doActionAndMeasureTime("Mapbox $POINTS similar points", R.id.mapbox_similar_points_btn)
    }

    @Test()
    fun test_googlemaps_different_points() {
        doActionAndMeasureTime("Google Maps $POINTS different points", R.id.googlemaps_different_points_btn)
    }

    @Test()
    fun test_mapbox_different_points() {
        doActionAndMeasureTime("Mapbox $POINTS different points", R.id.mapbox_different_points_btn)
    }

    fun doActionAndMeasureTime(name: String, btnId: Int) {

        val markers = Array<Long>(BuildConfig.REPEAT) { 0 }
        val icons = Array<Long>(BuildConfig.REPEAT) { 0 }

        for (i in 0 until BuildConfig.REPEAT) {

            onView(withId(btnId)).perform(click())

            assertEquals(true, MapActivity.actionDone)

            markers[i] = MapActivity.markerMillis
            icons[i] = MapActivity.iconMillis

            pressBack()
        }

        testResults.add(TestResult(name, BuildConfig.REPEAT, markers, icons))
    }

    data class TestResult(val name: String, val repeated: Int, val markerMillis: Array<Long>, val iconMillis: Array<Long>)
}