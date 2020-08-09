package com.mars_tech.shehriyar.top5.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.FiltersResponse;
import com.mars_tech.shehriyar.top5.pojo.Post;
import com.mars_tech.shehriyar.top5.pojo.PostsResponse;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainModel {

    private UserSingleton userSingleton = UserSingleton.getInstance();

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Post> allPosts = new ArrayList<>();
    private ArrayList<String> allPostIDs;

    private MutableLiveData<ArrayList<Category>> allQueriedCategoriesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<PostsResponse> allPostsMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> signOutUser() {
        MutableLiveData<Boolean> signOutMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signOut();
        signOutMutableLiveData.setValue(true);
        return signOutMutableLiveData;
    }

    public MutableLiveData<ArrayList<Category>> getAllCategories() {
        final MutableLiveData<ArrayList<Category>> allCategoriesMutableLiveData = new MutableLiveData<>();

        if (!categories.isEmpty()) {
            allCategoriesMutableLiveData.setValue(categories);
            return allCategoriesMutableLiveData;
        }

        firebaseDatabase.child("content").child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot category : snapshot.getChildren()) {
                        categories.add(
                                new Category(
                                        category.getKey(),
                                        category.child("name").getValue().toString(),
                                        category.child("imgURL").getValue().toString(),
                                        category.child("color").getValue().toString()
                                )
                        );
                    }

                    Log.d("CATEGORIES_COUNT", "" + categories.size());
                    allCategoriesMutableLiveData.setValue(categories);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return allCategoriesMutableLiveData;
    }

    public MutableLiveData<ArrayList<Category>> getQueriedCategories(String searchTerm) {
        searchTerm = searchTerm.toUpperCase();
        ArrayList<Category> queriedCategories = new ArrayList<>();
        if (searchTerm.length() == 0) {
            Log.d("WOOOOOT", "" + searchTerm.length());
            queriedCategories.addAll(categories);
        } else {
            for (Category category : categories) {
                if (category.name.length() >= searchTerm.length()) {
                    if (category.name.toUpperCase().substring(0, searchTerm.length()).equals(searchTerm)) {
                        queriedCategories.add(category);
                    }
                }
            }
        }

        Log.d("WOOOOOT_CATEGORIES", "" + categories.size());
        allQueriedCategoriesMutableLiveData.postValue(queriedCategories);

        return allQueriedCategoriesMutableLiveData;
    }

    public MutableLiveData<SaveResponse> saveSelectedCategories(ArrayList<String> categories) {
        final MutableLiveData<SaveResponse> savedMutableLiveData = new MutableLiveData<>();

        HashMap<String, String> categoryMap = new HashMap<>();
        for (String categoryID : categories) {
            categoryMap.put(categoryID, "categoryID");
        }

        firebaseDatabase.child("users").child("regularUsers").child(userSingleton.currentUser.uid).child("preferences").child("categories").setValue(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SaveResponse saveResponse = new SaveResponse();

                if (task.isSuccessful()) {
                    saveResponse.isSaved = true;
                    savedMutableLiveData.setValue(saveResponse);
                } else {
                    saveResponse.isError = true;
                    saveResponse.statusMessage = task.getException().getMessage();
                    savedMutableLiveData.setValue(saveResponse);
                }
            }
        });

        return savedMutableLiveData;
    }

    public MutableLiveData<SaveResponse> saveFiltersAndCategories(ArrayList<String> filters, ArrayList<String> categories) {
        final MutableLiveData<SaveResponse> savedMutableLiveData = new MutableLiveData<>();

        HashMap<String, String> categoryMap = new HashMap<>();
        for (String categoryID : categories) {
            categoryMap.put(categoryID, "categoryID");
        }

        HashMap<String, Map<String, String>> map = new HashMap<>();
        map.put("categories", categoryMap);


        firebaseDatabase.child("users").child("regularUsers").child(userSingleton.currentUser.uid).child("preferences").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SaveResponse saveResponse = new SaveResponse();

                if (task.isSuccessful()) {
                    saveResponse.isSaved = true;
                    savedMutableLiveData.setValue(saveResponse);
                } else {
                    saveResponse.isError = true;
                    saveResponse.statusMessage = task.getException().getMessage();
                    savedMutableLiveData.setValue(saveResponse);
                }
            }
        });

        return savedMutableLiveData;
    }

    public MutableLiveData<PostsResponse> getAllSelectedCategoricalPosts() {
        final HashMap<String, Category> categoryIDToCategory = new HashMap<>();
        for (Category category : categories) {
            categoryIDToCategory.put(category.id, category);
        }

        allPosts = new ArrayList<>();
        allPostIDs = new ArrayList<>();

        firebaseDatabase.child("users").child("regularUsers").child(userSingleton.currentUser.uid).child("preferences").child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot categorySnapshot) {
                final PostsResponse postsResponse = new PostsResponse();
                if (categorySnapshot.exists()) {
                    postsResponse.posts = new ArrayList<>();
//                    final int[] i = {0};
                    for (final DataSnapshot category : categorySnapshot.getChildren()) {
                        firebaseDatabase.child("content").child("posts").child(category.getKey()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot post, @Nullable String previousChildName) {
                                Log.d("NEW_POST_MODEL", "UPDATESSS BROOOO : " + previousChildName);
                                allPosts.add(new Post(
                                        post.getKey(),
                                        post.child("type").getValue().toString(),
                                        post.child("name").getValue().toString(),
                                        !post.child("type").getValue().toString().equals("txt") ? post.child("link").getValue().toString() : "",
                                        post.child("text").getValue().toString(),
                                        categoryIDToCategory.get(category.getKey())
                                ));
                                allPostIDs.add(post.getKey());

                                postsResponse.posts.clear();
                                postsResponse.posts.addAll(allPosts);

                                allPostsMutableLiveData.postValue(postsResponse);
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot post) {
                                if(allPostIDs.indexOf(post.getKey()) >= 0) {
                                    allPosts.remove(allPostIDs.indexOf(post.getKey()));
                                    allPostIDs.remove(post.getKey());

                                    postsResponse.posts.clear();
                                    postsResponse.posts.addAll(allPosts);

                                    allPostsMutableLiveData.postValue(postsResponse);
                                }
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

//                        firebaseDatabase.child("content").child("posts").child(category.getKey()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot postsSnapshot) {
//                                for (DataSnapshot post : postsSnapshot.getChildren()) {
//
//                                }
//
////                                i[0]++;
////
////                                if (i[0] == categorySnapshot.getChildrenCount()) {
//                                    Log.d("NEW_POST_MODEL", "UPDATESSS BROOOO");
//                                    allPostsMutableLiveData.postValue(postsResponse);
////                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                    }
                } else {
                    postsResponse.isError = true;
                    postsResponse.statusMessage = "Please select some interests from the filter menu to view relevant posts.";
                    allPostsMutableLiveData.postValue(postsResponse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return allPostsMutableLiveData;
    }

    public MutableLiveData<FiltersResponse> getFiltersAndSelectedCategories() {
        final MutableLiveData<FiltersResponse> filtersResponseMutableLiveData = new MutableLiveData<>();

        final HashMap<String, Category> categoryIDToCategory = new HashMap<>();
        for (Category category : categories) {
            categoryIDToCategory.put(category.id, category);
        }

        firebaseDatabase.child("users").child("regularUsers").child(userSingleton.currentUser.uid).child("preferences").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FiltersResponse filtersResponse = new FiltersResponse();
                filtersResponse.allCategories = categories;
                if (snapshot.exists()) {
                    ArrayList<String> selectedCategories = new ArrayList<>();
                    for (DataSnapshot category : snapshot.child("categories").getChildren()) {
                        selectedCategories.add(category.getKey());
                    }
                    filtersResponse.selectedCategories = selectedCategories;
                    filtersResponseMutableLiveData.setValue(filtersResponse);
                } else {
                    filtersResponse.selectedCategories = new ArrayList<>();
                    filtersResponseMutableLiveData.setValue(filtersResponse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return filtersResponseMutableLiveData;
    }

    public MutableLiveData<ArrayList<Category>> getSelectedCategories() {
        final MutableLiveData<ArrayList<Category>> selectedCategoriesMutableLiveData = new MutableLiveData<>();

        final HashMap<String, Category> categoryIDToCategory = new HashMap<>();
        for (Category category : categories) {
            categoryIDToCategory.put(category.id, category);
        }

        firebaseDatabase.child("users").child("regularUsers").child(userSingleton.currentUser.uid).child("preferences").child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Category> selectedCategories = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot category : snapshot.getChildren()) {
                        selectedCategories.add(categoryIDToCategory.get(category.getKey()));
                    }
                }

                selectedCategoriesMutableLiveData.setValue(selectedCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return selectedCategoriesMutableLiveData;
    }

}
