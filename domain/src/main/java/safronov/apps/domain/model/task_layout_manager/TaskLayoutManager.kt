package safronov.apps.domain.model.task_layout_manager

sealed interface TaskLayoutManager {

    class GridLayoutManager(
        val name: String = "Grid"
    ): TaskLayoutManager

    class LinearLayoutManager(
        val name: String = "Linear"
    ): TaskLayoutManager

}