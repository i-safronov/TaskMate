package safronov.apps.domain.use_case.task_layout_manager

import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository

class GetTaskLayoutManagerUseCase(
    private val gettingTaskLayoutManager: TaskLayoutManagerRepository.GettingTaskLayoutManager
) {

    suspend fun execute(): TaskLayoutManager {
        val result: String? = gettingTaskLayoutManager.getTaskLayoutManager()
        return if (result == TaskLayoutManager.GridLayoutManager().name) {
            TaskLayoutManager.GridLayoutManager()
        } else if (result == TaskLayoutManager.LinearLayoutManager().name) {
            TaskLayoutManager.LinearLayoutManager()
        } else throw DomainException("couldn't found task layout manager")
    }

}