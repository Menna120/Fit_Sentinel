package com.example.fit_sentinel.ui.main_activity

import androidx.lifecycle.ViewModel
import com.example.fit_sentinel.domain.usecase.shared_preferences.IsFirstLaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val isFirstLaunchUseCase: IsFirstLaunchUseCase
) : ViewModel() {

    fun isFirstLaunch() = isFirstLaunchUseCase.get()
    fun updateFirstLaunchStatus(data: Boolean) = isFirstLaunchUseCase.save(data)
}