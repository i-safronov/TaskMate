package safronov.apps.data.data_source.local.service.user_login

import android.content.SharedPreferences

//TODO перепеши на абстракцию а не на суть 

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

}