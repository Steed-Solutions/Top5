package com.mars_tech.shehriyar.top5.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.BrowseCategoriesListSingleItemViewBinding;

import java.util.ArrayList;

public class BrowseCategoriesListAdapter extends RecyclerView.Adapter<BrowseCategoriesListAdapter.BrowseCategoriesListViewHolder> {

    private ArrayList<String> items;

    public BrowseCategoriesListAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BrowseCategoriesListAdapter.BrowseCategoriesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BrowseCategoriesListSingleItemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.browse_categories_list_single_item_view, parent, false);
        return new BrowseCategoriesListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseCategoriesListAdapter.BrowseCategoriesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BrowseCategoriesListViewHolder extends RecyclerView.ViewHolder {

        private BrowseCategoriesListSingleItemViewBinding binding;

        public BrowseCategoriesListViewHolder(@NonNull BrowseCategoriesListSingleItemViewBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
