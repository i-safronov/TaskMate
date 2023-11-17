package safronov.apps.domain.model.task_layout_manager

sealed class TaskLayoutManager(
    val name: String
) {

    class GridLayoutManager(gridName: String): TaskLayoutManager(gridName)

    class LinearLayoutManager(linearName: String): TaskLayoutManager(linearName)

}