/*
 * Copyright (C) 2020 Song Byeong Jun <potados99@gmail.com>
 *
 * This file is part of android-essentials (https://github.com/potados99/android-essentials).
 * Android-essentials is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android-essentials is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.potados.failable

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.potados.failable.base.Failure
import org.potados.failable.extensions.has

class Fail {
    companion object {
        private const val TAG = "Fail"

        private const val DEBUG = 0x00
        private const val USUAL = 0x01 // default
        private const val WTF = 0x02

        private const val level = DEBUG

        private val allChannels = mapOf<Int, MutableLiveData<Failure>>(
            Pair(DEBUG, MutableLiveData()),
            Pair(USUAL, MutableLiveData()),
            Pair(WTF, MutableLiveData()))
        private val availableChannels = allChannels.filter { it.key >= level }

        fun debug(failure: Failure) {
            emit(
                failure,
                DEBUG
            )
        }

        fun usual(failure: Failure) {
            emit(
                failure,
                USUAL
            )
        }

        fun wtf(failure: Failure) {
            emit(
                failure,
                WTF
            )
        }

        private fun emit(failure: Failure, channel: Int) {
            availableChannels[USUAL]?.let { channelFound ->
                Log.d(TAG, "Failure set in channel $channel: $failure")
                channelFound.postValue(failure)
            }
        }

        fun observe(lifecycleOwner: LifecycleOwner,
                   channel: Int = DEBUG.and(
                       USUAL
                   ).and(WTF),
                   body: (Failure, Int) -> Any?) {

            val channelsToObserve = availableChannels
                .filter { channel.has(it.key) }

            channelsToObserve.forEach { entry ->
                Log.d(TAG, "$lifecycleOwner listening on ${entry.key} channel")
                entry.value.observe(lifecycleOwner, Observer { failure ->
                    body(failure, entry.key)
                })
            }
        }

        fun removeObservers(lifecycleOwner: LifecycleOwner) {
            availableChannels.forEach { entry ->
                Log.d(TAG, "$lifecycleOwner removing observers on ${entry.key}")
                entry.value.removeObservers(lifecycleOwner)
            }
        }
    }
}