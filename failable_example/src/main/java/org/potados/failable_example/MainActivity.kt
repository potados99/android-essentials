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

package org.potados.failable_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.potados.failable.Fail
import org.potados.failable.base.Failure
import org.potados.failable.base.NetworkFailure
import org.potados.failable.extensions.onFail

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeView()

        Handler().postDelayed({
            Worker().doSome()
        }, 1000)
    }

    private fun initializeView() {
        debugButton.setOnClickListener {
            Fail.debug(Failure("debug"))
        }

        usualButton.setOnClickListener {
            Fail.usual(Failure("usual"))
        }

        wtfButton.setOnClickListener {
            Fail.wtf(NetworkFailure("Ooh network down"))
        }
        
        onFail { failure, channel ->
            Log.d("MainActivity", "$failure on $channel")
        }

    }
}
