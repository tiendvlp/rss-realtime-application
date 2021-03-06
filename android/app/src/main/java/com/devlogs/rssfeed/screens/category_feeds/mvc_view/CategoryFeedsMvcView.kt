package com.devlogs.rssfeed.screens.category_feeds.mvc_view

import android.view.ViewGroup
import com.devlogs.rssfeed.screens.category_feeds.presentable_model.FeedPresentableModel
import com.devlogs.rssfeed.screens.common.mvcview.MvcViewFactory
import com.devlogs.rssfeed.screens.common.mvcview.ObservableMvcView

interface CategoryFeedsMvcView : ObservableMvcView<CategoryFeedsMvcView.Listener> {

    interface Listener {
        fun onFeedSelected(it: FeedPresentableModel)
        fun onBackClicked ()
    }

    fun loading ()
    fun showEmptyText()
    fun setTitle(title: String)
    fun setFeeds (feeds: Set<FeedPresentableModel>)
    fun toast(message: String)
}

fun MvcViewFactory.getCategoryFeedsMvcView (viewGroup: ViewGroup?) : CategoryFeedsMvcView = CategoryFeedsMvcViewImp(uiToolkit, viewGroup)