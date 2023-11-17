package safronov.apps.domain.repository.task_layout_manager

import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager

interface TaskLayoutManagerRepository {

    interface SavingTaskLayoutManagerRepository {
        suspend fun saveTaskLayoutManager(manager: TaskLayoutManager)
    }

    interface GettingTaskLayoutManager {
        suspend fun getTaskLayoutManager(): TaskLayoutManager?
    }

    interface MutableTaskLayoutManagerRepository: SavingTaskLayoutManagerRepository, GettingTaskLayoutManager

}