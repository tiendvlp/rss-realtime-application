package com.devlogs.rssfeed.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.devlogs.rssfeed.screens.ReadFeeds.controller.ReadFeedsFragment
import com.devlogs.rssfeed.screens.add_rss_channel.controller.AddRssChannelFragment
import com.devlogs.rssfeed.screens.saved_feeds.controller.SavedFeedsFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavController.Companion.TAB1
import com.ncapdevi.fragnav.FragNavController.Companion.TAB2
import com.ncapdevi.fragnav.FragNavController.Companion.TAB3
import java.lang.IndexOutOfBoundsException

class MainScreenNavigator {
    private val fragNavController: FragNavController
    private val rootFragmentListener : FragNavController.RootFragmentListener = object : FragNavController.RootFragmentListener {
        override val numberOfRootFragments: Int
            get() = 3

    private lateinit var savedFeedsFragment: SavedFeedsFragment
    private lateinit var readFeedsFragment: ReadFeedsFragment
    private lateinit var addRssChannelFragment: AddRssChannelFragment

        override fun getRootFragment(index: Int): Fragment {
            return when (index) {
                TAB1 -> {
                    if (!::savedFeedsFragment.isInitialized) {
                        savedFeedsFragment = SavedFeedsFragment.newInstance()
                    }
                    savedFeedsFragment
                }
                TAB2 -> {
                    if (!::readFeedsFragment.isInitialized) {
                        readFeedsFragment = ReadFeedsFragment.newInstance()
                    }
                    readFeedsFragment
                }
                TAB3 -> {
                    if (!::addRssChannelFragment.isInitialized) {
                        addRssChannelFragment = AddRssChannelFragment.newInstance()
                    }
                    addRssChannelFragment
                }
                else -> throw IndexOutOfBoundsException("MainScreenNavigator only has 4 tabs but tab ${index}th was accessed")
            }
        }
    }

    constructor(fragNavController: FragNavController) {
        this.fragNavController = fragNavController
    }

    fun init (savedInstanceState : Bundle?) {
        fragNavController.rootFragmentListener = rootFragmentListener
        fragNavController.initialize(TAB2, savedInstanceState)
    }

    fun onSavedInstanceState (outState : Bundle?) {
        fragNavController.onSaveInstanceState(outState)
    }

    fun navigateBackToRoot () {
        fragNavController.clearStack()
    }

    fun switchToFeedsTab () {
        fragNavController.switchTab(TAB2)
    }

    fun switchToSavedFeedsTab () {
        fragNavController.switchTab(TAB1)
    }

    fun switchToAddChannelTab () {
        fragNavController.switchTab(TAB3)
    }

    fun navigateBack () : Boolean{
        if (fragNavController.isRootFragment) {
            return false
        }
        fragNavController.popFragment()
        return true
    }
}