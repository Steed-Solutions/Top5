package com.mars_tech.shehriyar.top5.adapter;

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
        holder.binding.timestamp.setText("");
        holder.binding.comment.setText(comment.comment);

        if(comment.userID.equals(userSingleton.currentUser.uid)) {
            holder.binding.deleteCommentBtn.setVisibility(View.VISIBLE);
        }

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
}
