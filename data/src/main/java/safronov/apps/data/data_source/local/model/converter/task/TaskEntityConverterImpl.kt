package safronov.apps.data.data_source.local.model.converter.task

import com.google.gson.Gson
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.domain.model.task.Task
import java.lang.IllegalStateException

class TaskEntityConverterImpl(
    private val gson: Gson
): TaskEntityConverter {

    override fun convertTaskEntityToTaskList(taskEntity: TaskEntity): Task.TaskList {
        return Task.TaskList(
            title = taskEntity.title,
            list = gson.fromJson(taskEntity.content, Array<Task.TaskListItem>::class.java).asList(),
            date = taskEntity.date,
            taskCategoryId = taskEntity.taskCategoryId,
            taskType = taskEntity.taskType,
            isPinned = taskEntity.isPinned,
            id = taskEntity.id
        )
    }

    override fun convertTaskEntityToTaskText(taskEntity: TaskEntity): Task.TaskText {
        return Task.TaskText(
            title = taskEntity.title,
            text = taskEntity.content,
            date = taskEntity.date,
            taskCategoryId = taskEntity.taskCategoryId,
            taskType = taskEntity.taskType,
            isPinned = taskEntity.isPinned,
            id = taskEntity.id
        )
    }

    override fun convertTaskTextToTaskEntity(task: Task.TaskText): TaskEntity {
        return TaskEntity(
            title = task.title,
            content = task.text,
            date = task.date,
            taskCategoryId = task.taskCategoryId,
            taskType = task.taskType,
            isPinned = task.isPinned,
            id = task.id
        )
    }

    override fun convertTaskListToTaskEntity(task: Task.TaskList): TaskEntity {
        return TaskEntity(
            title = task.title,
            content = gson.toJson(task.list),
            date = task.date,
            taskCategoryId = task.taskCategoryId,
            taskType = task.taskType,
            isPinned = task.isPinned,
            id = task.id
        )
    }

    override fun convertListOfTaskEntityToListOfTaskList(list: List<TaskEntity>): List<Task.TaskList> {
        val mList = mutableListOf<Task.TaskList>()
        list.forEach {
            mList.add(convertTaskEntityToTaskList(it))
        }
        return mList
    }

    override fun convertListOfTaskEntityToListOfTaskText(list: List<TaskEntity>): List<Task.TaskText> {
        val mList = mutableListOf<Task.TaskText>()
        list.forEach {
            mList.add(convertTaskEntityToTaskText(it))
        }
        return mList
    }

    override fun convertListOfTaskTextToListOfTaskEntity(list: List<Task.TaskText>): List<TaskEntity> {
        val mList = mutableListOf<TaskEntity>()
        list.forEach {
            mList.add(convertTaskTextToTaskEntity(it))
        }
        return mList
    }

    override fun convertListOfTaskListToListOfTaskEntity(list: List<Task.TaskList>): List<TaskEntity> {
        val mList = mutableListOf<TaskEntity>()
        list.forEach {
            mList.add(convertTaskListToTaskEntity(it))
        }
        return mList
    }

    override fun convertListOfTaskToListOfTaskEntity(list: List<Task>): List<TaskEntity> {
        val mList = mutableListOf<TaskEntity>()
        list.forEach {
            if (it is Task.TaskText) {
                mList.add(convertTaskTextToTaskEntity(it))
            } else if (it is Task.TaskList) {
                mList.add(convertTaskListToTaskEntity(it))
            } else {
                throw IllegalStateException("didn't found this type of task")
            }
        }
        return mList
    }

}
