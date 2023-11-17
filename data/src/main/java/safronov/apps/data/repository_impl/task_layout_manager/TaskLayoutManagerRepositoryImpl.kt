package safronov.apps.data.repository_impl.task_layout_manager

import com.google.gson.Gson
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.data.exception.DataException
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository
import java.lang.Exception

class TaskLayoutManagerRepositoryImpl(
    private val sharedPreferencesService: SharedPreferencesService,
    private val json: Gson
): TaskLayoutManagerRepository.MutableTaskLayoutManagerRepository {

    override suspend fun saveTaskLayoutManager(manager: TaskLayoutManager) {
        try {
            sharedPreferencesService.saveString(TASK_LAYOUT_MANAGER_KEY, json.toJson(manager))
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTaskLayoutManager(): TaskLayoutManager? {
        try {
            val result = sharedPreferencesService.getString(TASK_LAYOUT_MANAGER_KEY)
            return json.fromJson(result, TaskLayoutManager::class.java)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    companion object {
        const val TASK_LAYOUT_MANAGER_KEY = "TaskLayoutManagerKey"
    }

}