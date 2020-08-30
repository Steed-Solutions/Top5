package com.mars_tech.shehriyar.top5.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mars_tech.shehriyar.top5.model.MainModel;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.Comment;
import com.mars_tech.shehriyar.top5.pojo.CommentsResponse;
import com.mars_tech.shehriyar.top5.pojo.FiltersResponse;
import com.mars_tech.shehriyar.top5.pojo.LikeResponse;
import com.mars_tech.shehriyar.top5.pojo.Post;
import com.mars_tech.shehriyar.top5.pojo.PostsResponse;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private MainModel mainModel;
    public LiveData<Boolean> signOutLiveData;
    public LiveData<LikeResponse> postLikedOrUnlikedLiveData;
    public LiveData<ArrayList<Category>> allCategoriesLiveData, queriedCategoriesLiveData, selectedCategoriesLiveData;
    public LiveData<SaveResponse> saveResponseLiveData, filtersAndCategoriesSaveResponseLiveData, savePostLiveData;
    public LiveData<PostsResponse> allPostsLiveData, allSavedPostsLiveData;
    public LiveData<FiltersResponse> filtersResponseLiveData;
    public LiveData<CommentsResponse> allPostCommentsLiveData, commentOnPostLiveData, deleteCommentLiveData;
    public LiveData<String> allPostLikerStringLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mainModel = new MainModel();
    }

    public void signOutUser() {
        signOutLiveData = mainModel.signOutUser();
    }

    public void getAllCategories() {
        allCategoriesLiveData = mainModel.getAllCategories();
    }

    public void getQueriedCategories(String searchTerm) {
        queriedCategoriesLiveData = mainModel.getQueriedCategories(searchTerm);
    }

    public void saveSelectedCategories(ArrayList<String> categories) {
        saveResponseLiveData = mainModel.saveSelectedCategories(categories);
    }

    public void getAllSelectedCategoricalPosts() {
        allPostsLiveData = mainModel.getAllSelectedCategoricalPosts();
    }

    public void setLikeOrUnlikePost(Post post){
        postLikedOrUnlikedLiveData = mainModel.getPostLikedOrUnlikedLiveData(post);
    }

    public void getFiltersAndSelectedCategories() {
        filtersResponseLiveData = mainModel.getFiltersAndSelectedCategories();
    }

    public void saveFiltersAndCategories(ArrayList<String> filters, ArrayList<String> categories) {
        filtersAndCategoriesSaveResponseLiveData = mainModel.saveFiltersAndCategories(filters, categories);
    }

    public void getSelectedCategories() {
        selectedCategoriesLiveData = mainModel.getSelectedCategories();
    }

    public void getAllPostLikerString(String postID, boolean isLiked) {
        allPostLikerStringLiveData = mainModel.getAllPostLikerString(postID, isLiked);
    }

    public void getPostComments(String postID){
        allPostCommentsLiveData = mainModel.getPostComments(postID);
    }

    public void commentOnPost(String categoryID, Comment comment) {
        commentOnPostLiveData = mainModel.commentOnPost(categoryID, comment);
    }

    public void deleteCommentFromPost(String categoryID, Comment comment) {
        deleteCommentLiveData = mainModel.deleteCommentFromPost(categoryID, comment);
    }

    public void getAllSavedPosts() {
        allSavedPostsLiveData = mainModel.getAllSavedPosts();
    }

    public void savePost(String categoryID, String postID) {
        savePostLiveData = mainModel.savePost(categoryID, postID);
    }
}
