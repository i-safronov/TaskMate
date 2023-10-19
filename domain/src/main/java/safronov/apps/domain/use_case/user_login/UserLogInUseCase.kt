package safronov.apps.domain.use_case.user_login

import safronov.apps.domain.repository.user_login.UserLoginRepository

class UserLogInUseCase(
    private val userLoginRepository: UserLoginRepository
) {

    suspend fun execute(): Boolean {
        return userLoginRepository.userLogIn()
    }

}
