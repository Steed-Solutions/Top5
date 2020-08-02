package com.mars_tech.shehriyar.top5.view.main;


import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mars_tech.shehriyar.top5.listener.SearchItemsListItemClickListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private int browseCategoriesListWidth, popularListWidth, searchItemsListWidth;

    private FragmentBrowseBinding binding;

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

        ArrayList<String> browseCategories = new ArrayList<>();
        browseCategories.add("");
        browseCategories.add("");
        browseCategories.add("");
        browseCategories.add("");
        browseCategories.add("");

        popularItems = new ArrayList<>();
        popularItems.add("");
        popularItems.add("");
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

        BrowseCategoriesListAdapter browseCategoriesListAdapter = new BrowseCategoriesListAdapter(browseCategories);
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

        PopularListAdapter popularListAdapter = new PopularListAdapter(popularItems);
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
                if(s.length() == 0) {
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

}
