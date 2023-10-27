package safronov.apps.domain.use_case.task.read

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class GetTasksByTextUseCase(
    private val gettingTasksByParametersRepository: TaskRepository.GettingTaskByParameters
) {

    suspend fun execute(text: String): List<Task> {
        return gettingTasksByParametersRepository.getTasksByText(text = text)
    }

}
