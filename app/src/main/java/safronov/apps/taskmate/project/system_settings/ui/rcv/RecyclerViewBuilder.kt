package safronov.apps.taskmate.project.system_settings.ui.rcv

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager

interface RecyclerViewBuilder {

    fun <VH: RecyclerView.ViewHolder> setupRcv(
        rcvView: RecyclerView,
        adapter: RecyclerView.Adapter<VH>,
        layoutManager: LayoutManager
    )

    class Base: RecyclerViewBuilder {
        override fun <VH : RecyclerView.ViewHolder> setupRcv(
            rcvView: RecyclerView,
            adapter: RecyclerView.Adapter<VH>,
            layoutManager: LayoutManager
        ) {
            rcvView.layoutManager = layoutManager
            rcvView.adapter = adapter
        }
    }

}