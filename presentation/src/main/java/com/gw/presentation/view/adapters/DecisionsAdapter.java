package com.gw.presentation.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gw.presentation.R;
import com.gw.presentation.model.DecisionModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DecisionsAdapter extends RecyclerView.Adapter<DecisionsAdapter.DecisionViewHolder> {

    public interface OnItemClickListener {
        void onDecisionItemClicked(DecisionModel DecisionModel);
    }

    private List<DecisionModel> decisionCollection;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    @Inject
    DecisionsAdapter(Context context) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.decisionCollection = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return (this.decisionCollection != null) ? this.decisionCollection.size() : 0;
    }

    @Override
    public DecisionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.decision_list_item, parent, false);
        return new DecisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DecisionViewHolder holder, final int position) {
        final DecisionModel decisionModel = this.decisionCollection.get(position);
        holder.textViewTitle.setText(decisionModel.getTitle());
        holder.textViewTags.setText(decisionModel.getTagsString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DecisionsAdapter.this.onItemClickListener != null) {
                    DecisionsAdapter.this.onItemClickListener.onDecisionItemClicked(decisionModel);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDecisionsCollection(Collection<DecisionModel> decisionCollection) {
        this.validateDecisionsCollection(decisionCollection);
        this.decisionCollection = (List<DecisionModel>) decisionCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateDecisionsCollection(Collection<DecisionModel> DecisionsCollection) {
        if (DecisionsCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class DecisionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.decision_list_item_title)
        TextView textViewTitle;

        @BindView(R.id.decision_list_tags_text)
        TextView textViewTags;
//
//        @BindView(R.id.decision_list_icon)
//        ImageView imageView;

        DecisionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
