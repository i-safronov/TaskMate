package safronov.apps.domain.use_case.task.create

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class InsertTaskListUseCase(
    private val insertingTask: TaskRepository.InsertingTask
) {

    suspend fun execute(task: Task.TaskList): Long? {
        return insertingTask.insertTaskList(task = task)
    }

}
