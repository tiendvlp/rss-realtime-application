package com.devlogs.rssfeed.screens.read_feeds.presentable_model

import java.io.Serializable

data class FeedPresentableModel
    ( val id: String,
      val rssChannelId: String,
      val channelTitle: String,
      val title: String,
      val pubDate:Long,
      val content: String,
      val pubDateInString: String,
      val url: String,
      val author: String,
      val imageUrl: String?) : Comparable<FeedPresentableModel>, Serializable {



    override fun compareTo(other: FeedPresentableModel): Int {
        if (other.id.equals(id)) {
            return 0
        }

        if (other.pubDate == pubDate) {
            return title.compareTo(other.title)
        }

        if (other.pubDate < pubDate) {
            return -1
        }
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {return false}

        if (!other.javaClass.equals(javaClass)) {
            return false
        }

        return (other as FeedPresentableModel).id.equals(id)
    }

}