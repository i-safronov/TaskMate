package safronov.apps.data.data_source.local.app_db.dao_sql

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import safronov.apps.data.data_source.local.app_db.core_sql.AppDB
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.domain.model.task.Task

//TODO check these unit tests
@RunWith(AndroidJUnit4::class)
class TaskDaoTestTest {

    private lateinit var taskDao: TaskDao
    private lateinit var db: AppDB
    private lateinit var dataToSave: List<TaskEntity>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDB::class.java).build()

        taskDao = db.getTaskDao()
        dataToSave = listOf(
            TaskEntity(
                title = "title",
                content = "context",
                date = "date",
                taskCategoryId = 435,
                taskType = Task.TaskType.Text,
                isPinned = false,
                id = null
            )
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun test_insertTask() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        assertEquals(true, item == taskDao.getTasks().first())
    }

    @Test
    fun test_getTasksAsFlow() = runBlocking {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val saved = taskDao.getTasksAsFlow().first().first()
        assertEquals(true, item == saved)
    }

    @Test
    fun test_getTasks() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val saved = taskDao.getTasks().first()
        assertEquals(true, item == saved)
    }

    @Test
    fun test_getTasksByText() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val result = taskDao.getTasksByText(text = item.title.toString())
        assertEquals(true, item == result.first())
    }

    @Test
    fun test_changeTask() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val result = taskDao.getTasks()
        assertEquals(true, item == result.first())
        item.content = "asdjfoiasdjf"
        taskDao.changeTask(item)
        val result2 = taskDao.getTasksByText(text = item.title.toString())
        assertEquals(true, item == result2.first())
    }

    @Test
    fun test_changeTasks() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val result = taskDao.getTasks()
        assertEquals(true, item == result.first())
        item.content = "asdjfoiasdjf"
        taskDao.changeTasks(listOf(item))
        val result2 = taskDao.getTasksByText(text = item.title.toString())
        assertEquals(true, item == result2.first())
    }

    @Test
    fun test_deleteTask() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val result = taskDao.getTasks()
        assertEquals(true, item == result.first())
        taskDao.deleteTask(item)
        assertEquals(true, taskDao.getTasks().isEmpty())
    }

    @Test
    fun test_deleteTasks() {
        val item = dataToSave.first()
        val id = taskDao.insertTask(item)
        item.id = id
        val result = taskDao.getTasks()
        assertEquals(true, item == result.first())
        taskDao.deleteTasks(listOf())
        assertEquals(true, taskDao.getTasks().isEmpty())
    }

}