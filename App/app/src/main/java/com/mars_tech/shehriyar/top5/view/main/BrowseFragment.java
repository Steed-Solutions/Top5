package com.mars_tech.shehriyar.top5.view.main;


import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.adapter.BrowseCategoriesListAdapter;
import com.mars_tech.shehriyar.top5.adapter.PopularListAdapter;
import com.mars_tech.shehriyar.top5.adapter.SearchItemsListAdapter;
import com.mars_tech.shehriyar.top5.databinding.FragmentBrowseBinding;
import com.mars_tech.shehriyar.top5.listener.BrowseCategoriesListItemClickListener;
import com.mars_tech.shehriyar.top5.listener.PopularListItemClickListener;
import com.mars_tech.shehriyar.top5.listener.SearchItemsListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Post;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private int browseCategoriesListWidth, popularListWidth, searchItemsListWidth;

    private FragmentBrowseBinding binding;
    private NavController controller;

    private ArrayList<String> popularItems;
    private ArrayList<String> allSearchItems, queriedSearchItems;

    public BrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse, container, false);
        initController();



        ArrayList<HashMap<String, Object>> browseCategories = new ArrayList<>();

        HashMap<String, Object> categoryMap1 = new HashMap<>();
        categoryMap1.put("name", "Spanish food");
        categoryMap1.put("image", R.drawable.browse_categories_dummy_image);
        categoryMap1.put("typeColor", "#A27DCE");
        categoryMap1.put("typeImage", "https://firebasestorage.googleapis.com/v0/b/top-50-9951b.appspot.com/o/content%2Fcategories%2Ffood.png?alt=media&token=2a886017-bbc0-46ce-8a96-a605dd1bf8f8");

        browseCategories.add(categoryMap1);

        HashMap<String, Object> categoryMap2 = new HashMap<>();
        categoryMap2.put("name", "Pop music");
        categoryMap2.put("image", R.drawable.pop_music);
        categoryMap2.put("typeColor", "#7EADDE");
        categoryMap2.put("typeImage", "https://firebasestorage.googleapis.com/v0/b/top-50-9951b.appspot.com/o/content%2Fcategories%2Fmusic.png?alt=media&token=35b0dbeb-9b59-4bff-a57d-d5d1af76d3d2");

        browseCategories.add(categoryMap2);

        HashMap<String, Object> categoryMap3 = new HashMap<>();
        categoryMap3.put("name", "Abstract art");
        categoryMap3.put("image", R.drawable.abstract_art);
        categoryMap3.put("typeColor", "#EF6E41");
        categoryMap3.put("typeImage", "https://firebasestorage.googleapis.com/v0/b/top-50-9951b.appspot.com/o/content%2Fcategories%2Fart.png?alt=media&token=d3c40a8d-6ff8-45a3-903a-efe323ad4810");

        browseCategories.add(categoryMap3);

        HashMap<String, Object> categoryMap4 = new HashMap<>();
        categoryMap4.put("name", "Planting");
        categoryMap4.put("image", R.drawable.planting);
        categoryMap4.put("typeColor", "#89BF6F");
        categoryMap4.put("typeImage", "https://firebasestorage.googleapis.com/v0/b/top-50-9951b.appspot.com/o/content%2Fcategories%2Fplanting.png?alt=media&token=644243d3-3df3-43de-908c-31e4e537665d");

        browseCategories.add(categoryMap4);

        popularItems = new ArrayList<>();
        popularItems.add("");
        popularItems.add("");
        popularItems.add("");
        popularItems.add("");

        allSearchItems = new ArrayList<>();
        allSearchItems.add("");
        allSearchItems.add("");
        allSearchItems.add("");

        queriedSearchItems = allSearchItems;

        // Browse Categories List
        binding.browseCategoriesList.setHasFixedSize(true);

        BrowseCategoriesListAdapter browseCategoriesListAdapter = new BrowseCategoriesListAdapter(requireContext(), browseCategories, new BrowseCategoriesListItemClickListener() {
            @Override
            public void OnItemClicked(int index) {
                BrowseFragmentDirections.ActionBrowseFragmentToDetailsFragment action = BrowseFragmentDirections.actionBrowseFragmentToDetailsFragment();
                controller.navigate(action);
            }
        });
        binding.browseCategoriesList.setAdapter(browseCategoriesListAdapter);

        GridLayoutManager browseCategoriesListLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                browseCategoriesListWidth = getWidth();
                return true;
            }
        };
        browseCategoriesListLayoutManager.setItemPrefetchEnabled(true);
        binding.browseCategoriesList.setLayoutManager(browseCategoriesListLayoutManager);

        binding.browseCategoriesList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position

                if (position % 2 == 0) {
                    outRect.right = (int) (browseCategoriesListWidth * 0.043);
                } else {
                    outRect.left = (int) (browseCategoriesListWidth * 0.043);
                }
