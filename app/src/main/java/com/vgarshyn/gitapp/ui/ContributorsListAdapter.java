package com.vgarshyn.gitapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vgarshyn.gitapp.R;
import com.vgarshyn.gitapp.rest.model.Contributor;

import java.util.Collections;
import java.util.List;

/**
 * Created by v.garshyn on 11.02.18.
 */

public class ContributorsListAdapter extends RecyclerView.Adapter<ContributorsListAdapter.ContributorVH> {

    private List<Contributor> contributors = Collections.emptyList();

    public ContributorsListAdapter(List<Contributor> contributors) {
        if (contributors != null) {
            this.contributors = contributors;
        }
    }

    @Override
    public ContributorVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_contributor, parent, false);
        return new ContributorVH(view);
    }

    @Override
    public void onBindViewHolder(ContributorVH holder, int position) {
        Contributor contributor = contributors.get(position);
        Context context = holder.itemView.getContext();

        String avatarUrl = contributor.avatarUrl;
        String statisticText = context.getString(R.string.commits_statistic, contributor.contributions);
        String formattedNumber = context.getString(R.string.number_format, position+1);

        Picasso.with(context)
                .load(avatarUrl)
                .into(holder.avatarView);
        holder.loginView.setText(contributor.login);
        holder.numberView.setText(formattedNumber);
        holder.statisticView.setText(statisticText);
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    public static class ContributorVH extends RecyclerView.ViewHolder {

        ImageView avatarView;
        TextView loginView;
        TextView numberView;
        TextView statisticView;

        public ContributorVH(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.avatarImage);
            loginView = itemView.findViewById(R.id.textLogin);
            numberView = itemView.findViewById(R.id.textNumber);
            statisticView = itemView.findViewById(R.id.textStatistic);
        }
    }
}
