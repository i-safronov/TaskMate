package safronov.apps.domain.use_case.user_login.read

import safronov.apps.domain.repository.user_login.UserLoginRepository

class IsUserLoggedInUseCase(
    private val userLoginRepository: UserLoginRepository
) {

    suspend fun execute(): Boolean {
        return userLoginRepository.isUserLoggedIn()
    }

}
