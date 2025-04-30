package com.example.fit_sentinel.domain.usecase.shared_preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.fit_sentinel.domain.repository.SharedPreferencesRepo
import com.example.fit_sentinel.utils.Constants.Companion.ONBOARDING_COMPLETED
import javax.inject.Inject

class IsOnboardingCompletedUseCase @Inject constructor(
    private val sharedPref: SharedPreferences
) : SharedPreferencesRepo<Boolean> {
    override fun save(data: Boolean) = sharedPref.edit { putBoolean(ONBOARDING_COMPLETED, data) }

    override fun get(): Boolean = sharedPref.getBoolean(ONBOARDING_COMPLETED, false)

    override fun delete() = sharedPref.edit { remove(ONBOARDING_COMPLETED) }

}