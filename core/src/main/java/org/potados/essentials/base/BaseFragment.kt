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

package org.potados.essentials.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import org.potados.essentials.failable.FailableContainer
import org.potados.essentials.failable.FailableHandler
import org.potados.essentials.extension.notify
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.potados.essentials.failable.Failable
import org.potados.essentials.failable.FailableHandlerDefaultImpl
import timber.log.Timber

/**
 * Base Fragment that handles Failable.
 */
abstract class BaseFragment : Fragment(), Failable, FailableContainer, FailableHandler, KoinComponent {

    private val mContext: Context by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Failables are observed after the activity is created.
        // So all the failables that need to be handled must be added
        // before onActivityCreated.
        // It is recommended to insert it in init{}.
        startObservingFailables(failables)
    }

    override fun onDestroy() {
        super.onDestroy()

        stopObservingFailables()
    }

    /******************************
     * AS A Failable
     ******************************/
    private val failure = MutableLiveData<Failable.Failure>()
    override fun getFailure(): MutableLiveData<Failable.Failure> = failure
    override fun setFailure(failure: Failable.Failure) {
        this.failure.postValue(failure)
        Timber.w("Failure is set: ${failure.message}")
    }
    override fun fail(@StringRes message: Int, vararg formatArgs: Any?, show: Boolean) {
        setFailure(Failable.Failure(mContext.getString(message, *formatArgs), show))
    }

    /******************************
     * AS A FailableContainer
     ******************************/
    override val failables: MutableList<Failable> = mutableListOf()

    /******************************
     * AS A FailableHandler
     ******************************/
    override val observedFailables: MutableList<Failable> = mutableListOf()
    @CallSuper override fun onFail(failure: Failable.Failure) {
        if (failure.show) {
            notify(failure.message, long = failure.show)
        }

        Timber.w("Failure in ${this::class.java.name} with message: $failure")
    }
    final override fun startObservingFailables(failables: List<Failable>) {
        FailableHandlerDefaultImpl.startObservingFailables(this, this, failables)
    }

    final override fun stopObservingFailables() {
        FailableHandlerDefaultImpl.stopObservingFailables(this, this)
    }
}