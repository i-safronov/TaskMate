package safronov.apps.domain.use_case.task_category.delete

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

class ClearTaskCategoriesUseCaseTest {

    @Test
    fun `test, execute, should clear all task categories`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val clearTaskCategoriesUseCase = ClearTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        clearTaskCategoriesUseCase.execute()
        assertEquals(true, fakeTaskCategoryRepository.dataToReturn.isEmpty())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`(): Unit = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeTaskCategoryRepository.throwException()
        val clearTaskCategoriesUseCase = ClearTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        clearTaskCategoriesUseCase.execute()
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

    fun throwException() {
        isNeedToThrowException = true
    }

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