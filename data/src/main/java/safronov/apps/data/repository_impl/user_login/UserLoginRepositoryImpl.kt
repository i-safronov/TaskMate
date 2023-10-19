package safronov.apps.data.repository_impl.user_login

import safronov.apps.data.data_source.local.service.user_login.UserLoginService
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository

class UserLoginRepositoryImpl(
    private val userLoginService: UserLoginService
): UserLoginRepository {

    override suspend fun userLogIn(): Boolean {
        try {
            return userLoginService.userLogIn()
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        try {
            return userLoginService.isUserLoggedIn()
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

}
