package com.devlogs.rssfeed.authentication

import android.content.SharedPreferences
import com.devlogs.rssfeed.common.background_dispatcher.BackgroundDispatcher
import com.devlogs.rssfeed.common.helper.LogTarget
import com.devlogs.rssfeed.common.helper.normalLog
import com.devlogs.rssfeed.common.shared_context.AppConfig
import com.devlogs.rssfeed.domain.entities.UserEntity
import com.devlogs.rssfeed.receive_channel_update.SubscribeFollowedChannelsNotificationUseCaseSync
import com.devlogs.rssfeed.users.AddUserUseCaseSync
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import javax.inject.Inject

class SSOLoginUseCaseSync @Inject constructor(
    private val loginRule: LoginRule,
    private val sharedPreferences: SharedPreferences,
    private val addUserUseCaseSync: AddUserUseCaseSync,
    private val subscribeFollowedChannelsNotificationUseCaseSync: SubscribeFollowedChannelsNotificationUseCaseSync
): LogTarget {

    sealed class Result {
        data class Success (val user: UserEntity) : Result ()
        data class GeneralError (val message: String?) : Result ()
    }

    suspend fun executes (email: String, name: String, avatarUrl: String?) : Result = withContext(BackgroundDispatcher){
        normalLog("Start login usecase")
        val addUserResult = addUserUseCaseSync.executes(email, name, avatarUrl)
        normalLog("Start login usecase 2")
        if (addUserResult is AddUserUseCaseSync.Result.Success) {
            sharedPreferences.edit()
                .putString(AppConfig.SharedPreferencesKey.USER_EMAIL, email)
                .putString(AppConfig.SharedPreferencesKey.USER_NAME, name)
                .putString(AppConfig.SharedPreferencesKey.USER_AVATAR, avatarUrl)
                .putLong(AppConfig.SharedPreferencesKey.LOGIN_EXPIRED_TIME, System.currentTimeMillis() + loginRule.getValidTime())
                .apply()
            normalLog("Login subscribe to channel")
            subscribeFollowedChannelsNotificationUseCaseSync.executes().let { result ->
                if (result is SubscribeFollowedChannelsNotificationUseCaseSync.Result.Success) {
                    normalLog("Subscribe to channel success")
                    return@withContext Result.Success(addUserResult.addedUser)
                } else {
                    normalLog("Subscribe to channel failed")
                    return@withContext Result.GeneralError("Subscribe to followed channel failed")
                }
            }
        } else if (addUserResult is AddUserUseCaseSync.Result.GeneralError) {
            normalLog("Login usecase failed due to: ${addUserResult.javaClass.name}")
            return@withContext Result.GeneralError(addUserResult.errorMessage)
        }

        throw RuntimeException("UnHandle AddUserUseCaseSync.Result")
    }

}