package com.devlogs.rssfeed.screens.navigation_drawer.mvc_view

import android.view.ViewGroup
import com.devlogs.rssfeed.screens.common.mvcview.MvcViewFactory
import com.devlogs.rssfeed.screens.common.mvcview.ObservableMvcView
import com.devlogs.rssfeed.screens.navigation_drawer.presentable_model.ChannelPresentableModel
import com.devlogs.rssfeed.screens.navigation_drawer.presentable_model.UserPresentableModel

interface MainNavMvcView : ObservableMvcView<MainNavMvcView.Listener> {

    interface Listener {
        fun onBtnSignOutClicked()
        fun onChannelSelected (channel: ChannelPresentableModel)
    }

    fun loading ()
    fun setChannels(channels : List<ChannelPresentableModel>)
    fun setUsers(user: UserPresentableModel)
}


fun MvcViewFactory.getMainNavMvcView (viewGroup: ViewGroup?) : MainNavMvcView = MainNavMvcViewImp(uiToolkit, viewGroup)