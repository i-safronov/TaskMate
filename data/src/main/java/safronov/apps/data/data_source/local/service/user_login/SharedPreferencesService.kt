package safronov.apps.data.data_source.local.service.user_login

interface SharedPreferencesService {

    suspend fun saveBoolean(key: String, value: Boolean): Boolean
    suspend fun getBoolean(key: String): Boolean
    suspend fun saveString(key: String, value: String)
    suspend fun getString(key: String): String?

}