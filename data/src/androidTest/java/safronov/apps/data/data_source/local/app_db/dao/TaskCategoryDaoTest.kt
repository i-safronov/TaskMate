package safronov.apps.data.data_source.local.app_db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import safronov.apps.data.data_source.local.app_db.core.AppDB
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskCategoryDaoTest {

    private lateinit var taskCategoryDao: TaskCategoryDao
    private lateinit var db: AppDB
    private lateinit var dataToSave: List<TaskCategoryEntity>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDB::class.java).build()
        taskCategoryDao = db.getTaskCategoryDao()
        dataToSave = listOf(
            TaskCategoryEntity(
                id = 4,
                icon = 3,
                backgroundColor = 4,
                categoryName = "some name",
                categoryType = CategoryTypes.System
            )
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertTaskCategoriesShouldSaveTaskCategories() = runBlocking {
        assertEquals(true, taskCategoryDao.getTaskCategories().first().isEmpty())
        taskCategoryDao.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryDao.getTaskCategories().first())
    }

    @Test
    fun testGetTaskCategories() = runBlocking {
        taskCategoryDao.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryDao.getTaskCategories().first())
    }

    @Test
    fun testGetTaskCategoryById() = runBlocking {
        assertEquals(true, taskCategoryDao.getTaskCategories().first().isEmpty())
        taskCategoryDao.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryDao.getTaskCategories().first())
        val currentItem = dataToSave.first()
        assertEquals(currentItem, taskCategoryDao.getTaskCategoryById(currentItem.id.toString()))
    }

    @Test
    fun updateTaskCategory() = runBlocking {
        assertEquals(true, taskCategoryDao.getTaskCategories().first().isEmpty())
        taskCategoryDao.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryDao.getTaskCategories().first())
        val newData = dataToSave.first().copy(
            categoryName = "some new name",
            backgroundColor = 3452345,
            icon = 4351231
        )
        taskCategoryDao.updateTaskCategory(newData)
        val savedData = taskCategoryDao.getTaskCategoryById(newData.id.toString())
        assertEquals(newData, savedData)
    }

    @Test
    fun clearTaskCategories() = runBlocking {
        assertEquals(true, taskCategoryDao.getTaskCategories().first().isEmpty())
        taskCategoryDao.insertTaskCategories(dataToSave)
        assertEquals(dataToSave, taskCategoryDao.getTaskCategories().first())
        taskCategoryDao.clearTaskCategories()
        assertEquals(true, taskCategoryDao.getTaskCategories().first().isEmpty())
    }

}