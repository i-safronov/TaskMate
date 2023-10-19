package safronov.apps.domain.repository.user_login

interface UserLoginRepository {

    suspend fun userLogIn(): Boolean
    suspend fun isUserLoggedIn(): Boolean

}