package com.steed.top5.view.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.CompoundButton;

import com.steed.top5.R;
import com.steed.top5.adapter.ProfileInterestsListAdapter;
import com.steed.top5.databinding.FragmentFiltersBinding;
import com.steed.top5.databinding.FragmentProfileBinding;
import com.steed.top5.pojo.Category;
import com.steed.top5.singleton.UserSingleton;
import com.steed.top5.util.Constants;
import com.steed.top5.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;


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
        loadLocale();
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        initController();
        initViewModel();
        initLangSwitch();

        viewModel.getAllCategories();
        viewModel.allCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                if(viewModel.allCategoriesLiveData.hasActiveObservers()) {
                    viewModel.allCategoriesLiveData.removeObservers(requireActivity());
                }
                viewModel.getSelectedCategories();
                viewModel.selectedCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
                    @Override
                    public void onChanged(ArrayList<Category> categories) {
                        if (viewModel.selectedCategoriesLiveData.hasActiveObservers()) {
                            viewModel.selectedCategoriesLiveData.removeObservers(requireActivity());
                        }
                        binding.loadingLayout.setVisibility(View.GONE);

                        interests = categories;

                        afterDBLoad();
                    }
                });
            }
        });

        return binding.getRoot();
    }

    void initController() {
        controller = NavHostFragment.findNavController(this);
    }

    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    void initLangSwitch() {
        SharedPreferences preferenceSharedPref = requireActivity().getSharedPreferences(Constants.PREFERENCE_SHARED_PREF, Context.MODE_PRIVATE);
        String lang = preferenceSharedPref.getString(Constants.PREFERRED_LANG_PREFERRED, "en");

        binding.langSwitch.setChecked(lang.equals("fa"));
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

        binding.langSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLocale("fa");
                    requireActivity().recreate();
                } else {
                    setLocale("en");
                    requireActivity().recreate();
                }
            }
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireActivity().getBaseContext().getResources().updateConfiguration(config, requireActivity().getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(Constants.PREFERENCE_SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PREFERRED_LANG_PREFERRED, lang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences preferenceSharedPref = requireActivity().getSharedPreferences(Constants.PREFERENCE_SHARED_PREF, Context.MODE_PRIVATE);
        String lang = preferenceSharedPref.getString(Constants.PREFERRED_LANG_PREFERRED, "en");
        setLocale(lang);

    }

}
