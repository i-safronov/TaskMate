package safronov.apps.taskmate.project.ui.fragment.welcome.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentWelcomeViewModelTest {

    @Test
    fun `test, sign request that sign in, should return true`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        fragmentWelcomeViewModel.requestToSignIn()
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == true)
    }

    @Test
    fun `test, sign request that sign in, should return false`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        fragmentWelcomeViewModel.requestToSignIn()
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(false, fragmentWelcomeViewModel.userLoggedIn().value == true)
    }

    @Test
    fun `test, sign request that sign in, handle exception`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isNeedToThrowException = true
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        fragmentWelcomeViewModel.requestToSignIn()
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == false)
    }

}

private class FakeUserLoginRepository: UserLoginRepository {

    var isNeedToThrowException = false
    var userLogin = true

    override suspend fun userLogIn(): Boolean {
        if (isNeedToThrowException) throw DomainException("some exception")
        return userLogin
    }

    override suspend fun isUserLoggedIn(): Boolean {
        if (isNeedToThrowException) throw DomainException("some exception")
        return false
    }

}

private class TestDispatchersList(
    private val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
): DispatchersList {
    override fun io(): CoroutineDispatcher {
        return testCoroutineDispatcher
    }

    override fun ui(): CoroutineDispatcher {
        return testCoroutineDispatcher
    }
}