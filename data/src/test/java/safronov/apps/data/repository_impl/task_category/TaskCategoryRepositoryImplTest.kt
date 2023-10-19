package safronov.apps.data.repository_impl.task_category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryService
import safronov.apps.data.exception.DataException
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class TaskCategoryRepositoryImplTest {

    private lateinit var newTaskCategories: List<TaskCategory>
    private lateinit var newTaskCategoryForUpdate: TaskCategory

    @Before
    fun setup() {
        newTaskCategories = listOf(
            TaskCategory(
                id = 5,
                icon = 1,
                backgroundColor = 2,
                categoryName = "some name1",
                categoryType = CategoryTypes.System
            )
        )
        newTaskCategoryForUpdate = TaskCategory(
            id = 164564,
            icon = 4,
            backgroundColor = 24534,
            categoryName = "some name3",
            categoryType = CategoryTypes.System
        )
    }

    @Test
    fun `test, insert new categories, should save new categories`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val oldData = TaskCategoryEntity.convertListOfTaskCategoryEntityToListOfTaskCategory(fakeTaskCategoryService.dataToReturn)
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        assertNotEquals(oldData, newTaskCategories)
        taskCategoryRepository.insertTaskCategories(newTaskCategories)
        val savedTaskCategory = TaskCategoryEntity.convertListOfTaskCategoryEntityToListOfTaskCategory(fakeTaskCategoryService.dataToReturn)
        assertEquals(savedTaskCategory, newTaskCategories)
    }

    @Test
    fun `test, insert new categories, should save and return saved categories`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        val oldData = TaskCategoryEntity.convertListOfTaskCategoryEntityToListOfTaskCategory(fakeTaskCategoryService.dataToReturn)
        assertNotEquals(oldData, newTaskCategories)
        taskCategoryRepository.insertTaskCategories(newTaskCategories)
        val savedData = TaskCategoryEntity.convertListOfTaskCategoryEntityToListOfTaskCategory(
            fakeTaskCategoryService.getTaskCategories().first()
        )
        assertEquals(savedData, newTaskCategories)
    }

    @Test
    fun `test, insert new categories, delete them and try to get them, should return empty data`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.insertTaskCategories(newTaskCategories)
        taskCategoryRepository.clearTaskCategories()
        val savedData: List<TaskCategory> = taskCategoryRepository.getTaskCategories().first()
        assertEquals(true, savedData.isEmpty())
    }

    @Test
    fun `test, insert categories, and get item by id, should return item`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.insertTaskCategories(newTaskCategories)
        val savedData: TaskCategory? = taskCategoryRepository.getTaskCategoryById("2")
        assertEquals(newTaskCategories.first(), savedData)
    }

    @Test
    fun `test, update task category, should update task category`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        assertNotEquals(newTaskCategoryForUpdate, taskCategoryRepository.getTaskCategoryById("3"))
        taskCategoryRepository.updateTaskCategory(newTaskCategoryForUpdate)
        val savedTaskCategory = TaskCategoryEntity.convertTaskCategoryEntityToTaskCategory(fakeTaskCategoryService.getTaskCategoryById("3"))
        assertEquals(newTaskCategoryForUpdate, savedTaskCategory)
    }

    @Test
    fun `test, update deleted category`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        assertNotEquals(fakeTaskCategoryService.getTaskCategoryById("3"), newTaskCategoryForUpdate)
        taskCategoryRepository.clearTaskCategories()
        assertEquals(true, fakeTaskCategoryService.dataToReturn.isEmpty())
        taskCategoryRepository.updateTaskCategory(newTaskCategoryForUpdate)
        val savedTaskCategory = TaskCategoryEntity.convertTaskCategoryEntityToTaskCategory(fakeTaskCategoryService.getTaskCategoryById("3"))
        assertEquals(savedTaskCategory, newTaskCategoryForUpdate)
    }

    @Test
    fun `test, insert and update inserted task category and after that get task category by id`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        assertNotEquals(fakeTaskCategoryService.getTaskCategoryById("3"), newTaskCategoryForUpdate)
        taskCategoryRepository.updateTaskCategory(newTaskCategoryForUpdate)
        val savedTaskCategory = TaskCategoryEntity.convertTaskCategoryEntityToTaskCategory(fakeTaskCategoryService.getTaskCategoryById("3"))
        assertEquals(savedTaskCategory, taskCategoryRepository.getTaskCategoryById("3"))
        assertEquals(savedTaskCategory, newTaskCategoryForUpdate)
    }

    @Test
    fun `test, insert and clear task categories, should clear task categories`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.insertTaskCategories(newTaskCategories)
        assertEquals(true, taskCategoryRepository.getTaskCategories().first().isNotEmpty())
        taskCategoryRepository.clearTaskCategories()
        assertEquals(false, taskCategoryRepository.getTaskCategories().first().isNotEmpty())
    }

    @Test
    fun `test, clear task categories when db is empty`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        assertEquals(true, taskCategoryRepository.getTaskCategories().first().isNotEmpty())
        taskCategoryRepository.clearTaskCategories()
        taskCategoryRepository.clearTaskCategories()
        assertEquals(false, taskCategoryRepository.getTaskCategories().first().isNotEmpty())
    }

    @Test(expected = DomainException::class)
    fun `test, insert new categories, should throw domain exception`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        fakeTaskCategoryService.isNeedToThrowException = true
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.insertTaskCategories(newTaskCategories)
    }

    @Test(expected = DomainException::class)
    fun `test, get categories, should throw domain exception`(): Unit = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        fakeTaskCategoryService.isNeedToThrowException = true
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.getTaskCategories()
    }

    @Test(expected = DomainException::class)
    fun `test, get category by id, should throw domain exception`(): Unit = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        fakeTaskCategoryService.isNeedToThrowException = true
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.getTaskCategoryById("3")
    }

    @Test(expected = DomainException::class)
    fun `test, update category, should throw exception`(): Unit = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        fakeTaskCategoryService.isNeedToThrowException = true
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.updateTaskCategory(newTaskCategoryForUpdate)
    }

    @Test(expected = DomainException::class)
    fun `test, clear task categories, should throw exception`() = runBlocking {
        val fakeTaskCategoryService = FakeTaskCategoryService()
        fakeTaskCategoryService.isNeedToThrowException = true
        val taskCategoryRepository: TaskCategoryRepository = TaskCategoryRepositoryImpl(taskCategoryService = fakeTaskCategoryService)
        taskCategoryRepository.clearTaskCategories()
    }

}

private class FakeTaskCategoryService: TaskCategoryService {

    var isNeedToThrowException = false
    var dataToReturn = mutableListOf(
        TaskCategoryEntity(
            id = 4,
            icon = 3,
            backgroundColor = 4,
            categoryName = "some name",
            categoryType = CategoryTypes.System
        )
    )

    override suspend fun insertTaskCategories(list: List<TaskCategoryEntity>) {
        if (isNeedToThrowException) throw DataException("some exception")
        dataToReturn = list.toMutableList()
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategoryEntity>> {
        if (isNeedToThrowException) throw DataException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategoryEntity {
        if (isNeedToThrowException) throw DataException("some exception")
        return dataToReturn.first()
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategoryEntity) {
        if (isNeedToThrowException) throw DataException("some exception")
        dataToReturn.clear()
        dataToReturn.add(taskCategory)
    }

    override suspend fun clearTaskCategories() {
        if (isNeedToThrowException) throw DataException("some exception")
        dataToReturn.clear()
    }

}