package com.github.kyleilantzis.androidmaps

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

            for (testResult in testResults) {

                val name = testResult.name
                val repeated = testResult.repeated
                val markers = testResult.markerMillis
                val icons = testResult.iconMillis

                System.out.println("==== ====")
                System.out.println("==== $name ====")
                System.out.println("==== ====")

                System.out.println()
                System.out.println("repeated: $repeated")

                System.out.println()
                System.out.println("markers: " + Arrays.toString(markers))
                System.out.println("markers average: " + markers.sum() / markers.size.toDouble())

                System.out.println()
                System.out.println("icons: " + Arrays.toString(icons))
                System.out.println("icons average: " + icons.sum() / icons.size.toDouble())

                System.out.println("==== ====")
                System.out.println("==== // $name ====")
                System.out.println("==== ====")
            }
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
        val times = doActionAndMeasureTime(BuildConfig.REPEAT) { onView(withId(R.id.googlemaps_points_btn)).perform(click()) }
        testResults.add(TestResult("Google maps $POINTS points", BuildConfig.REPEAT, times.first, times.second))
    }

    fun doActionAndMeasureTime(repeat: Int, block: () -> Unit): Pair<Array<Long>, Array<Long>> {

        val markers = Array<Long>(repeat) { 0 }
        val icons = Array<Long>(repeat) { 0 }

        for (i in 0 until repeat) {

            block()

            assertEquals(true, MapActivity.actionDone)

            markers[i] = MapActivity.markerMillis
            icons[i] = MapActivity.iconMillis

            pressBack()
        }

        return markers to icons
    }

    data class TestResult(val name: String, val repeated: Int, val markerMillis: Array<Long>, val iconMillis: Array<Long>)
}