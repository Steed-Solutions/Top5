package com.mars_tech.shehriyar.top5.view.main;


import android.graphics.Rect;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.adapter.InterestsListAdapter;
import com.mars_tech.shehriyar.top5.adapter.ProfileInterestsListAdapter;
import com.mars_tech.shehriyar.top5.databinding.FragmentFiltersBinding;
import com.mars_tech.shehriyar.top5.databinding.FragmentProfileBinding;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.User;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;
import com.mars_tech.shehriyar.top5.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private NavController controller;
    private MainViewModel viewModel;

    private int interestsListWidth;

    private ArrayList<Category> interests;

    private UserSingleton userSingleton = UserSingleton.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        initController();
        initViewModel();

        viewModel.getAllCategories();
        viewModel.allCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                viewModel.allCategoriesLiveData.removeObservers(requireActivity());
                viewModel.getSelectedCategories();
                viewModel.selectedCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
                    @Override
                    public void onChanged(ArrayList<Category> categories) {
                        viewModel.selectedCategoriesLiveData.removeObservers(requireActivity());
                        binding.loadingLayout.setVisibility(View.GONE);

                        interests = categories;

                        afterDBLoad();
                    }
                });
            }
        });

        return binding.getRoot();
    }

    void initController(){
        controller = NavHostFragment.findNavController(this);
    }

    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    void afterDBLoad() {
        binding.userName.setText(userSingleton.currentUser.name.toUpperCase() + "!");
        binding.userEmail.setText(userSingleton.currentUser.email);

        if (interests.isEmpty()) {
            binding.yourInterestsNone.setVisibility(View.VISIBLE);
        } else {
            binding.interestsList.setHasFixedSize(true);

            LinearLayoutManager interestsListLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false) {
                @Override
                public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                    interestsListWidth = getWidth();
                    return true;
                }
            };
            binding.interestsList.setLayoutManager(interestsListLayoutManager);

            binding.interestsList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int position = parent.getChildAdapterPosition(view); // item position

                    outRect.right = (int) (interestsListWidth * 0.074);

                    if (position == 0) {
                        outRect.left = (int) (interestsListWidth * 0.092);
                    } else if (position == interests.size() - 1) {
                        outRect.right = (int) (interestsListWidth * 0.092);
                    }
                }
            });

            ProfileInterestsListAdapter profileInterestsListAdapter = new ProfileInterestsListAdapter(getContext(), interests);
            binding.interestsList.setAdapter(profileInterestsListAdapter);

            binding.saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.navigateUp();
                }
            });
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigateUp();
            }
        });
    }

}
