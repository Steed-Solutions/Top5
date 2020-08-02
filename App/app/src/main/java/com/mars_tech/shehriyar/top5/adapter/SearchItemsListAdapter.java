package com.mars_tech.shehriyar.top5.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.SearchItemsListSingleItemLayoutBinding;
import com.mars_tech.shehriyar.top5.listener.SearchItemsListItemClickListener;

import java.util.ArrayList;

public class SearchItemsListAdapter extends RecyclerView.Adapter<SearchItemsListAdapter.SearchItemsListViewHolder> {

    private ArrayList<String> searchItems;
    private SearchItemsListItemClickListener searchItemsListItemClickListener;

    public SearchItemsListAdapter(ArrayList<String> searchItems, SearchItemsListItemClickListener searchItemsListItemClickListener) {
        this.searchItems = searchItems;
        this.searchItemsListItemClickListener = searchItemsListItemClickListener;
    }

    @NonNull
    @Override
    public SearchItemsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemsListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.search_items_list_single_item_layout, parent, false);
        return new SearchItemsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemsListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public class SearchItemsListViewHolder extends RecyclerView.ViewHolder {

        SearchItemsListSingleItemLayoutBinding binding;

        public SearchItemsListViewHolder(@NonNull SearchItemsListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
