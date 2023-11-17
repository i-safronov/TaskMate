package safronov.apps.data.repository_impl.task_layout_manager

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.data.exception.DataException
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository
import java.lang.IllegalStateException

class TaskLayoutManagerRepositoryImplTest {

    private lateinit var fakeSharedPreferencesService: FakeSharedPreferencesService
    private lateinit var taskLayoutManagerRepositoryImpl: TaskLayoutManagerRepository.MutableTaskLayoutManagerRepository

    @Before
    fun setUp() {
        fakeSharedPreferencesService = FakeSharedPreferencesService()
        taskLayoutManagerRepositoryImpl = TaskLayoutManagerRepositoryImpl(
            fakeSharedPreferencesService
        )
    }

    @Test
    fun saveTaskLayoutManager() = runBlocking {
        val value = TaskLayoutManager.GridLayoutManager("grid")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
        assertEquals(true, value.name == fakeSharedPreferencesService.dataToReturn)
    }

    @Test
    fun saveTaskLayoutManager_linear() = runBlocking {
        val value = TaskLayoutManager.LinearLayoutManager("linear")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
        assertEquals(true, value.name == fakeSharedPreferencesService.dataToReturn)
    }

    @Test
    fun getTaskLayoutManager() = runBlocking {
        val value = TaskLayoutManager.GridLayoutManager("grid")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
        assertEquals(true, value.name == fakeSharedPreferencesService.dataToReturn)
        assertEquals(true, value.name == taskLayoutManagerRepositoryImpl.getTaskLayoutManager())
    }

    @Test
    fun getTaskLayoutManager_linear() = runBlocking {
        val value = TaskLayoutManager.LinearLayoutManager("linear")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
        assertEquals(true, value.name == fakeSharedPreferencesService.dataToReturn)
        assertEquals(true, value.name == taskLayoutManagerRepositoryImpl.getTaskLayoutManager())
    }

    @Test(expected = DataException::class)
    fun saveTaskLayoutManager_expectedException() = runBlocking {
        fakeSharedPreferencesService.exception = true
        val value = TaskLayoutManager.GridLayoutManager("grid")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
    }

    @Test(expected = DataException::class)
    fun saveTaskLayoutManager_linear_expectedException() = runBlocking {
        fakeSharedPreferencesService.exception = true
        val value = TaskLayoutManager.LinearLayoutManager("linear")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
    }

    @Test(expected = DataException::class)
    fun getTaskLayoutManager_expectedException() = runBlocking {
        fakeSharedPreferencesService.exception = true
        val value = TaskLayoutManager.GridLayoutManager("grid")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
    }

    @Test(expected = DataException::class)
    fun getTaskLayoutManager_linear_expectedException() = runBlocking {
        fakeSharedPreferencesService.exception = true
        val value = TaskLayoutManager.LinearLayoutManager("linear")
        taskLayoutManagerRepositoryImpl.saveTaskLayoutManager(manager = value)
    }

}

private class FakeSharedPreferencesService: SharedPreferencesService {

    var dataToReturn: String? = null
    var exception = false
    var requestKey: String? = null

    override suspend fun saveBoolean(key: String, value: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getBoolean(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveString(key: String, value: String) {
        if (exception) throw IllegalStateException("some exception")
        requestKey = key
        dataToReturn = value
    }

    override suspend fun getString(key: String): String? {
        if (exception) throw IllegalStateException("some exception")
        return dataToReturn
    }

}