package safronov.apps.data.data_source.local.model.converter.task

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.domain.model.task.Task

class TaskEntityConverterTest {

    private lateinit var taskEntityText: TaskEntity
    private lateinit var taskEntityList: TaskEntity
    private lateinit var taskText: Task.TaskText
    private lateinit var taskList: Task.TaskList
    private lateinit var listOfTaskEntityText: List<TaskEntity>
    private lateinit var listOfTaskEntityList: List<TaskEntity>
    private lateinit var listOfTaskText: List<Task.TaskText>
    private lateinit var listOfTaskList: List<Task.TaskList>
    private lateinit var taskEntityConverter: TaskEntityConverter

    @Before
    fun setUp() {
        taskEntityConverter = TaskEntityConverterImpl(gson = Gson())
        taskEntityText = TaskEntity(
            title = "something",
            content = "context",
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        taskEntityList = TaskEntity(
            title = "something",
            content = Gson().toJson(listOf<Task.TaskListItem>(
                Task.TaskListItem(
                    title = "some title",
                    isChecked = true
                )
            )),
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        taskText = Task.TaskText(
            title = "something",
            text = "context",
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        taskList = Task.TaskList(
            title = "something",
            list = listOf<Task.TaskListItem>(
                Task.TaskListItem(
                    title = "some title",
                    isChecked = true
                )
            ),
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        listOfTaskEntityText = listOf(taskEntityText)
        listOfTaskEntityList = listOf(taskEntityList)
        listOfTaskText = listOf(taskText)
        listOfTaskList = listOf(taskList)
    }

    @Test
    fun test_convertTaskEntityToTaskText() {
        val result: Task.TaskText = taskEntityConverter.convertTaskEntityToTaskText(taskEntity = taskEntityText)
        assertEquals(true, result.title == taskEntityText.title)
        assertEquals(true, result.text == taskEntityText.content)
        assertEquals(true, result.date == taskEntityText.date)
        assertEquals(true, result.taskCategoryId == taskEntityText.taskCategoryId)
        assertEquals(true, result.taskType == taskEntityText.taskType)
        assertEquals(true, result.isPinned == taskEntityText.isPinned)
        assertEquals(true, result.id == taskEntityText.id)
    }

    @Test
    fun test_convertTaskEntityToTaskList() {
        val result: Task.TaskList = taskEntityConverter.convertTaskEntityToTaskList(taskEntity = taskEntityList)
        assertEquals(true, result.title == taskEntityList.title)
        assertEquals(true, result.list?.isNotEmpty())
        assertEquals(true, result.list?.get(0)?.title == "some title")
        assertEquals(true, result.list?.get(0)?.isChecked == true)
        assertEquals(true, result.date == taskEntityList.date)
        assertEquals(true, result.taskCategoryId == taskEntityList.taskCategoryId)
        assertEquals(true, result.taskType == taskEntityList.taskType)
        assertEquals(true, result.isPinned == taskEntityList.isPinned)
        assertEquals(true, result.id == taskEntityList.id)
    }

    @Test
    fun test_convertTaskTextToTaskEntity() {
        val result: TaskEntity = taskEntityConverter.convertTaskTextToTaskEntity(task = taskText)
        assertEquals(true, result.title == taskText.title)
        assertEquals(true, result.content == taskText.text)
        assertEquals(true, result.date == taskText.date)
        assertEquals(true, result.taskCategoryId == taskText.taskCategoryId)
        assertEquals(true, result.taskType == taskText.taskType)
        assertEquals(true, result.isPinned == taskText.isPinned)
        assertEquals(true, result.id == taskText.id)
    }

    @Test
    fun test_convertTaskListToTaskEntity() {
        val result: TaskEntity = taskEntityConverter.convertTaskListToTaskEntity(task = taskList)
        assertEquals(true, result.title == taskList.title)
        val list = Gson().fromJson(result.content, Array<Task.TaskListItem>::class.java).asList()
        assertEquals(true, list == taskList.list)
        assertEquals(true, result.date == taskList.date)
        assertEquals(true, result.taskCategoryId == taskList.taskCategoryId)
        assertEquals(true, result.taskType == taskList.taskType)
        assertEquals(true, result.isPinned == taskList.isPinned)
        assertEquals(true, result.id == taskList.id)
    }

    @Test
    fun test_convertListOfTaskEntityToListOfTaskList() {
        val result: Task.TaskList = (taskEntityConverter.convertListOfTaskEntityToListOfTaskList(list = listOfTaskEntityList).first())
        assertEquals(true, result.title == taskEntityList.title)
        assertEquals(true, result.list?.isNotEmpty())
        assertEquals(true, result.list?.get(0)?.title == "some title")
        assertEquals(true, result.list?.get(0)?.isChecked == true)
        assertEquals(true, result.date == taskEntityList.date)
        assertEquals(true, result.taskCategoryId == taskEntityList.taskCategoryId)
        assertEquals(true, result.taskType == taskEntityList.taskType)
        assertEquals(true, result.isPinned == taskEntityList.isPinned)
        assertEquals(true, result.id == taskEntityList.id)
    }

    @Test
    fun test_convertListOfTaskEntityToListOfTaskText() {
        val result: Task.TaskText = (taskEntityConverter.convertListOfTaskEntityToListOfTaskText(list = listOfTaskEntityText).first())
        assertEquals(true, result.title == taskEntityText.title)
        println("1: ${result.text}, 2: ${taskEntityText.content}")
        assertEquals(true, result.text == taskEntityText.content)
        assertEquals(true, result.date == taskEntityText.date)
        assertEquals(true, result.taskCategoryId == taskEntityText.taskCategoryId)
        assertEquals(true, result.taskType == taskEntityText.taskType)
        assertEquals(true, result.isPinned == taskEntityText.isPinned)
        assertEquals(true, result.id == taskEntityText.id)
    }

    @Test
    fun test_convertListOfTaskTextToListOfTaskEntity() {
        val result: TaskEntity = (taskEntityConverter.convertListOfTaskTextToListOfTaskEntity(listOfTaskText).first())
        assertEquals(true, result.title == taskText.title)
        assertEquals(true, result.content == taskText.text)
        assertEquals(true, result.date == taskText.date)
        assertEquals(true, result.taskCategoryId == taskText.taskCategoryId)
        assertEquals(true, result.taskType == taskText.taskType)
        assertEquals(true, result.isPinned == taskText.isPinned)
        assertEquals(true, result.id == taskText.id)
    }

    @Test
    fun test_convertListOfTaskListToListOfTaskEntity() {
        val result: TaskEntity = (taskEntityConverter.convertListOfTaskListToListOfTaskEntity(list = listOfTaskList).first())
        assertEquals(true, result.title == taskList.title)
        val list = Gson().fromJson(result.content, Array<Task.TaskListItem>::class.java).asList()
        assertEquals(true, list == taskList.list)
        assertEquals(true, result.date == taskList.date)
        assertEquals(true, result.taskCategoryId == taskList.taskCategoryId)
        assertEquals(true, result.taskType == taskList.taskType)
        assertEquals(true, result.isPinned == taskList.isPinned)
        assertEquals(true, result.id == taskList.id)
    }

    @Test
    fun test_convertListOfTasksToListOfTaskEntityList() {
        val result: TaskEntity = (taskEntityConverter.convertListOfTaskToListOfTaskEntity(list = listOfTaskList).first())
        assertEquals(true, result.title == taskList.title)
        val list = Gson().fromJson(result.content, Array<Task.TaskListItem>::class.java).asList()
        assertEquals(true, list == taskList.list)
        assertEquals(true, result.date == taskList.date)
        assertEquals(true, result.taskCategoryId == taskList.taskCategoryId)
        assertEquals(true, result.taskType == taskList.taskType)
        assertEquals(true, result.isPinned == taskList.isPinned)
        assertEquals(true, result.id == taskList.id)
    }

    @Test
    fun test_convertListOfTasksToListOfTaskEntityText() {
        val result: TaskEntity = (taskEntityConverter.convertListOfTaskToListOfTaskEntity(list = listOfTaskText).first())
        assertEquals(true, result.title == taskText.title)
        assertEquals(true, result.content == taskText.text)
        assertEquals(true, result.date == taskText.date)
        assertEquals(true, result.taskCategoryId == taskText.taskCategoryId)
        assertEquals(true, result.taskType == taskText.taskType)
        assertEquals(true, result.isPinned == taskText.isPinned)
        assertEquals(true, result.id == taskText.id)
    }

}