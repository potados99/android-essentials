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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import org.koin.core.inject

/**
 * A base Fragment that will be each page of bottom navigation.
 */
abstract class NavigationFragment : BaseFragment() {
    abstract val layoutRes: Int
    abstract val toolbarId: Int
    abstract val navHostId: Int

    private val appBarConfig: AppBarConfiguration by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onStart() {
        super.onStart()
        setUpToolbarAndNavController()
    }

    fun onBackPressed(): Boolean {
        return getNavController().navigateUp(appBarConfig)
    }

    fun popToRoot() {
        getNavController().let {
            it.popBackStack(it.graph.startDestination, false)
        }
    }

    fun handleDeepLink(intent: Intent) = requireActivity().findNavController(navHostId).handleDeepLink(intent)

    private fun getNavController(): NavController = requireActivity().findNavController(navHostId)

    private fun getToolbar(): Toolbar = requireActivity().findViewById(toolbarId)

    private fun setUpToolbarAndNavController() =
        NavigationUI.setupWithNavController(getToolbar(), getNavController(), appBarConfig)
}