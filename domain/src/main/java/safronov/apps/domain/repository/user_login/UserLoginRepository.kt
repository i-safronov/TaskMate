package safronov.apps.domain.repository.user_login

interface UserLoginRepository {

    suspend fun logIn(): Boolean
    suspend fun isUserLoggedIn(): Boolean

}