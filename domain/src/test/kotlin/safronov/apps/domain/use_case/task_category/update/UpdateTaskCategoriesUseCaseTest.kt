package safronov.apps.domain.use_case.task_category.update

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import java.lang.IllegalStateException

class UpdateTaskCategoriesUseCaseTest {

    private lateinit var fakeTaskCategoryRepository2: FakeTaskCategoryRepository2
    private lateinit var updateTaskCategoriesUseCase: UpdateTaskCategoriesUseCase

    @Before
    fun setUp() {
        fakeTaskCategoryRepository2 = FakeTaskCategoryRepository2()
        updateTaskCategoriesUseCase = UpdateTaskCategoriesUseCase(
            fakeTaskCategoryRepository2
        )
    }

    @Test
    fun execute() = runBlocking {
        val data = fakeTaskCategoryRepository2.categories
        updateTaskCategoriesUseCase.execute(data)
        assertEquals(true, data == fakeTaskCategoryRepository2.categories)
        assertEquals(true, fakeTaskCategoryRepository2.requests == 1)
    }

    @Test(expected = DomainException::class)
    fun execute_expectedException() = runBlocking {
        fakeTaskCategoryRepository2.exception = true
        updateTaskCategoriesUseCase.execute(fakeTaskCategoryRepository2.categories)
    }

}

private class FakeTaskCategoryRepository2: TaskCategoryRepository {

    var exception = false
    var requests = 0
    var categories = listOf(
        TaskCategory(
            id = 23423,
            icon = 32432,
            backgroundColor = 2342,
            categoryName = "asdf",
            categoryType = CategoryTypes.User
        )
    )

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun updateTaskCategories(categories: List<TaskCategory>) {
        if (exception) throw DomainException("some exception")
        requests++
        this.categories = categories
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategory) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun clearTaskCategories() {
        throw IllegalStateException("don't use this method")
    }

}