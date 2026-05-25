package com.example.pulsetimer.presets

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pulsetimer.data.Preset
import com.example.pulsetimer.databinding.ItemPresetBinding
import com.example.pulsetimer.timer.MainActivity

class PresetAdapter(private val presets: List<Preset>) :
    RecyclerView.Adapter<PresetAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPresetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPresetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preset = presets[position]

        holder.binding.tvPresetInfo.text =
            "⏱ RL: ${preset.roundLength}m | " +
                    "🔔 BI: ${preset.bellInterval}s | " +
                    "🛑 RBR: ${preset.restLength}s | " +
                    "🥊 TR: ${preset.totalRounds}"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("roundLength", preset.roundLength)
                putExtra("restLength", preset.restLength)
                putExtra("bellInterval", preset.bellInterval)
                putExtra("totalRounds", preset.totalRounds)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = presets.size
}