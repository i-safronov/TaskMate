package safronov.apps.domain.use_case.task_category.create

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class InsertTaskCategoriesUseCaseTest {

    @Test
    fun `test, insert task categories, should save and return saved categories`() {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
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
    fun `test, insert task categories, should throw exception`() {
        val fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        val insertTaskCategoriesUseCase = InsertTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
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

class FakeTaskCategoryRepository: TaskCategoryRepository {

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

    override fun updateTaskCategory(taskCategory: TaskCategory) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn[0] = taskCategory
    }

    override fun clearTaskCategories() {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
    }

}