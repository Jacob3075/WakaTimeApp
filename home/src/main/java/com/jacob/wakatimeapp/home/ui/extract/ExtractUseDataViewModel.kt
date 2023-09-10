package com.jacob.wakatimeapp.home.ui.extract

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
internal class ExtractUseDataViewModel @Inject constructor() : ViewModel() {
    private val _extractPageState =
        MutableStateFlow<ExtractPageViewState>(ExtractPageViewState.Idle)
    val extractPageState = _extractPageState.asStateFlow()
    fun asfca() {
    }

    fun createExtract() {
        _extractPageState.value = ExtractPageViewState.CreatingExtract(0.1f)
    }
}
