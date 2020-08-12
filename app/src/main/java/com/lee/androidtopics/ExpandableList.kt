package com.lee.androidtopics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.expandable_list_item.view.*
import kotlinx.android.synthetic.main.expandable_list_subitem.view.*
import java.io.Serializable

data class ExpandableItem(val title: String, val subItems: List<ExpandableSubItem>) : Serializable {
    var isExpand = false
}

data class ExpandableSubItem(val title: String, val onClickListener: View.OnClickListener)

class ExpandableListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ExpandableListAdapter(private val items: List<ExpandableItem>) :
    RecyclerView.Adapter<ExpandableListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableListViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.expandable_list_item, parent, false)
        return ExpandableListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ExpandableListViewHolder, position: Int) {
        holder.itemView.apply {
            val item = items[position]
            expandable_item_title.text = item.title
            if (item.isExpand) {
                inflateSubItems(item, expand_view)
                ic_expand.rotation = 90f
            } else {
                expand_view.visibility = View.GONE
                ic_expand.rotation = 0f
            }
            setOnClickListener {
                toggleExpandView(item, it)
            }
        }
    }

    private fun inflateSubItems(item: ExpandableItem, expandView: LinearLayout) {
        expandView.removeAllViews()
        item.subItems.forEachIndexed { i, v ->
            val subItem = LayoutInflater.from(expandView.context)
                .inflate(R.layout.expandable_list_subitem, expandView, false).apply {
                    setOnClickListener(v.onClickListener)
                    expandable_sub_item_title.text = v.title
                }
            expandView.addView(subItem)
        }
    }

    private fun toggleExpandView(item: ExpandableItem, itemView: View) {
        if (item.isExpand) {
            itemView.ic_expand.animate().apply {
                duration = 200
                rotation(0f)
            }
            collapse(itemView.expand_view)
        } else {
            itemView.ic_expand.animate().apply {
                duration = 200
                rotation(90f)
            }
            expand(item, itemView.expand_view)
        }
        item.isExpand = !item.isExpand
    }

    private fun expand(item: ExpandableItem, expandView: LinearLayout) {
        expandView.run {
            inflateSubItems(item, expandView)
            measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val actualH = measuredHeight
            layoutParams.height = 0;
            visibility = View.VISIBLE

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    layoutParams.height =
                        if (interpolatedTime == 1f)
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        else (actualH * interpolatedTime).toInt()
                    requestLayout()
                }
            }.apply { duration = 200 }
            startAnimation(animation)
        }
    }

    private fun collapse(expandView: LinearLayout) {
        expandView.run {
            val actualH = measuredHeight
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f)
                        visibility = View.GONE
                    else {
                        layoutParams.height = (actualH * (1 - interpolatedTime)).toInt()
                        requestLayout()
                    }
                }
            }.apply { duration = 200 }
            startAnimation(animation)
        }
    }
}
