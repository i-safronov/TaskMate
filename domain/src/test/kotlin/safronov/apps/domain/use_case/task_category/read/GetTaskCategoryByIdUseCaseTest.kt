package safronov.apps.domain.use_case.task_category.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class GetTaskCategoryByIdUseCaseTest {

    @Test
    fun `test, execute, should return data`() {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository1()
        val getTaskCategoriesUseCase = GetTaskCategoryByIdUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        val result = getTaskCategoriesUseCase.execute(id = "4")
        assertEquals(fakeTaskCategoryRepository.dataToReturn, result)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw exception`() {
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

    override fun insertTaskCategories(list: List<TaskCategory>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = list.toMutableList()
    }

    override fun getTaskCategories(): Flow<List<TaskCategory>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override fun getTaskCategoryById(id: String): TaskCategory? {
        return if (isWannaUpdateSystem) wannaUpdateSystemData else dataToReturn.first()
    }

    override fun updateTaskCategory(taskCategory: TaskCategory) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn[0] = taskCategory
    }

    override fun clearTaskCategories() {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
    }

}