package safronov.apps.domain.use_case.task.create

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class InsertTaskTextUseCase(
    private val insertingTaskRepository: TaskRepository.InsertingTask
) {

    suspend fun execute(task: Task.TaskText): Long? {
        return insertingTaskRepository.insertTaskText(task = task)
    }

}
