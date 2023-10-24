package safronov.apps.domain.use_case.task.update

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class ChangeTaskTextUseCase(
    private val changingTaskRepository: TaskRepository.ChangingTask
) {

    suspend fun execute(task: Task.TaskText) {
        changingTaskRepository.changeTaskText(task = task)
    }

}
