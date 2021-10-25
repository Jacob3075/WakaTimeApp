package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.jacob.wakatimeapp.common.models.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    private val userSession: UserSession,
) : AndroidViewModel(application) {
    val userDetails get() = userSession.loggedInUser.asLiveData()

}
