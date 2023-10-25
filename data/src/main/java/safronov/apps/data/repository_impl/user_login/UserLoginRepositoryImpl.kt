package safronov.apps.data.repository_impl.user_login

import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository

class UserLoginRepositoryImpl(
    private val sharedPreferencesService: SharedPreferencesService
): UserLoginRepository {

    override suspend fun userLogIn(): Boolean {
        try {
            return sharedPreferencesService.saveBoolean(key = USER_LOGIN_KEY, value = true)
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        try {
            return sharedPreferencesService.getBoolean(key = USER_LOGIN_KEY)
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    companion object {
        const val USER_LOGIN_KEY = "UserLoginKey"
    }

}
