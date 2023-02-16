package pl.bk20.forest.presentation

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.FloatRange
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import pl.bk20.forest.databinding.ItemChartBarBinding
import pl.bk20.forest.util.getThemeColor

class ChartAdapter<T>(
    private val listener: OnValueSelected<T>
) : ListAdapter<ChartAdapter.ChartValue<T>, ChartAdapter.ChartItemViewHolder<T>>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartItemViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChartBarBinding.inflate(layoutInflater, parent, false)
        return ChartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartItemViewHolder<T>, position: Int) {
        val value = getItem(position)
        holder.bind(value, listener)
    }

    class ChartItemViewHolder<T>(
        private val binding: ItemChartBarBinding
    ) : ViewHolder(binding.root) {

        fun bind(chartValue: ChartValue<T>, listener: OnValueSelected<T>) {
            binding.root.setOnClickListener { listener.onSelect(chartValue) }
            binding.textSupporting.apply {
                text = chartValue.label
                val color = context.getThemeColor(chartValue.textColor)
                setTextColor(color)
            }
            binding.barFilled.apply {
                val color = context.getThemeColor(chartValue.barColor)
                backgroundTintList = ColorStateList.valueOf(color)
                val params = layoutParams as ConstraintLayout.LayoutParams
                params.matchConstraintPercentHeight = chartValue.value
                requestLayout()
            }
        }
    }

    private class DiffCallback<T> : DiffUtil.ItemCallback<ChartValue<T>>() {

        override fun areItemsTheSame(oldItem: ChartValue<T>, newItem: ChartValue<T>): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChartValue<T>, newItem: ChartValue<T>): Boolean {
            return oldItem == newItem
        }
    }

    data class ChartValue<T>(

        val id: T,

        @FloatRange(from = 0.0, to = 1.0)
        val value: Float,

        val label: String,

        @AttrRes
        val barColor: Int,

        @AttrRes
        val textColor: Int,
    )

    fun interface OnValueSelected<T> {
        fun onSelect(value: ChartValue<T>)
    }
}