package com.mars_tech.shehriyar.top5.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.PreferenceItemsListSingleItemLayoutBinding;
import com.mars_tech.shehriyar.top5.listener.PreferenceItemsListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Post;

import java.util.ArrayList;

public class PreferenceItemsListAdapter extends RecyclerView.Adapter<PreferenceItemsListAdapter.PreferenceItemsListViewHolder> {

    private Context context;
    private ArrayList<Post> preferenceItems;
    private PreferenceItemsListItemClickListener preferenceItemsListItemClickListener;

    private static int lastSelectedPos = -1;

    public PreferenceItemsListAdapter(Context context, ArrayList<Post> preferenceItems, PreferenceItemsListItemClickListener preferenceItemsListItemClickListener) {
        this.context = context;
        this.preferenceItems = preferenceItems;
        this.preferenceItemsListItemClickListener = preferenceItemsListItemClickListener;
    }

    @NonNull
    @Override
    public PreferenceItemsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PreferenceItemsListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.preference_items_list_single_item_layout, parent, false);
        return new PreferenceItemsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferenceItemsListViewHolder holder, int position) {
        Post post = preferenceItems.get(position);

        holder.binding.itemName.setText(post.name);

        if (post.type.contains("img")) {
            Glide.with(context).load(post.link).into(holder.binding.itemContentImage);
        } else if (post.type.contains("vid")) {
            holder.binding.playBtn.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().load(post.link).into(holder.binding.itemContentImage);
        }

        holder.binding.itemContentText.setText(post.type.toLowerCase().contains("txt") ? post.text : "");

        Drawable backgroundDrawable = DrawableCompat.wrap(holder.binding.itemTypeImageContainer.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, Color.parseColor(post.category.color));

        Glide.with(context).load(post.category.imgURL).into(holder.binding.itemTypeImage);

        if (lastSelectedPos == position) {
            holder.binding.itemImageOverlay.setVisibility(View.GONE);
            holder.binding.itemContentText.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams topGap = holder.binding.itemImageContainerTopSpace.getLayoutParams();
            topGap.height = 0;
            holder.binding.itemImageContainerTopSpace.setLayoutParams(topGap);
        } else {
            holder.binding.itemImageOverlay.setVisibility(View.VISIBLE);
            holder.binding.itemContentText.setVisibility(View.GONE);
            ViewGroup.LayoutParams topGap = holder.binding.itemImageContainerTopSpace.getLayoutParams();
            topGap.height = 1;
            holder.binding.itemImageContainerTopSpace.setLayoutParams(topGap);
        }


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedPos >= 0) {
                    notifyItemChanged(lastSelectedPos);
                }

                if (lastSelectedPos != holder.getLayoutPosition()) {
                    lastSelectedPos = holder.getLayoutPosition();

                    notifyItemChanged(lastSelectedPos);
                }

                preferenceItemsListItemClickListener.OnItemClicked(holder.getLayoutPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return preferenceItems.size();
    }

    public class PreferenceItemsListViewHolder extends RecyclerView.ViewHolder {

        PreferenceItemsListSingleItemLayoutBinding binding;

        public PreferenceItemsListViewHolder(@NonNull PreferenceItemsListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
