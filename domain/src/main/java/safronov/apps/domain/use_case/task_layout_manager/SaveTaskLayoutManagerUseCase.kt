package safronov.apps.domain.use_case.task_layout_manager

import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository

class SaveTaskLayoutManagerUseCase(
    private val savingTaskLayoutManagerRepository: TaskLayoutManagerRepository.SavingTaskLayoutManagerRepository
) {

    suspend fun execute(taskLayoutManager: TaskLayoutManager) {
        savingTaskLayoutManagerRepository.saveTaskLayoutManager(taskLayoutManager)
    }

}