package safronov.apps.data.data_source.local.model.converter.task

import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.domain.model.task.Task

interface TaskEntityConverter {

    fun convertTaskEntityToTaskList(taskEntity: TaskEntity): Task.TaskList
    fun convertTaskEntityToTaskText(taskEntity: TaskEntity): Task.TaskText

    fun convertTaskTextToTaskEntity(task: Task.TaskText): TaskEntity
    fun convertTaskListToTaskEntity(task: Task.TaskList): TaskEntity

    fun convertListOfTaskEntityToListOfTaskList(list: List<TaskEntity>): List<Task.TaskList>
    fun convertListOfTaskEntityToListOfTaskText(list: List<TaskEntity>): List<Task.TaskText>

    fun convertListOfTaskTextToListOfTaskEntity(list: List<Task.TaskText>): List<TaskEntity>
    fun convertListOfTaskListToListOfTaskEntity(list: List<Task.TaskList>): List<TaskEntity>
    fun convertListOfTaskToListOfTaskEntity(list: List<Task>): List<TaskEntity>

    fun getTaskEntityByTask(task: Task): TaskEntity
    fun getTaskByTaskEntity(taskEntity: TaskEntity): Task

}