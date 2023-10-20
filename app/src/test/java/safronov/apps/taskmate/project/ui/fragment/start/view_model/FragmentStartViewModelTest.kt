package safronov.apps.taskmate.project.ui.fragment.start.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository
import safronov.apps.domain.use_case.user_login.IsUserLoggedInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentStartViewModelTest {

    @Test
    fun `test, check is user logged in, should return true`() = runBlocking {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isUserLoggedIn = true
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentStartVM = FragmentStartViewModel(isUserLoggedInUseCase = isUserLoggedInUseCase, dispatchersList = TestDispatchersList())
        fragmentStartVM.checkIsUserLoggedIn()
        assertEquals(true, fragmentStartVM.isUserLoggedIn().first())
    }

    @Test
    fun `test, check is user logged in, should return false`() = runBlocking {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isUserLoggedIn = false
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentStartVM = FragmentStartViewModel(isUserLoggedInUseCase = isUserLoggedInUseCase, dispatchersList = TestDispatchersList())
        fragmentStartVM.checkIsUserLoggedIn()
        assertEquals(false, fragmentStartVM.isUserLoggedIn().first())
    }

    @Test
    fun `test, check is user logged in, should throw exception`() = runBlocking {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isNeedToThrowException = true
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentStartVM = FragmentStartViewModel(isUserLoggedInUseCase = isUserLoggedInUseCase, dispatchersList = TestDispatchersList())
        fragmentStartVM.checkIsUserLoggedIn()
        assertEquals(true, fragmentStartVM.wasException().first()?.message?.isNotEmpty())
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