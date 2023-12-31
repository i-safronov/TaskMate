package safronov.apps.data.data_source.local.service.user_login

import android.content.SharedPreferences

class SharedPreferencesServiceImpl(
    private val sharedPreferences: SharedPreferences
): SharedPreferencesService {

    companion object {
        const val SHARED_PREFERENCES_NAME = "TaskMateSharedPreferences"
    }

    override suspend fun saveBoolean(key: String, value: Boolean): Boolean {
        sharedPreferences.edit().putBoolean(key, true).apply()
        return true
    }

    override suspend fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override suspend fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override suspend fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

}