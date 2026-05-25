package com.example.pulsetimer.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pulsetimer.history.SessionDetailActivity
import com.example.pulsetimer.data.WorkoutSession
import com.example.pulsetimer.databinding.ItemHistoryBinding

class HistoryAdapter(private val sessionList: List<WorkoutSession>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessionList[position]

        holder.binding.tvDate.text = "📅 ${session.date}"

        holder.binding.tvRounds.text =
            "🥊 ${session.rounds} Rounds"

        holder.binding.tvDuration.text =
            "⏱ RL: ${session.roundLength} min | " +
                    "🛑 RBR: ${session.restLength} sec | " +
                    "🔔 BI: ${session.bellInterval} sec"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, SessionDetailActivity::class.java).apply {
                putExtra("id", session.id)
                putExtra("date", session.date)
                putExtra("rounds", session.rounds)
                putExtra("duration", session.duration)
                putExtra("roundLength", session.roundLength)
                putExtra("restLength", session.restLength)
                putExtra("bellInterval", session.bellInterval)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = sessionList.size
}