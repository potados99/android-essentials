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

package org.potados.navigation.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.navigation_activity.*
import org.potados.navigation.R
import java.util.*

/**
 * A base Activity that acts as a host of bottom navigation.
 */
abstract class NavigationActivity : AppCompatActivity(),
    ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * overall back stack of containers
     * IMPORTANT: back stack does not contain the current container,
     * and has no duplicated members.
     * For example: when tab moves 3 2 1 4 3 2 3 0 3 2 0 1 4 2:
     * the stack will look like: 3 2 0 1 4 (and current 2).
     */
    private val backStack = Stack<Int>()

    /**
     * List of fragments that will be held in the bottom navigation.
     */
    abstract val fragments: List<NavigationFragment>

    /**
     * Map of { tab position : tab id }.
     */
    abstract val indexToPage: Map<Int, Int>

    /**
     * Bottom navigation menu resource id
     */
    abstract val menuRes: Int

    /********************************
     * AppCompatActivity
     ********************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_activity)

        initViewPager()
        initBottomNavigation()
    }

    private fun initViewPager() {
        with(main_pager) {
            addOnPageChangeListener(this@NavigationActivity)
            adapter = ViewPagerAdapter()
            post(::checkDeepLink)
            offscreenPageLimit = fragments.size
        }
    }

    private fun checkDeepLink() {
        fragments.forEachIndexed { index, fragment ->
            val hasDeepLink = fragment.handleDeepLink(intent)
            if (hasDeepLink) {
                setItem(index)
            }
        }
    }

    private fun initBottomNavigation() {
        with(bottom_nav) {
            menu.clear()
            inflateMenu(menuRes)

            setOnNavigationItemSelectedListener(this@NavigationActivity)
            setOnNavigationItemReselectedListener(this@NavigationActivity)
        }
    }

    override fun onBackPressed() {
        val fragment = fragments[main_pager.currentItem]
        val hadNestedFragments = fragment.onBackPressed()
        // if no fragments were popped
        if (!hadNestedFragments) {
            if (backStack.size > 0) {
                main_pager.currentItem = backStack.pop()
            } else {
                super.onBackPressed()
            }
        }
    }

    /********************************
     * ViewPager
     ********************************/
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) = markTabSelected(position)

    /********************************
     * BottomNavigationView
     ********************************/
    override fun onNavigationItemReselected(item: MenuItem) = getHostFragmentByTabItem(item).popToRoot()

    override fun onNavigationItemSelected(item: MenuItem): Boolean = setItem(item)

    private fun getHostFragmentByTabItem(item: MenuItem): NavigationFragment = fragments[indexToPage.values.indexOf(item.itemId)]

    private fun setItem(item: MenuItem): Boolean = setItem(indexToPage.values.indexOf(item.itemId))

    private fun setItem(position: Int): Boolean {
        with(main_pager) {
            if (currentItem != position) {
                // Prevent duplication in back stack.
                backStack.removeIf { it == currentItem }
                backStack.push(currentItem)

                currentItem = position
            }
        }
        return true
    }

    private fun markTabSelected(position: Int) {
        val itemId = indexToPage[position] ?: return

        with(bottom_nav) {
            if (selectedItemId != itemId) {
                selectedItemId = itemId
            }
        }
    }

    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragments[position]
        override fun getCount(): Int = fragments.size
    }
}