//
                outRect.bottom = (int) (browseCategoriesListWidth * 0.086);
            }
        });

        // Popular List
        binding.popularList.setHasFixedSize(true);

        PopularListAdapter popularListAdapter = new PopularListAdapter(popularItems, new PopularListItemClickListener() {
            @Override
            public void OnItemClicked(int index) {
                BrowseFragmentDirections.ActionBrowseFragmentToDetailsFragment action = BrowseFragmentDirections.actionBrowseFragmentToDetailsFragment();
                controller.navigate(action);
            }
        });
        binding.popularList.setAdapter(popularListAdapter);

        LinearLayoutManager popularListLayoutManager = new LinearLayoutManager(this.getContext(), GridLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                popularListWidth = getWidth();
                lp.width = (int) (popularListWidth * 0.267);
                lp.height = (int) (popularListWidth * 0.267);
                return true;
            }
        };
        popularListLayoutManager.setItemPrefetchEnabled(true);
        binding.popularList.setLayoutManager(popularListLayoutManager);

        binding.popularList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position

                if (position == 0) {
                    outRect.left = (int) (popularListWidth * 0.086);
                    outRect.right = (int) (popularListWidth * 0.015);
                } else if (position == popularItems.size() - 1) {
                    outRect.right = (int) (popularListWidth * 0.086);
                    outRect.left = (int) (popularListWidth * 0.015);
                } else {
                    outRect.right = (int) (popularListWidth * 0.015);
                    outRect.left = (int) (popularListWidth * 0.015);
                }
            }
        });

        // Search Items List
        binding.searchItemsList.setHasFixedSize(true);

        SearchItemsListAdapter searchItemsListAdapter = new SearchItemsListAdapter(queriedSearchItems, new SearchItemsListItemClickListener() {
            @Override
            public void OnItemClicked(int index) {
                BrowseFragmentDirections.ActionBrowseFragmentToDetailsFragment action = BrowseFragmentDirections.actionBrowseFragmentToDetailsFragment();
                controller.navigate(action);
            }
        });
        binding.searchItemsList.setAdapter(searchItemsListAdapter);

        LinearLayoutManager searchItemsListLayoutManager = new LinearLayoutManager(this.getContext(), GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                searchItemsListWidth = getWidth();
                return true;
            }
        };
        searchItemsListLayoutManager.setItemPrefetchEnabled(true);
        binding.searchItemsList.setLayoutManager(searchItemsListLayoutManager);

        binding.searchItemsList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = (int) (searchItemsListWidth * 0.058);
            }
        });

        // Search Bar
        binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.searchTermLayout.setVisibility(View.GONE);
                    binding.noSearchTermLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.noSearchTermLayout.setVisibility(View.GONE);
                    binding.searchTermLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogStyle);
                alertDialogBuilder.setMessage("Are you sure that you want to EXIT?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                getActivity().finishAffinity();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        return binding.getRoot();
    }

    private void initController() {
        controller = NavHostFragment.findNavController(this);
    }

}
