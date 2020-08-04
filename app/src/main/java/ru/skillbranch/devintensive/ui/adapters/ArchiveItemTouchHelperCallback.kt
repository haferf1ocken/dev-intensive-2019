package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem

class ArchiveItemTouchHelperCallback(
    adapter: ChatAdapter,
    swipeListener: (ChatItem) -> Unit) :
    ChatItemTouchHelperCallback(adapter, swipeListener) {

    override fun drawIcon(canvas: Canvas, itemView: View, dX: Float) {
        val icon = itemView.resources.getDrawable(R.drawable.ic_unarchive, itemView.context.theme)
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)

        val margin = (itemView.bottom - itemView.top - iconSize) / 2

        iconBounds.run {
            left = itemView.right + dX.toInt() + space
            top = itemView.top + margin
            right = itemView.right + dX.toInt() + iconSize + space
            bottom = itemView.bottom - margin
        }

        icon.bounds = iconBounds
        icon.draw(canvas)
    }
}