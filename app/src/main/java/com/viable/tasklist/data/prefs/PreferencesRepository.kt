package com.viable.tasklist.data.prefs

interface PreferencesRepository {

    fun getSettings(): UserSettings

    fun updateSettings(settingsDto: UserSettings)
    class Base(private val dataSource: PreferencesDataSource) :
        PreferencesRepository {

        override fun getSettings(): UserSettings {
            return dataSource.getSettings()
        }

        override fun updateSettings(settings: UserSettings) {
            dataSource.updateSettings(settings)
        }
    }
}
