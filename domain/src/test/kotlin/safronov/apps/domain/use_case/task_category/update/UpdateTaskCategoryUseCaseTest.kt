package safronov.apps.domain.use_case.task_category.update

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import java.lang.IllegalStateException

class UpdateTaskCategoryUseCaseTest {

    @Test
    fun `test, execute, should update existing task category and return task categories`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val getTaskCategoriesUseCase = GetTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val updateTaskCategoryUseCase = UpdateTaskCategoryUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = getTaskCategoriesUseCase,
            getTaskCategoryByIdUseCase = GetTaskCategoryByIdUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        )
        val newTaskCategory = TaskCategory(
            id = 4,
            icon = 2,
            backgroundColor = 1,
            categoryName = "some name1",
            categoryType = CategoryTypes.User
        )
        assertNotEquals(fakeTaskCategoryRepository.dataToReturn, newTaskCategory)
        val list: List<TaskCategory> = updateTaskCategoryUseCase.execute(taskCategory = newTaskCategory)
        assertEquals(fakeTaskCategoryRepository.dataToReturn.first(), newTaskCategory)
        assertEquals(fakeTaskCategoryRepository.dataToReturn, list)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, try to update task category with category type System, should throw domain exception`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeTaskCategoryRepository.isWannaUpdateSystem = true
        val getTaskCategoriesUseCase = GetTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val updateTaskCategoryUseCase = UpdateTaskCategoryUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = getTaskCategoriesUseCase,
            getTaskCategoryByIdUseCase = GetTaskCategoryByIdUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        )
        val newTaskCategory = TaskCategory(
            id = 4,
            icon = 2,
            backgroundColor = 1,
            categoryName = "some name1",
            categoryType = CategoryTypes.System
        )
        assertNotEquals(fakeTaskCategoryRepository.dataToReturn, newTaskCategory)
        val list: List<TaskCategory> = updateTaskCategoryUseCase.execute(taskCategory = newTaskCategory)
        assertEquals(fakeTaskCategoryRepository.dataToReturn, newTaskCategory)
        assertEquals(fakeTaskCategoryRepository.dataToReturn, list)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeTaskCategoryRepository.isNeedToThrowException = true
        val getTaskCategoriesUseCase = GetTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val updateTaskCategoryUseCase = UpdateTaskCategoryUseCase(
            taskCategoryRepository = fakeTaskCategoryRepository,
            getTaskCategoriesUseCase = getTaskCategoriesUseCase,
            getTaskCategoryByIdUseCase = GetTaskCategoryByIdUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        )
        val newTaskCategory = TaskCategory(
            id = 4,
            icon = 2,
            backgroundColor = 1,
            categoryName = "some name1",
            categoryType = CategoryTypes.User
        )
        val list: List<TaskCategory> = updateTaskCategoryUseCase.execute(taskCategory = newTaskCategory)
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
            categoryType = CategoryTypes.User
        )
    )
    var isWannaUpdateSystem = false
    private val wannaUpdateSystemData = TaskCategory(
        id = 4,
        icon = 3,
        backgroundColor = 4,
        categoryName = "some name",
        categoryType = CategoryTypes.System
    )

    fun throwException() {
        isNeedToThrowException = true
    }

    override suspend fun updateTaskCategories(categories: List<TaskCategory>) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = list.toMutableList()
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        return if (isWannaUpdateSystem) wannaUpdateSystemData else dataToReturn.first()
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategory) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn[0] = taskCategory
    }

    override suspend fun clearTaskCategories() {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
    }

}