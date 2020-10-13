package com.mars_tech.shehriyar.top5.adapter;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.CommentsListSingleItemLayoutBinding;
import com.mars_tech.shehriyar.top5.listener.CommentsListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Comment;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentsListViewHolder> {

    UserSingleton userSingleton = UserSingleton.getInstance();

    ArrayList<Comment> comments;
    CommentsListItemClickListener commentsListItemClickListener;

    public CommentsListAdapter(ArrayList<Comment> comments, CommentsListItemClickListener commentsListItemClickListener) {
        this.comments = comments;
        this.commentsListItemClickListener = commentsListItemClickListener;
    }

    @NonNull
    @Override
    public CommentsListAdapter.CommentsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentsListSingleItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.comments_list_single_item_layout, parent, false);
        return new CommentsListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsListAdapter.CommentsListViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.binding.name.setText(comment.userName);
        holder.binding.timestamp.setText(getFormattedTime(comment.timestamp));
        holder.binding.comment.setText(comment.comment);

        holder.binding.deleteCommentBtn.setVisibility(comment.userID.equals(userSingleton.currentUser.uid) ? View.VISIBLE : View.INVISIBLE);

        holder.binding.deleteCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsListItemClickListener.OnDeleteClicked(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentsListViewHolder extends RecyclerView.ViewHolder {

        CommentsListSingleItemLayoutBinding binding;

        public CommentsListViewHolder(@NonNull CommentsListSingleItemLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    private String getFormattedTime(long timestamp) {
        try {
            Date past = new Date(timestamp);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                return seconds + "s";
            } else if (minutes < 60) {
                return minutes + "m";
            } else if (hours < 24) {
                return hours + "h";
            } else {
                return days + "d";
            }
        } catch (Exception j) {
            j.printStackTrace();
        }

        return "N/A";
    }
}
