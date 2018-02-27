package com.vgarshyn.gitapp.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.vgarshyn.gitapp.R
import com.vgarshyn.gitapp.rest.model.Contributor

/**
 * Trivial Adapter for [RecyclerView]
 *
 * Created by v.garshyn on 11.02.18.
 */

class ContributorsListAdapter : RecyclerView.Adapter<ContributorsListAdapter.ContributorVH>() {

    private var contributors = emptyList<Contributor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contributor, parent, false)
        return ContributorVH(view)
    }

    override fun onBindViewHolder(holder: ContributorVH, position: Int) {
        val contributor = contributors[position]
        val context = holder.itemView.context

        val statisticText = context.getString(R.string.commits_statistic, contributor.contributions)
        val formattedNumber = context.getString(R.string.number_format, position + 1)

        Picasso.with(context)
                .load(contributor.avatarUrl)
                .into(holder.avatarView)
        holder.loginView.text = contributor.login
        holder.numberView.text = formattedNumber
        holder.statisticView.text = statisticText
    }

    override fun getItemCount(): Int {
        return contributors.size
    }

    /**
     * Update dataset and notify Recycler about changes
     * @param contributors
     */
    fun setContributors(contributors: List<Contributor>?) {
        this.contributors = contributors ?: emptyList()
        notifyDataSetChanged()
    }

    /**
     * ViewHolder based on *R.layout.item_contributor*
     */
    class ContributorVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatarView: ImageView = itemView.findViewById(R.id.avatarImage)
        var loginView: TextView = itemView.findViewById(R.id.textLogin)
        var numberView: TextView = itemView.findViewById(R.id.textNumber)
        var statisticView: TextView = itemView.findViewById(R.id.textStatistic)
    }
}
