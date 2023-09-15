package com.jacob.wakatimeapp.home.ui.extract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class ExtractUseDataViewModel @Inject constructor() : ViewModel() {
    private val _extractPageState =
        MutableStateFlow<ExtractPageViewState>(ExtractPageViewState.Idle)
    val extractPageState = _extractPageState.asStateFlow()

    fun createExtract() {
        viewModelScope.launch {
            var progress = 0f
            while (true) {
                progress += 0.2f
                _extractPageState.value = ExtractPageViewState.CreatingExtract(progress)

                if (progress >= 1f) {
                    delay(1000)
                    _extractPageState.value = ExtractPageViewState.ExtractCreated
                    break
                }

                delay(1000)
            }
        }
    }

    fun downloadExtract() {
        TODO("Not yet implemented")
    }
}
