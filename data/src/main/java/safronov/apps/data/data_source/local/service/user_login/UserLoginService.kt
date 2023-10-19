package safronov.apps.data.data_source.local.service.user_login

interface UserLoginService {

    suspend fun userLogIn(): Boolean
    suspend fun isUserLoggedIn(): Boolean

}