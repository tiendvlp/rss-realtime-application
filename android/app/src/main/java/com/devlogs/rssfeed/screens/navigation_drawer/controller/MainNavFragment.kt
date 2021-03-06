package com.devlogs.rssfeed.screens.navigation_drawer.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devlogs.rssfeed.application.ApplicationStateManager
import com.devlogs.rssfeed.authentication.SignOutUseCase
import com.devlogs.rssfeed.screens.common.mvcview.MvcViewFactory
import com.devlogs.rssfeed.screens.main.MainActivity
import com.devlogs.rssfeed.screens.main.MainScreenInsiderObservable
import com.devlogs.rssfeed.screens.main.MainScreenNavigator
import com.devlogs.rssfeed.screens.navigation_drawer.mvc_view.MainNavMvcView
import com.devlogs.rssfeed.screens.navigation_drawer.mvc_view.getMainNavMvcView
import com.devlogs.rssfeed.screens.navigation_drawer.presentable_model.ChannelPresentableModel
import com.devlogs.rssfeed.screens.navigation_drawer.presentable_model.UserPresentableModel
import com.devlogs.rssfeed.screens.splash_screen.controller.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainNavFragment : Fragment(), MainNavMvcView.Listener, MainActivity.ReloadAble {

    @Inject
    protected lateinit var mvcViewFactory: MvcViewFactory
    @Inject
    protected lateinit var channelController: ChannelsController
    @Inject
    protected lateinit var applicationStateManager: ApplicationStateManager
    @Inject
    protected lateinit var mainScreenNavigator: MainScreenNavigator
    @Inject
    protected lateinit var mainScreenInsiderObservable: MainScreenInsiderObservable
    @Inject
    protected lateinit var signOutUseCase: SignOutUseCase

    private lateinit var mvcView: MainNavMvcView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mvcView = mvcViewFactory.getMainNavMvcView(container)
        val user = applicationStateManager.user!!
        mvcView.setUsers(UserPresentableModel(user.name, user.avatar))
        channelController.setMvcView(mvcView)
        channelController.getChannels()

        return mvcView.getRootView()
    }

    override fun onStart() {
        super.onStart()
        mvcView.register(this)
    }

    override fun onStop() {
        super.onStop()
        mvcView.unRegister(this)
    }


    companion object {
        @JvmStatic
        fun newInstance() = MainNavFragment()
    }

    override fun onBtnSignOutClicked() {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            signOutUseCase.executes()
            SplashActivity.start(requireContext())
            requireActivity().finishAffinity()
        }
    }

    override fun onChannelSelected(channel: ChannelPresentableModel) {
        applicationStateManager.selectedChannelId = channel.id
        mainScreenInsiderObservable.userSelectChannel(channel.id)
        mainScreenNavigator.switchToFeedsTab()
        requireActivity().onBackPressed()
    }

    override fun reload() {
        channelController.getChannels()
    }
}