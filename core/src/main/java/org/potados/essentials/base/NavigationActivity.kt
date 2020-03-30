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

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.navigation_activity.*
import org.potados.essentials.R
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
    abstract val hostFragments: List<NavigationHostFragment>

    /**
     * Map of { tab position : tab id }.
     */
    abstract val indexToPage: Map<Int, Int>

    /********************************
     * AppCompatActivity
     ********************************/
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.navigation_activity)

        initViewPager()
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
    override fun onNavigationItemReselected(item: MenuItem) = getFragmentByTabItem(item).popToRoot()
    override fun onNavigationItemSelected(item: MenuItem): Boolean = setItem(item)

    /**
     * Get fragment by selected tab item.
     */
    private fun getFragmentByTabItem(item: MenuItem): NavigationHostFragment = hostFragments[indexToPage.values.indexOf(item.itemId)]

    /**
     * Set ViewPager to show page selected by tab.
     */
    private fun setItem(item: MenuItem): Boolean = setItem(indexToPage.values.indexOf(item.itemId))

    /**
     * Set ViewPager to show page using its position.
     */
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

    /**
     * Why need it?
     * Example case: page is selected by entering through deep link.
     * In this case the tab item is not selected but need to be marked selected.
     */
    private fun markTabSelected(position: Int) {
        val itemId = indexToPage[position] ?: return

        with(bottom_nav) {
            if (selectedItemId != itemId) {
                selectedItemId = itemId
            }
        }
    }

    private fun initViewPager() {
        with(main_pager) {
            addOnPageChangeListener(this@NavigationActivity)
            adapter = ViewPagerAdapter()
            post(::checkDeepLink)
            offscreenPageLimit = hostFragments.size
        }
    }

    private fun checkDeepLink() {
        hostFragments.forEachIndexed { index, fragment ->
            if (fragment.handleDeepLink(intent)) {
                // has deep link.
                setItem(index)
            }
        }
    }

    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = hostFragments[position]
        override fun getCount(): Int = hostFragments.size
    }
}