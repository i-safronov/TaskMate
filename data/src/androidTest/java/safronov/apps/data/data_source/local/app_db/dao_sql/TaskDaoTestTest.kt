package safronov.apps.data.data_source.local.app_db.dao_sql

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import safronov.apps.data.data_source.local.app_db.core_sql.AppDB
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.domain.model.task.Task

@RunWith(AndroidJUnit4::class)
class TaskDaoTestTest {

    private lateinit var taskCategoryDao: TaskDao
    private lateinit var db: AppDB
    private lateinit var dataToSave: List<TaskEntity>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDB::class.java).build()
        taskCategoryDao = db.getTaskDao()
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

    }

    @Test
    fun test_getTasksAsFlow() {

    }

    @Test
    fun test_getTasks() {

    }

    @Test
    fun test_getTasksByText() {

    }

    @Test
    fun test_changeTask() {

    }

    @Test
    fun test_changeTasks() {

    }

    @Test
    fun test_deleteTask() {

    }

    @Test
    fun test_deleteTasks() {

    }

}