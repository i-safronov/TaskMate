package safronov.apps.domain.use_case.user_login

import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository

class IsUserLoggedInUseCaseTest {

    @Test
    fun `test, execute, should check is user logged in, and return true`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isUserLoggedIn = true
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val result: Boolean = isUserLoggedInUseCase.execute()
        assertEquals(true, result)
    }

    @Test
    fun `test, execute, should check is user logged in, and return false`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isUserLoggedIn = false
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val result: Boolean = isUserLoggedInUseCase.execute()
        assertEquals(false, result)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should check is user logged in, should throw exception`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isNeedToThrowException = true
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val result: Boolean = isUserLoggedInUseCase.execute()
        assertEquals(false, result)
    }

}

private class FakeUserLoginRepository: UserLoginRepository {

    var isNeedToThrowException = false
    var isUserLoggedIn = true

    override suspend fun userLogIn(): Boolean {
        if (isNeedToThrowException) throw DomainException("some exception")
        return true
    }

    override suspend fun isUserLoggedIn(): Boolean {
        if (isNeedToThrowException) throw DomainException("some exception")
        return isUserLoggedIn
    }

}