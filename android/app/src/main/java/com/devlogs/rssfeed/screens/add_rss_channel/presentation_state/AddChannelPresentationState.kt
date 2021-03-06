package com.devlogs.rssfeed.screens.add_rss_channel.presentation_state

import com.devlogs.chatty.screen.common.presentationstate.CommonPresentationAction.InitAction
import com.devlogs.chatty.screen.common.presentationstate.CommonPresentationAction.RestoreAction
import com.devlogs.rssfeed.screens.add_rss_channel.presentable_model.RssChannelResultPresentableModel
import com.devlogs.rssfeed.screens.add_rss_channel.presentation_state.AddChannelPresentationAction.*
import com.devlogs.rssfeed.screens.common.presentation_state.CauseAndEffect
import com.devlogs.rssfeed.screens.common.presentation_state.PresentationAction
import com.devlogs.rssfeed.screens.common.presentation_state.PresentationState

sealed class AddChannelPresentationState : PresentationState {

    data class SearchingState (val url : String): PresentationState {
        override val allowSave: Boolean
            get() = false

        override fun consumeAction(
            previousState: PresentationState,
            action: PresentationAction
        ): CauseAndEffect {
            when (action) {
                is SearchFailedAction -> return CauseAndEffect(action, SearchFailedState(action.errorMessage))
                is SearchSuccessAction -> return CauseAndEffect(action, DisplayState(action.channel, false))
            }
            return super.consumeAction(previousState, action)
        }
    }

    data class SearchFailedState (val errorMessage: String) : PresentationState{
        override val allowSave: Boolean
            get() = true

        override fun consumeAction(
            previousState: PresentationState,
            action: PresentationAction
        ): CauseAndEffect {
            when(action) {
                is SearchAction ->return CauseAndEffect(action,SearchingState(action.url))
            }
            return super.consumeAction(previousState, action)
        }
    }

    data class DisplayState (val result: RssChannelResultPresentableModel?, val showTut:Boolean = true) : PresentationState {
        override val allowSave: Boolean
            get() = true

        override fun consumeAction(
            previousState: PresentationState,
            action: PresentationAction
        ): CauseAndEffect {
            when(action) {
                is AddFailedAction -> return CauseAndEffect(action, copy())
                is InitAction -> return CauseAndEffect(action, copy())
                is SearchAction -> return CauseAndEffect(action, SearchingState(action.url))
                is RestoreAction -> return CauseAndEffect(action, copy())
                is AddSuccessAction -> return CauseAndEffect(action, copy(result!!.copy(isAdded = true)))
            }

            return super.consumeAction(previousState, action)
        }
    }
}