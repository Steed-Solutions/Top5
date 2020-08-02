package com.mars_tech.shehriyar.top5.view.main;


import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.adapter.UserPreferencesListAdapter;
import com.mars_tech.shehriyar.top5.databinding.FragmentUserPrefBinding;
import com.mars_tech.shehriyar.top5.listener.UserPreferencesListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;
import com.mars_tech.shehriyar.top5.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPrefFragment extends Fragment {

    private FragmentUserPrefBinding binding;
    private NavController controller;
    private MainViewModel viewModel;

    private int categoriesListWidth;
    private ArrayList<Category> allCategories;
    private ArrayList<String> selectedCategories;

    private UserSingleton userSingleton = UserSingleton.getInstance();

    public UserPrefFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_pref, container, false);

        controller = NavHostFragment.findNavController(this);

        if (!userSingleton.currentUser.isNew) {
            controller.navigate(UserPrefFragmentDirections.actionUserPrefFragmentToHomeFragment(), new NavOptions.Builder().setPopUpTo(R.id.userPrefFragment, true).build());
        } else {
            initViewModel();

            viewModel.getAllCategories();
            viewModel.allCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
                @Override
                public void onChanged(ArrayList<Category> categories) {
                    allCategories = categories;
                    Log.d("CATEGORIES_USER_PREF", "" + categories.size());
                    afterDBLoad();
                }
            });

            selectedCategories = new ArrayList<>();

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
        }

        return binding.getRoot();
    }

    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    void afterDBLoad() {
        binding.categoriesGrid.setHasFixedSize(true);

        UserPreferencesListAdapter userPreferencesListAdapter = new UserPreferencesListAdapter(getContext(), allCategories, new UserPreferencesListItemClickListener() {
            @Override
            public void OnItemClicked(int index) {
                if (selectedCategories.contains(allCategories.get(index))) {
                    selectedCategories.remove(allCategories.get(index));

                    if (selectedCategories.isEmpty()) {
                        binding.saveBtn.setEnabled(false);
                        binding.saveBtn.setAlpha((float) 0.4);
                    }
                } else {
                    selectedCategories.add(allCategories.get(index).id);

                    if (selectedCategories.size() == 1) {
                        binding.saveBtn.setEnabled(true);
                        binding.saveBtn.setAlpha(1);
                    }
                }
            }
        });
        binding.categoriesGrid.setAdapter(userPreferencesListAdapter);

        GridLayoutManager categoriesListLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                categoriesListWidth = getWidth();
                lp.width = (int) (getWidth() * 0.42);
                return true;
            }
        };
        categoriesListLayoutManager.setItemPrefetchEnabled(true);

        binding.categoriesGrid.setLayoutManager(categoriesListLayoutManager);

        binding.categoriesGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position

                if (position % 2 == 0) {
                    outRect.right = (int) (categoriesListWidth * 0.083);
                } else {
                    outRect.left = (int) (categoriesListWidth * 0.083);
                }

                outRect.bottom = (int) (categoriesListWidth * 0.083 * 2);
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverlay();
                viewModel.saveSelectedCategories(selectedCategories);
                viewModel.saveResponseLiveData.observe(requireActivity(), new Observer<SaveResponse>() {
                    @Override
                    public void onChanged(SaveResponse saveResponse) {
                        hideOverlay();
                        if (saveResponse.isError) {
                            showError(saveResponse.statusMessage);
                        } else {
                            controller.navigate(UserPrefFragmentDirections.actionUserPrefFragmentToHomeFragment(), new NavOptions.Builder().setPopUpTo(R.id.userPrefFragment, true).build());
                        }
                    }
                });
            }
        });

        binding.loadingLayout.setVisibility(View.GONE);
    }

    private void showOverlay() {
        AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        binding.progressBarHolder.setAnimation(inAnimation);
        binding.progressBarHolder.setVisibility(View.VISIBLE);
    }

    private void hideOverlay() {
        AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        binding.progressBarHolder.setAnimation(outAnimation);
        binding.progressBarHolder.setVisibility(View.GONE);
    }

    private void showError(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogStyle);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
