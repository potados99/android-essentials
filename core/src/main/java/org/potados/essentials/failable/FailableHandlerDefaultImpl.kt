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

package org.potados.essentials.failable

import androidx.lifecycle.LifecycleOwner
import org.potados.essentials.extension.observe
import timber.log.Timber

class FailableHandlerDefaultImpl {

    companion object {
        fun startObservingFailables(handler: FailableHandler, owner: LifecycleOwner, failables: List<Failable>) {
            failables.forEach {
                if (it is FailableContainer) {
                    // 만약 현재 Failable이 FailableContainer이기도 할 경우,
                    // 재귀적으로 해당 컨테이너 안의 Failable들도 추가해줌.
                    startObservingFailables(handler, owner, it.failables)
                } else {
                    // 중복 observe를 막기 위해 먼저 owner가 자신인 구독을 취소함.
                    it.getFailure().removeObservers(owner)

                    owner.observe(it.getFailure()) { failure ->
                        // null이 아닐 때에만 이를 처리함.
                        failure?.let(handler::onFail)

                        // 이 Failure는 처리되었으므로 failure event에 null을 집어넣음.
                        it.getFailure().postValue(null)
                    }

                    handler.observedFailables.add(it)

                    Timber.i("Started observing of ${failables.joinToString{ f -> f::class.java.name }}.")
                }
            }
        }

        fun stopObservingFailables(handler: FailableHandler, owner: LifecycleOwner) {
            handler.observedFailables.forEach {
                it.getFailure().removeObservers(owner)
            }
            handler.observedFailables.clear()

            Timber.i("Stopped observing of ${handler.observedFailables.joinToString{ it::class.java.name }}.")

        }

    }
}