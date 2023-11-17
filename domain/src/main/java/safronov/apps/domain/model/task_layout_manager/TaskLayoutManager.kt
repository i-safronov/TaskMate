package safronov.apps.domain.model.task_layout_manager

sealed class TaskLayoutManager(
    val name: String
) {

    class GridLayoutManager(gridName: String = "Grid"): TaskLayoutManager(gridName)

    class LinearLayoutManager(linearName: String = "Linear"): TaskLayoutManager(linearName)

}