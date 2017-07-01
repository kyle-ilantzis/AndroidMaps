/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.kyleilantzis.androidmaps.espresso

import android.support.test.espresso.IdlingResource

/**
 * Contains a static reference to [IdlingResource], only available in the 'mock' build type.
 */
object EspressoIdlingResource {

    private val RESOURCE_NAME = "GLOBAL"

    private var sIdlingResource: SimpleCountingIdlingResource? = null

    @Synchronized fun increment() {
        sIdlingResource?.apply {
            increment()
        }
    }

    @Synchronized fun decrement() {
        sIdlingResource?.apply {
            decrement()
        }
    }

    @Synchronized fun createIdlingResource(): IdlingResource {
        val i = SimpleCountingIdlingResource(RESOURCE_NAME)
        sIdlingResource = i
        return i
    }

    val idlingResource: IdlingResource?
        @Synchronized get() = sIdlingResource
}
