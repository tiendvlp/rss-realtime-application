package com.devlogs.rssfeed.screens.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.devlogs.chatty.screen.common.BackPressDispatcher
import com.devlogs.chatty.screen.common.BackPressListener
import com.devlogs.rssfeed.R
import com.devlogs.rssfeed.screens.navigation_drawer.controller.MainNavFragment
import com.google.android.material.navigation.NavigationView
import com.ncapdevi.fragnav.FragNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BackPressDispatcher {
    companion object {
        fun start(currentContext: Context) {
            currentContext.startActivity(Intent(currentContext, MainActivity::class.java))
        }
    }

    interface ReloadAble {
        fun reload ()
    }

    private var backPressListeners: HashSet<BackPressListener> = HashSet()

    private lateinit var reloadAble: ReloadAble
    private lateinit var grBottomNav : RadioGroup
    private lateinit var btnMenu : Button
    private lateinit var rbtnFavoriteFeed : RadioButton
    private lateinit var rbtnReadFeeds : RadioButton
    private lateinit var rbtnAddChannel : RadioButton
    private lateinit var drawer : DrawerLayout
    private lateinit var navigation: NavigationView

    @Inject
    protected lateinit var mainScreenNavigator: MainScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        mainScreenNavigator.init(savedInstanceState)
        addControls ()
        addEvents()
        setupNavDrawer()
        grBottomNav.check(rbtnReadFeeds.id)
    }

    private fun setupNavDrawer() {
        val newFragment = MainNavFragment.newInstance()
        reloadAble = newFragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navFragContent, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun addControls() {
        grBottomNav = findViewById(R.id.grBottomNav)
        btnMenu = findViewById(R.id.btnMenu)
        rbtnFavoriteFeed = findViewById(R.id.rbtnFavoriteFeed)
        rbtnReadFeeds = findViewById(R.id.rbtnReadFeeds)
        rbtnAddChannel = findViewById(R.id.rbtnAddChannel)
        drawer = findViewById(R.id.drawer)
        navigation = findViewById(R.id.navigation)
    }

    private fun addEvents() {
        btnMenu.setOnClickListener {
            drawer.openDrawer(Gravity.LEFT)
            reloadAble.reload()

        }

        mainScreenNavigator.onTabChanged = {
            when (it) {
                0 -> rbtnFavoriteFeed.isChecked = true
                1 -> rbtnReadFeeds.isChecked = true
                2 -> rbtnAddChannel.isChecked = true
            }
        }

        grBottomNav.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                rbtnFavoriteFeed.id -> mainScreenNavigator.switchToCategoriesFeedsTab()
                rbtnReadFeeds.id -> mainScreenNavigator.switchToFeedsTab()
                rbtnAddChannel.id -> mainScreenNavigator.switchToAddChannelTab()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mainScreenNavigator.onSavedInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun register(backPressListener: BackPressListener) {
        backPressListeners.add(backPressListener)
    }

    override fun unregister(backPressListener: BackPressListener) {
        backPressListeners.remove(backPressListener)
    }

    override fun onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT)
            return
        }

        var isBackPressConsumed = false

        backPressListeners.forEach { listener ->
            if (listener.onBackPress()) {
                isBackPressConsumed = true
            }
        }

        if (isBackPressConsumed) {
            return
        }

        if (!mainScreenNavigator.navigateBack()) {
            super.onBackPressed()
        }
    }
}