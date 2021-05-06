package org.wit.player.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_player.view.*
import org.wit.player.R
import org.wit.player.models.PlayerModel


class PlayerAdapter constructor(private var players: List<PlayerModel>) :
        RecyclerView.Adapter<PlayerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.card_player,
                        parent,
                        false
                )
        )
    }


    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val player = players[holder.adapterPosition]
        holder.bind(player)
    }

    override fun getItemCount(): Int = players.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: PlayerModel) {
            itemView.playerTitle.text = player.title
            itemView.description.text = player.description
        }
    }
}