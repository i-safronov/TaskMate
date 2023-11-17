package safronov.apps.data.repository_impl.task_layout_manager

import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.data.exception.DataException
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository
import java.lang.Exception

class TaskLayoutManagerRepositoryImpl(
    private val sharedPreferencesService: SharedPreferencesService
): TaskLayoutManagerRepository.MutableTaskLayoutManagerRepository {

    override suspend fun saveTaskLayoutManager(manager: TaskLayoutManager) {
        try {
            sharedPreferencesService.saveString(TASK_LAYOUT_MANAGER_KEY, manager.name)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTaskLayoutManager(): String? {
        try {
            return sharedPreferencesService.getString(TASK_LAYOUT_MANAGER_KEY)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    companion object {
        const val TASK_LAYOUT_MANAGER_KEY = "TaskLayoutManagerKey"
    }

}