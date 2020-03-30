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

package org.potados.essentials.extension

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelProviders
import org.potados.essentials.widget.Notify

/**
 * Get ViewModel of the childFragment without SingleUseCaseViewModelFactory.
 */
inline fun <reified T : ViewModel> Fragment.getViewModel(body: T.() -> Unit = {}): T {
    return ViewModelProviders.of(this).get(T::class.java).apply(body)
}

val Fragment.supportActionBar: ActionBar? get() = (activity as? AppCompatActivity)?.supportActionBar
fun Fragment.setSupportActionBar(toolbar: Toolbar, title: Boolean = false, upButton: Boolean = false) {
    withNonNull(activity as? AppCompatActivity) {
        setSupportActionBar(toolbar)

        withNonNull(supportActionBar) {
            setDisplayShowTitleEnabled(title)
            setDisplayHomeAsUpEnabled(upButton)
        }
    }
}

fun Fragment.notify(message: String?, long: Boolean = false) {
    message?.let {
        with(Notify(context)) {
            if (long) long(it) else short(it)
        }
    }
}