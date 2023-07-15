package com.viable.tasklist.presentation.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.viable.tasklist.data.prefs.PreferencesRepository
import com.viable.tasklist.data.prefs.UserSettings
import com.viable.tasklist.utils.ThemePreferences

class PreferencesViewModel(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private var currentTheme = ThemePreferences.SYSTEM
    private val _themeLiveData = MutableLiveData(currentTheme)
    val themeLiveData: LiveData<ThemePreferences> get() = _themeLiveData

    init {
        currentTheme = preferencesRepository.getSettings().currentTheme
        switchTheme(currentTheme)
        _themeLiveData.value = currentTheme
    }

    fun onThemeUpdate(theme: ThemePreferences) {
        _themeLiveData.value = theme
        switchTheme(theme)
        preferencesRepository.updateSettings(UserSettings(theme))
        Log.d("gsonon", "current theme = $theme")
    }

    private fun switchTheme(theme: ThemePreferences) {
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                ThemePreferences.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                ThemePreferences.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                ThemePreferences.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            },
        )
    }
}
