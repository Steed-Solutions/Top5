package com.mars_tech.shehriyar.top5.view.main;


import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.adapter.InterestsListAdapter;
import com.mars_tech.shehriyar.top5.databinding.FragmentFiltersBinding;
import com.mars_tech.shehriyar.top5.listener.InterestsListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.FiltersResponse;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;
import com.mars_tech.shehriyar.top5.viewmodel.MainViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersFragment extends Fragment {

    private FragmentFiltersBinding binding;
    private NavController controller;
    private MainViewModel viewModel;

    private int interestsListWidth;

    private ArrayList<Category> allInterests;
    private ArrayList<String> selectedInterests, updatedInterests;

    private CheckBox lastCheckedCheckBox;

    public FiltersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false);
        controller = NavHostFragment.findNavController(this);

        initViewModel();
        initCheckBoxes();

        viewModel.getAllCategories();
        viewModel.allCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                viewModel.allCategoriesLiveData.removeObservers(requireActivity());
                viewModel.getFiltersAndSelectedCategories();
                viewModel.filtersResponseLiveData.observe(requireActivity(), new Observer<FiltersResponse>() {
                    @Override
                    public void onChanged(FiltersResponse filtersResponse) {
                        viewModel.filtersResponseLiveData.removeObservers(requireActivity());
                        allInterests = filtersResponse.allCategories;
                        selectedInterests = filtersResponse.selectedCategories;
                        updatedInterests = new ArrayList<>();
                        updatedInterests.addAll(selectedInterests);

                        afterDBLoad();
                    }
                });
            }
        });


        return binding.getRoot();
    }

    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    void initCheckBoxes() {
        final HashMap<ConstraintLayout, CheckBox> checkboxes = new HashMap<>();
        checkboxes.put(binding.checkbox1, binding.checkboxInp1);
        checkboxes.put(binding.checkbox2, binding.checkboxInp2);
        checkboxes.put(binding.checkbox3, binding.checkboxInp3);
        checkboxes.put(binding.checkbox4, binding.checkboxInp4);

        lastCheckedCheckBox = binding.checkboxInp1.isChecked() ? binding.checkboxInp1 : (binding.checkboxInp2.isChecked() ? binding.checkboxInp2 : (binding.checkboxInp3.isChecked() ? binding.checkboxInp3 : binding.checkboxInp4));

        for (final ConstraintLayout layout : checkboxes.keySet()) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!checkboxes.get(layout).isChecked()) {
                        lastCheckedCheckBox.setChecked(false);
                        checkboxes.get(layout).setChecked(true);
                        lastCheckedCheckBox = checkboxes.get(layout);
                    }
                }
            });
        }
    }

    void afterDBLoad() {
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
                outRect.right = (int) (interestsListWidth * 0.037);
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(FiltersFragment.this).navigateUp();
            }
        });

        InterestsListAdapter interestsListAdapter = new InterestsListAdapter(getContext(), allInterests, selectedInterests, new InterestsListItemClickListener() {
            @Override
            public void OnItemClicked(String id) {
                if (updatedInterests.contains(id)) {
                    updatedInterests.remove(id);
                } else {
                    updatedInterests.add(id);
                }
            }
        });
        binding.interestsList.setAdapter(interestsListAdapter);

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedInterests.containsAll(updatedInterests) || !updatedInterests.containsAll(selectedInterests) || selectedInterests.size() != updatedInterests.size()) {
                    showOverlay();
                    viewModel.saveFiltersAndCategories(new ArrayList<String>(), updatedInterests);
                    viewModel.filtersAndCategoriesSaveResponseLiveData.observe(requireActivity(), new Observer<SaveResponse>() {
                        @Override
                        public void onChanged(SaveResponse saveResponse) {
                            viewModel.filtersAndCategoriesSaveResponseLiveData.removeObservers(requireActivity());
                            hideOverlay();
                            if (saveResponse.isError) {
                                Toast.makeText(getContext(), saveResponse.statusMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                controller.navigateUp();
                            }
                        }
                    });
                } else {
                    controller.navigateUp();
                }
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
}
