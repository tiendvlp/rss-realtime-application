package com.devlogs.rssfeed.screens.read_feeds.mvc_view

import android.view.ViewGroup
import com.devlogs.rssfeed.screens.read_feeds.presentable_model.FeedPresentableModel
import com.devlogs.rssfeed.screens.read_feeds.presentable_model.RssChannelPresentableModel
import com.devlogs.rssfeed.screens.common.mvcview.MvcViewFactory
import com.devlogs.rssfeed.screens.common.mvcview.ObservableMvcView
import com.devlogs.rssfeed.screens.read_feeds.presentable_model.ReadFeedPresentableModel
import java.util.*

interface ReadFeedsMvcView : ObservableMvcView<ReadFeedsMvcView.Listener> {
    interface Listener {
        fun onFeedClicked(selectedFeeds: FeedPresentableModel)
        fun onFeedSavedClicked(selectedFeeds: FeedPresentableModel)
        fun onLoadMoreFeeds()
        fun onReload()
        fun onBtnFollowClicked()
        fun onBtnUnFollowClicked()

    }

    fun scrollToPos (position: Int)
    fun getCurrentScrollPos () : Int
    fun setUserAvatarUrl (url:String?)
    fun setChannels (channel: RssChannelPresentableModel)
    fun appendFeeds (feeds: TreeSet<FeedPresentableModel>)
    fun addNewFeeds (feeds: TreeSet<FeedPresentableModel>)
    fun hideRefreshLayout()
    fun clear()
    fun empty()
    fun showFollowLoading ()
    fun dismissFollowLoading()
    fun showFollowButton()
    fun showUnFollowButton()
    fun showMessage(message: String)

}

fun MvcViewFactory.getReadFeedsMvcView (viewGroup: ViewGroup?) : ReadFeedsMvcView = ReadFeedsMvcViewImp(uiToolkit, viewGroup)