package com.mars_tech.shehriyar.top5.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.PopularListSingleItemViewBinding;
import com.mars_tech.shehriyar.top5.listener.PopularListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Post;

import java.util.ArrayList;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.PopularListViewHolder> {

    private ArrayList<String> popularItems;
    private PopularListItemClickListener popularListItemClickListener;

    public PopularListAdapter(ArrayList<String> popularItems, PopularListItemClickListener popularListItemClickListener) {
        this.popularItems = popularItems;
        this.popularListItemClickListener = popularListItemClickListener;
    }

    @NonNull
    @Override
    public PopularListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PopularListSingleItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.popular_list_single_item_view, parent, false);
        return new PopularListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final PopularListViewHolder holder, int position) {
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popularListItemClickListener.OnItemClicked(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularItems.size();
    }

    public class PopularListViewHolder extends RecyclerView.ViewHolder {

        PopularListSingleItemViewBinding binding;

        public PopularListViewHolder(@NonNull PopularListSingleItemViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
