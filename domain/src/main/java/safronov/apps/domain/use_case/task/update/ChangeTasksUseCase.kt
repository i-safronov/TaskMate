package safronov.apps.domain.use_case.task.update

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class ChangeTasksUseCase(
    private val changingTasksUseCase: TaskRepository.ChangingTask
) {

    suspend fun execute(tasks: List<Task>) {
        changingTasksUseCase.changeTasks(tasks = tasks)
    }

}
