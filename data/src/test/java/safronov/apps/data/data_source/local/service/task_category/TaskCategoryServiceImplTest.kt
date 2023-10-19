package safronov.apps.data.data_source.local.service.task_category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.app_db.dao.TaskCategoryDao
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity
import safronov.apps.data.exception.DataException
import safronov.apps.domain.model.task_category.category_type.CategoryTypes

class TaskCategoryServiceImplTest {

    private lateinit var dataToSave: List<TaskCategoryEntity>

    @Before
    fun setup() {
        dataToSave = mutableListOf(
            TaskCategoryEntity(
                id = 65,
                icon = 3342,
                backgroundColor = 4235324,
                categoryName = "some namefajsl dk",
                categoryType = CategoryTypes.System
            )
        )
    }

    @Test
    fun testInsertTaskCategoriesShouldSaveTaskCategories() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
    }

    @Test
    fun testGetTaskCategories() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
    }

    @Test
    fun testGetTaskCategoryById() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
        val currentItem = dataToSave.first()
        assertEquals(currentItem, taskCategoryServiceImpl.getTaskCategoryById(currentItem.id.toString()))
    }

    @Test
    fun updateTaskCategory() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
        val newData = dataToSave.first().copy(
            categoryName = "some new name",
            backgroundColor = 3452345,
            icon = 4351231
        )
        taskCategoryServiceImpl.updateTaskCategory(newData)
        val savedData = taskCategoryServiceImpl.getTaskCategoryById(newData.id.toString())
        assertEquals(newData, savedData)
    }

    @Test
    fun clearTaskCategories() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
        taskCategoryServiceImpl.clearTaskCategories()
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
    }

    @Test(expected = DataException::class)
    fun testInsertTaskCategoriesShouldSaveTaskCategoriesShouldThrowException() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        fakeTaskCategoryDao.isNeedToThrowException = true
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
    }

    @Test(expected = DataException::class)
    fun testGetTaskCategoriesShouldThrowException() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        fakeTaskCategoryDao.isNeedToThrowException = true
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
    }

    @Test(expected = DataException::class)
    fun testGetTaskCategoryByIdShouldThrowException() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        fakeTaskCategoryDao.isNeedToThrowException = true
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
        val currentItem = dataToSave.first()
        assertEquals(currentItem, taskCategoryServiceImpl.getTaskCategoryById(currentItem.id.toString()))
    }

    @Test(expected = DataException::class)
    fun updateTaskCategoryShouldThrowException() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        fakeTaskCategoryDao.isNeedToThrowException = true
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
        val newData = dataToSave.first().copy(
            categoryName = "some new name",
            backgroundColor = 3452345,
            icon = 4351231
        )
        taskCategoryServiceImpl.updateTaskCategory(newData)
        val savedData = taskCategoryServiceImpl.getTaskCategoryById(newData.id.toString())
        assertEquals(newData, savedData)
    }

    @Test(expected = DataException::class)
    fun clearTaskCategoriesShouldThrowException() = runBlocking {
        val fakeTaskCategoryDao = FakeTaskCategoryDao()
        fakeTaskCategoryDao.isNeedToThrowException = true
        val taskCategoryServiceImpl = TaskCategoryServiceImpl(taskCategoryDao = fakeTaskCategoryDao)
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
        taskCategoryServiceImpl.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryServiceImpl.getTaskCategories().first())
        taskCategoryServiceImpl.clearTaskCategories()
        assertEquals(true, taskCategoryServiceImpl.getTaskCategories().first().isEmpty())
    }

}

private class FakeTaskCategoryDao: TaskCategoryDao {

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

    override fun insertTaskCategories(list: List<TaskCategoryEntity>) {
        if (isNeedToThrowException) throw IllegalArgumentException("some exception ;)")
        dataToReturn = list.toMutableList()
    }

    override fun getTaskCategories(): Flow<List<TaskCategoryEntity>> {
        if (isNeedToThrowException) throw IllegalArgumentException("some exception ;)")
        return flow {
            emit(dataToReturn)
        }
    }

    override fun getTaskCategoryById(id: String): TaskCategoryEntity? {
        if (isNeedToThrowException) throw IllegalArgumentException("some exception ;)")
        return dataToReturn.first()
    }

    override fun updateTaskCategory(taskCategory: TaskCategoryEntity) {
        if (isNeedToThrowException) throw IllegalArgumentException("some exception ;)")
        dataToReturn.clear()
        dataToReturn.add(taskCategory)
    }

    override fun clearTaskCategories() {
        if (isNeedToThrowException) throw IllegalArgumentException("some exception ;)")
        dataToReturn.clear()
    }

}