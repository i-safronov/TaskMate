package safronov.apps.domain.use_case.user_login

import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository

class UserLogInUseCaseTest {

    @Test
    fun `test, execute, should user log in`() {
        val fakeUserLoginRepository = FakeUserLoginRepository1()
        fakeUserLoginRepository.isUserLoggedIn = false
        fakeUserLoginRepository.userLogIn = true
        val userLogInUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val result: Boolean = userLogInUseCase.execute()
        assertEquals(true, result)
    }

    @Test
    fun `test, execute, should user log in, should return false, cause something went wrong`() {
        val fakeUserLoginRepository = FakeUserLoginRepository1()
        fakeUserLoginRepository.isUserLoggedIn = false
        fakeUserLoginRepository.userLogIn = false
        val userLogInUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val result: Boolean = userLogInUseCase.execute()
        assertEquals(true, result)
    }

    @Test
    fun `test, execute when user logged in, should throw exception`() {
        val fakeUserLoginRepository = FakeUserLoginRepository1()
        fakeUserLoginRepository.isUserLoggedIn = true
        fakeUserLoginRepository.isNeedToThrowException = true
        val userLogInUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val result: Boolean = userLogInUseCase.execute()
        assertEquals(true, result)
    }

}

private class FakeUserLoginRepository1: UserLoginRepository {

    var isNeedToThrowException = false
    var isUserLoggedIn = true
    var userLogIn = false

    override suspend fun userLogIn(): Boolean {
        if (isNeedToThrowException) throw DomainException("some exception")
        return userLogIn
    }

    override suspend fun isUserLoggedIn(): Boolean {
        if (isNeedToThrowException) throw DomainException("some exception")
        return isUserLoggedIn
    }

}