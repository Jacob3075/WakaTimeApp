package com.jacob.wakatimeapp.home.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jacob.wakatimeapp.common.data.OfflineDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    application: Application,
    offlineDataStore: OfflineDataStore,
) : AndroidViewModel(application) {
    @ExperimentalCoroutinesApi
    val userDetails = offlineDataStore.getUserDetails(getApplication())

}
