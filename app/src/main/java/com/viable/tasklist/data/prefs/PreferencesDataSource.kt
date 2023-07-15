package com.viable.tasklist.data.prefs

import android.content.SharedPreferences
import com.viable.tasklist.utils.ThemePreferences

interface PreferencesDataSource {
    fun getSettings(): UserSettings

    fun updateSettings(settingsDto: UserSettings)
    class DefaultPreferencesDataSource(private val sharedPreferences: SharedPreferences) :
        PreferencesDataSource {

        override fun getSettings(): UserSettings {
            return UserSettings(
                currentTheme = ThemePreferences.valueOf(
                    sharedPreferences.getString(
                        THEME_KEY,
                        ThemePreferences.SYSTEM.name,
                    )!!,
                ),
            )
        }

        override fun updateSettings(settings: UserSettings) {
            sharedPreferences
                .edit()
                .putString(THEME_KEY, settings.currentTheme.name)
                .apply()
        }

        companion object {
            const val THEME_KEY = "theme_preferences"
        }
    }
}
