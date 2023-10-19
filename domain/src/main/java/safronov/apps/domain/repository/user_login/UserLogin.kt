package safronov.apps.domain.repository.user_login

interface UserLogin {

    suspend fun logIn()
    suspend fun isUserLoggedIn(): Boolean

}