package safronov.apps.domain.use_case.task_category.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import java.lang.IllegalStateException

class GetTaskCategoryByIdUseCaseTest {

    @Test
    fun `test, execute, should return data`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository1()
        val getTaskCategoriesUseCase = GetTaskCategoryByIdUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val result = getTaskCategoriesUseCase.execute(id = "4")
        assertEquals(fakeTaskCategoryRepository.dataToReturn.first(), result)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw exception`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository1()
        fakeTaskCategoryRepository.isNeedToThrowException = true
        val getTaskCategoriesUseCase = GetTaskCategoryByIdUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val result = getTaskCategoriesUseCase.execute(id = "4")
    }

}

private class FakeTaskCategoryRepository1: TaskCategoryRepository {

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
        if (isNeedToThrowException) throw DomainException("some exception")
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