package safronov.apps.taskmate.project.ui.fragment.start.view_model

import org.junit.Assert.*
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.repository.user_login.UserLoginRepository
import safronov.apps.domain.use_case.user_login.IsUserLoggedInUseCase

class FragmentStartViewModelTest {

    @Test
    fun `test, check is user logged in, should return true`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isUserLoggedIn = true
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentStartVM = FragmentStartViewModel(isUserLoggedInUseCase = isUserLoggedInUseCase)
        assertEquals(true, fragmentStartVM.isUserLoggedIn().first())
    }

    @Test
    fun `test, check is user logged in, should return false`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isUserLoggedIn = false
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentStartVM = FragmentStartViewModel(isUserLoggedInUseCase = isUserLoggedInUseCase)
        assertEquals(false, fragmentStartVM.isUserLoggedIn().first())
    }

    @Test
    fun `test, check is user logged in, should throw exception`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isNeedToThrowException = true
        val isUserLoggedInUseCase = IsUserLoggedInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fragmentStartVM = FragmentStartViewModel(isUserLoggedInUseCase = isUserLoggedInUseCase)
        assertEquals(true, fragmentStartVM.isWasException().first().message())
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