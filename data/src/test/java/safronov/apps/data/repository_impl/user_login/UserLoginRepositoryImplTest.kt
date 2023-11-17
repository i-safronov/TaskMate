package safronov.apps.data.repository_impl.user_login

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.data.exception.DataException
import safronov.apps.domain.exception.DomainException

class UserLoginRepositoryImplTest {

    @Test
    fun `test, user log in, should return true`() = runBlocking {
        val fakeSharedPreferencesService = FakeSharedPreferencesService()
        fakeSharedPreferencesService.isUserLoggedIn = false
        fakeSharedPreferencesService.userLogIn = true
        val userLoginRepositoryImpl = UserLoginRepositoryImpl(sharedPreferencesService = fakeSharedPreferencesService)
        assertEquals(true, userLoginRepositoryImpl.userLogIn())
    }

    @Test
    fun `test, user log in, should return false`() = runBlocking {
        val fakeSharedPreferencesService = FakeSharedPreferencesService()
        fakeSharedPreferencesService.isUserLoggedIn = true
        fakeSharedPreferencesService.userLogIn = false
        val userLoginRepositoryImpl = UserLoginRepositoryImpl(sharedPreferencesService = fakeSharedPreferencesService)
        assertEquals(false, userLoginRepositoryImpl.userLogIn())
    }

    @Test(expected = DomainException::class)
    fun `test, user log in, should throw exception`() = runBlocking {
        val fakeSharedPreferencesService = FakeSharedPreferencesService()
        fakeSharedPreferencesService.isNeedToThrowException = true
        val userLoginRepositoryImpl = UserLoginRepositoryImpl(sharedPreferencesService = fakeSharedPreferencesService)
        assertEquals(false, userLoginRepositoryImpl.userLogIn())
    }

}

private class FakeSharedPreferencesService(): SharedPreferencesService {

    var isNeedToThrowException = false
    var isUserLoggedIn = true
    var userLogIn = false

    override suspend fun saveBoolean(key: String, value: Boolean): Boolean {
        if (isNeedToThrowException) throw DataException("some exception")
        return userLogIn
    }

    override suspend fun getBoolean(key: String): Boolean {
        if (isNeedToThrowException) throw DataException("some exception")
        return isUserLoggedIn
    }

    override suspend fun saveString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getString(key: String): String? {
        TODO("Not yet implemented")
    }

}