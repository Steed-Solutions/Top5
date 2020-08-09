package com.mars_tech.shehriyar.top5.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mars_tech.shehriyar.top5.model.MainModel;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.FiltersResponse;
import com.mars_tech.shehriyar.top5.pojo.Post;
import com.mars_tech.shehriyar.top5.pojo.PostsResponse;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private MainModel mainModel;
    public LiveData<Boolean> signOutLiveData;
    public LiveData<ArrayList<Category>> allCategoriesLiveData, queriedCategoriesLiveData, selectedCategoriesLiveData;
    public LiveData<SaveResponse> saveResponseLiveData, filtersAndCategoriesSaveResponseLiveData;
    public LiveData<PostsResponse> allPostsLiveData;
    public LiveData<FiltersResponse> filtersResponseLiveData;

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

    public void getFiltersAndSelectedCategories() {
        filtersResponseLiveData = mainModel.getFiltersAndSelectedCategories();
    }

    public void saveFiltersAndCategories(ArrayList<String> filters, ArrayList<String> categories) {
        filtersAndCategoriesSaveResponseLiveData = mainModel.saveFiltersAndCategories(filters, categories);
    }

    public void getSelectedCategories() {
        selectedCategoriesLiveData = mainModel.getSelectedCategories();
    }
}
