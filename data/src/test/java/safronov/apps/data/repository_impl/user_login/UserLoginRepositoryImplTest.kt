package safronov.apps.data.repository_impl.user_login

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import safronov.apps.data.data_source.local.service.user_login.UserLoginService
import safronov.apps.data.exception.DataException
import safronov.apps.domain.exception.DomainException

class UserLoginRepositoryImplTest {

    @Test
    fun `test, user log in, should return true`() = runBlocking {
        val fakeUserLoginService = FakeUserLoginService()
        fakeUserLoginService.isUserLoggedIn = false
        fakeUserLoginService.userLogIn = true
        val userLoginRepositoryImpl = UserLoginRepositoryImpl(userLoginService = fakeUserLoginService)
        assertEquals(true, userLoginRepositoryImpl.userLogIn())
    }

    @Test
    fun `test, user log in, should return false`() = runBlocking {
        val fakeUserLoginService = FakeUserLoginService()
        fakeUserLoginService.isUserLoggedIn = true
        fakeUserLoginService.userLogIn = false
        val userLoginRepositoryImpl = UserLoginRepositoryImpl(userLoginService = fakeUserLoginService)
        assertEquals(false, userLoginRepositoryImpl.userLogIn())
    }

    @Test(expected = DomainException::class)
    fun `test, user log in, should throw exception`() = runBlocking {
        val fakeUserLoginService = FakeUserLoginService()
        fakeUserLoginService.isNeedToThrowException = true
        val userLoginRepositoryImpl = UserLoginRepositoryImpl(userLoginService = fakeUserLoginService)
        assertEquals(false, userLoginRepositoryImpl.userLogIn())
    }

}

private class FakeUserLoginService(): UserLoginService {

    var isNeedToThrowException = false
    var isUserLoggedIn = true
    var userLogIn = false

    override suspend fun userLogIn(): Boolean {
        if (isNeedToThrowException) throw DataException("some exception")
        return userLogIn
    }

    override suspend fun isUserLoggedIn(): Boolean {
        if (isNeedToThrowException) throw DataException("some exception")
        return isUserLoggedIn
    }

}