package safronov.apps.domain.use_case.task_category.create

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase

class InsertTaskCategoriesUseCaseTest {

    @Test
    fun `test, insert task categories, should save and return saved categories`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository, getTaskCategoriesUseCase = GetTaskCategoriesUseCase((fakeTaskCategoryRepository)))
        val newData = listOf(
            TaskCategory(
                id = 5,
                icon = 2,
                backgroundColor = 1,
                categoryName = "some name2",
                categoryType = CategoryTypes.User
            )
        )
        val savedData: List<TaskCategory> = insertTaskCategoriesUseCase.execute(list = newData)
        assertEquals(newData, savedData)
        assertEquals(fakeTaskCategoryRepository.dataToReturn, savedData)
    }

    @Test(expected = DomainException::class)
    fun `test, insert task categories, should throw exception`() = runBlocking {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeTaskCategoryRepository.isNeedToThrowException = true
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository, getTaskCategoriesUseCase = GetTaskCategoriesUseCase((fakeTaskCategoryRepository)))
        val newData = listOf(
            TaskCategory(
                id = 5,
                icon = 2,
                backgroundColor = 1,
                categoryName = "some name2",
                categoryType = CategoryTypes.User
            )
        )
        val savedData: List<TaskCategory> = insertTaskCategoriesUseCase.execute(list = newData)
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