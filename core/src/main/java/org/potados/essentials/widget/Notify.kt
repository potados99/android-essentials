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

package org.potados.essentials.widget

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * A wrapper for Toast.
 */
class Notify(private val context: Context?) {

    fun short(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun short(@StringRes message: Int, vararg formatArgs: Any?) = short(context?.getString(message, *formatArgs))

    fun long(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun long(@StringRes message: Int) = long(context?.getString(message))

    companion object {
        fun short(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        fun long(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}