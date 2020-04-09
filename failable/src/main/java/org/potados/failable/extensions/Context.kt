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

package org.potados.failable.extensions

import android.content.Context
import androidx.annotation.StringRes
import org.potados.failable.Fail
import org.potados.failable.base.Failure

fun Context.debug(@StringRes message: Int, vararg formatArgs: Any?, visible: Boolean=true) {
    val failure = Failure(getString(message, *formatArgs), visible)

    Fail.debug(failure)
}

fun Context.fail(@StringRes message: Int, vararg formatArgs: Any?, visible: Boolean=true) {
    val failure = Failure(getString(message, *formatArgs), visible)

    Fail.usual(failure)
}

fun Context.wtf(@StringRes message: Int, vararg formatArgs: Any?, visible: Boolean=true) {
    val failure = Failure(getString(message, *formatArgs), visible)

    Fail.wtf(failure)
}