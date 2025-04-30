package com.example.landrenting


import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space // Add spacing below each item
        outRect.left = space / 2 // Optional: Add horizontal spacing
        outRect.right = space / 2 // Optional: Add horizontal spacing

        // Optionally, add top spacing only for the first item
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
    }
}