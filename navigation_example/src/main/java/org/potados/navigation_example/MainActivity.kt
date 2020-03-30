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

package org.potados.navigation_example

import org.potados.navigation.base.NavigationActivity
import org.potados.navigation.base.NavigationFragment

class MainActivity : NavigationActivity() {

    override val fragments = listOf(
        NavigationFragment.newInstance(
            layoutRes = R.layout.content_home_base,
            toolbarId = R.id.toolbar_home,
            navHostId = R.id.nav_host_home,
            tabItemId = R.id.home,
            rootDests = rootDestinations),

        NavigationFragment.newInstance(
            layoutRes = R.layout.content_library_base,
            toolbarId = R.id.toolbar_library,
            navHostId = R.id.nav_host_library,
            tabItemId = R.id.library,
            rootDests = rootDestinations),

        NavigationFragment.newInstance(
            layoutRes = R.layout.content_settings_base,
            toolbarId = R.id.toolbar_settings,
            navHostId = R.id.nav_host_settings,
            tabItemId = R.id.settings,
            rootDests = rootDestinations))

    override val menuRes: Int = R.menu.menu_main
}
