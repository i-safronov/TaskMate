package safronov.apps.domain.use_case.task_category.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class GetTaskCategoriesUseCaseTest {

    @Test
    fun `test, execute, should return categories`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val getTaskCategoriesUseCase = GetTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val list: Flow<List<TaskCategory>> = getTaskCategoriesUseCase.execute()
        val result: List<TaskCategory> = list.first()
        assertEquals(fakeTaskCategoryRepository.dataToReturn, result)
        assertEquals(true, result.isNotEmpty())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeTaskCategoryRepository.isNeedToThrowException = true
        val getTaskCategoriesUseCase = GetTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val list: Flow<List<TaskCategory>> = getTaskCategoriesUseCase.execute()
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

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        return dataToReturn.first()
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