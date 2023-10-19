package safronov.apps.data.data_source.local.service.user_login

import android.content.SharedPreferences

class UserLoginServiceImpl(
    private val sharedPreferences: SharedPreferences
): UserLoginService {

    companion object {
        private const val USER_LOGIN = "User login"
    }

    override suspend fun userLogIn(): Boolean {
        sharedPreferences.edit().putBoolean(USER_LOGIN, true).apply()
        return true
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(USER_LOGIN, false)
    }

}