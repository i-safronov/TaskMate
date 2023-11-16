package safronov.apps.taskmate.project.ui.fragment.welcome.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.repository.user_login.UserLoginRepository
import safronov.apps.domain.use_case.task_category.create.InsertTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import java.lang.IllegalStateException

class FragmentWelcomeViewModelTest {

    private lateinit var newTaskCategoriesToSave: List<TaskCategory>

    @Before
    fun setup() {
        newTaskCategoriesToSave = listOf(
            TaskCategory(
                id = 25,
                icon = 325232,
                backgroundColor = 143463,
                categoryName = "some asda",
                categoryType = CategoryTypes.System
            )
        )
    }

    @Test
    fun `test, sign request that sign in, should return true`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = GetTaskCategoriesUseCase(fakeTaskCategoryRepository)
        )
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        fragmentWelcomeViewModel.requestToLogIn(
            newTaskCategoriesToSave
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == true)
    }

    @Test
    fun `test, sign request that sign in, should return false`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.userLogin = false
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = GetTaskCategoriesUseCase(fakeTaskCategoryRepository)
        )
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        fragmentWelcomeViewModel.requestToLogIn(
            newTaskCategoriesToSave
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(false, fragmentWelcomeViewModel.userLoggedIn().value == true)
    }

    @Test
    fun `test, sign request that sign in, handle exception`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        fakeUserLoginRepository.isNeedToThrowException = true
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = GetTaskCategoriesUseCase(fakeTaskCategoryRepository)
        )
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        fragmentWelcomeViewModel.requestToLogIn(
            newTaskCategoriesToSave
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value != null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == false)
    }

    @Test
    fun `test, sign request, should save new task categories`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = GetTaskCategoriesUseCase(fakeTaskCategoryRepository)
        )
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        assertEquals(true, fakeTaskCategoryRepository.dataToReturn != newTaskCategoriesToSave)
        fragmentWelcomeViewModel.requestToLogIn(
            defaultTaskCategories = newTaskCategoriesToSave
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == true)
        assertEquals(true, fakeTaskCategoryRepository.dataToReturn == newTaskCategoriesToSave)
    }

    @Test
    fun `test, sign request, should throw exception when saving new task categories`() {
        val fakeUserLoginRepository = FakeUserLoginRepository()
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeUserLoginRepository.isNeedToThrowException = true
        fakeTaskCategoryRepository.isNeedToThrowException = true
        val userLoginUseCase = UserLogInUseCase(userLoginRepository = fakeUserLoginRepository)
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = GetTaskCategoriesUseCase(fakeTaskCategoryRepository)
        )
        val fragmentWelcomeViewModel = FragmentWelcomeViewModel(
            dispatchersList = TestDispatchersList(),
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == null)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value == null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == null)
        assertEquals(true, fakeTaskCategoryRepository.dataToReturn != newTaskCategoriesToSave)
        fragmentWelcomeViewModel.requestToLogIn(
            defaultTaskCategories = newTaskCategoriesToSave
        )
        assertEquals(true, fragmentWelcomeViewModel.isLoading().value == false)
        assertEquals(true, fragmentWelcomeViewModel.wasException().value != null)
        assertEquals(true, fragmentWelcomeViewModel.userLoggedIn().value == false)
        assertEquals(true, fakeTaskCategoryRepository.dataToReturn != newTaskCategoriesToSave)
    }

}

private class FakeTaskCategoryRepository: TaskCategoryRepository {

    var isNeedToThrowException = false
    var dataToReturn = mutableListOf(
        TaskCategory(
            id = 4,
            icon = 3,
            backgroundColor = 4,
            categoryName = "some name",
            categoryType = CategoryTypes.System
        )
    )

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = list.toMutableList()
    }

    override suspend fun updateTaskCategories(categories: List<TaskCategory>) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        if (isNeedToThrowException) throw DomainException("some exception")
        return dataToReturn[0]
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategory) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
        dataToReturn.add(taskCategory)
    }

    override suspend fun clearTaskCategories() {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
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