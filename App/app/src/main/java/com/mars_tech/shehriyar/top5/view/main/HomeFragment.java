package com.mars_tech.shehriyar.top5.view.main;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.adapter.PreferenceItemsListAdapter;
import com.mars_tech.shehriyar.top5.databinding.FragmentHomeBinding;
import com.mars_tech.shehriyar.top5.listener.PreferenceItemsListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Category;
import com.mars_tech.shehriyar.top5.pojo.LikeResponse;
import com.mars_tech.shehriyar.top5.pojo.Post;
import com.mars_tech.shehriyar.top5.pojo.PostsResponse;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;
import com.mars_tech.shehriyar.top5.viewmodel.MainViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NavController controller;
    private MainViewModel viewModel;

    private PreferenceItemsListAdapter preferenceItemsListAdapter;

    private ArrayList<Post> preferenceItems;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        initViewModel();
        initController();
        initBannerAd();

        viewModel.getAllCategories();
        viewModel.allCategoriesLiveData.observe(requireActivity(), new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {


                viewModel.getAllSelectedCategoricalPosts();
                viewModel.allPostsLiveData.observe(requireActivity(), new Observer<PostsResponse>() {
                    @Override
                    public void onChanged(PostsResponse postsResponse) {
                        if (postsResponse.isError) {
                            binding.statusTxt.setText(postsResponse.statusMessage);
                            binding.loadingLayout.setVisibility(View.GONE);
                            binding.noneLayout.setVisibility(View.VISIBLE);
                        } else {
                            if(preferenceItems != null) {
                                preferenceItems.clear();
                                preferenceItems.addAll(postsResponse.posts);
                                preferenceItemsListAdapter.notifyDataSetChanged();
                            } else {
                                preferenceItems = new ArrayList<>();
                                preferenceItems.addAll(postsResponse.posts);
                            }
                            afterDBLoad();
                        }
                    }
                });

//                viewModel.allCategoriesLiveData.removeObservers(requireActivity());
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

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void initController() {
        controller = NavHostFragment.findNavController(this);

    }
    private void initBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adBanner.loadAd(adRequest);
    }

    private void afterDBLoad() {
        binding.preferenceItemsList.setItemAnimator(null);
        binding.preferenceItemsList.setNestedScrollingEnabled(false);
        binding.preferenceItemsList.setHasFixedSize(true);

        preferenceItemsListAdapter = new PreferenceItemsListAdapter(getContext(), preferenceItems, new PreferenceItemsListItemClickListener() {
            @Override
            public void OnItemClicked(int index) {
                HomeFragmentDirections.ActionHomeFragmentToContentFragment action = HomeFragmentDirections.actionHomeFragmentToContentFragment().setPostArg(preferenceItems.get(index));
                controller.navigate(action);
            }

            @Override
            public void OnPostLikeContainerClicked(int index) {
                viewModel.setLikeOrUnlikePost(preferenceItems.get(index));
                viewModel.postLikedOrUnlikedLiveData.observe(requireActivity(), new Observer<LikeResponse>() {
                    @Override
                    public void onChanged(LikeResponse response) {
                        if(response.isSuccess) {
                            preferenceItems.set(preferenceItems.indexOf(response.post), response.post);
                            preferenceItemsListAdapter.notifyItemChanged(preferenceItems.indexOf(response.post));
                        }
                    }
                });
            }

            @Override
            public void OnPostCommentsContainerClicked(int index) {
                HomeFragmentDirections.ActionHomeFragmentToCommentsFragment action = HomeFragmentDirections.actionHomeFragmentToCommentsFragment().setPostArg(preferenceItems.get(index));
                controller.navigate(action);
            }

            @Override
            public void OnPostSaveBtnClicked(int index) {
                viewModel.savePost(preferenceItems.get(index).category.id, preferenceItems.get(index).id);
                viewModel.savePostLiveData.observe(requireActivity(), new Observer<SaveResponse>() {
                    @Override
                    public void onChanged(SaveResponse saveResponse) {
                        if(!saveResponse.isError) {
                            Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), saveResponse.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        viewModel.savePostLiveData.removeObservers(requireActivity());
                    }
                });
            }
        });
        binding.preferenceItemsList.setAdapter(preferenceItemsListAdapter);


        LinearLayoutManager preferenceItemsListLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        preferenceItemsListLayoutManager.setItemPrefetchEnabled(true);

        binding.preferenceItemsList.setLayoutManager(preferenceItemsListLayoutManager);

        binding.loadingLayout.setVisibility(View.GONE);
    }

}
