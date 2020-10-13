package com.mars_tech.shehriyar.top5.view.main;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.adapter.CommentsListAdapter;
import com.mars_tech.shehriyar.top5.databinding.FragmentContentBinding;
import com.mars_tech.shehriyar.top5.listener.CommentsListItemClickListener;
import com.mars_tech.shehriyar.top5.pojo.Comment;
import com.mars_tech.shehriyar.top5.pojo.CommentsResponse;
import com.mars_tech.shehriyar.top5.pojo.LikeResponse;
import com.mars_tech.shehriyar.top5.pojo.Post;
import com.mars_tech.shehriyar.top5.pojo.SaveResponse;
import com.mars_tech.shehriyar.top5.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private FragmentContentBinding binding;
    private NavController controller;
    private MainViewModel viewModel;

    private Post post;

    private CommentsListAdapter commentsListAdapter;
    private ArrayList<Comment> comments;

    private MediaController videoController;

    private SimpleExoPlayer player;

    // Player vars
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;


    public ContentFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if(Util.SDK_INT >= 24) {
//            if(post != null) {
//                if (post.type.contains("vid")) {
//                    initExoPlayer();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        hideSystemUi();
//        if(Util.SDK_INT < 24 || player == null) {
//            if(post != null) {
//                if (post.type.contains("vid")) {
//                    initExoPlayer();
//                }
//            }
//        }
//
//    }
//
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            if(post != null) {
                if (post.type.contains("vid")) {
                    releasePlayer();
                }
            }
        }
    }
//
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            if(post != null) {
                if (post.type.contains("vid")) {
                    releasePlayer();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false);

        initViewModel();
        initController();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        post = ContentFragmentArgs.fromBundle(getArguments()).getPostArg();



        binding.postName.setText(post.name);

        binding.likes.setText(post.likes + " like" + (post.likes != 1 ? "s" : ""));
        binding.comments.setText(post.comments + " comment" + (post.comments != 1 ? "s" : ""));

        binding.liked.setImageResource(post.isLiked ? R.drawable.ic_fav_true : R.drawable.ic_fav_false);
        binding.saveBtnImg.setImageResource(post.isSaved ? R.drawable.ic_download_true : R.drawable.ic_download_false);

        if (post.type.contains("img")) {
            binding.img.setVisibility(View.VISIBLE);
            Glide.with(this).load(post.link).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    binding.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    binding.progressBar.setVisibility(View.GONE);

                    if(post.type.contains("Txt")) {
                        binding.text.setText(post.text);
                        binding.text.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            }).into(binding.img);


        } else if (post.type.contains("vid")) {
            hideSystemUi();
            initExoPlayer(post.type.contains("Txt") ? post.text : "");
//            binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    binding.progressBar.setVisibility(View.GONE);
//                    binding.video.setVisibility(View.VISIBLE);
//                    binding.video.start();
//                }
//            });
        } else if (post.type.equals("article")) {
            binding.topBar.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.webView.requestFocus();
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setUseWideViewPort(true);
            binding.webView.getSettings().setLoadWithOverviewMode(true);
            binding.webView.getSettings().setSupportZoom(true);
            binding.webView.getSettings().setBuiltInZoomControls(true);
            binding.webView.getSettings().setDisplayZoomControls(false);
            binding.webView.setSoundEffectsEnabled(true);
            binding.webView.loadData("<html><head><meta charset=\"UTF-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><style>* {x margin: 0; padding: 0; box-sizing: border-box; outline: none; word-wrap: break-word; font-family: \"Poppins\"; } html, body { height: 100%; width: 100vw; } img, video { max-width: 95vw; }</style></head><body><pre style=\"white-space: normal; width:95vw; margin: auto;\">" + post.text + "</pre></body></html>",
                    "text/html", "UTF-8");
            binding.webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress == 100) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.webView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        viewModel.getAllPostLikerString(post.id, post.isLiked);
        viewModel.allPostLikerStringLiveData.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                isLikerStrLoaded = true;
                binding.liker.setText(s);

//                if(areAllCommentsLoaded) {
//                    afterDBLoad();
//                }
            }
        });

        viewModel.getPostComments(post);
        viewModel.allPostCommentsLiveData.observe(requireActivity(), new Observer<CommentsResponse>() {
            @Override
            public void onChanged(CommentsResponse commentsResponse) {
//                areAllCommentsLoaded = true;
                if (!commentsResponse.isError) {
                    comments = new ArrayList<>();
                    comments.addAll(commentsResponse.comments);

//                    if(isLikerStrLoaded) {
                    afterCommentsLoaded();
//                    }
                } else {
                    Toast.makeText(requireContext(), commentsResponse.statusMessage, Toast.LENGTH_SHORT).show();
                }

                if(viewModel.allPostCommentsLiveData.hasActiveObservers()) {
                    viewModel.allPostCommentsLiveData.removeObservers(requireActivity());
                }
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigateUp();
            }
        });

        binding.likesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setLikeOrUnlikePost(post);
                viewModel.postLikedOrUnlikedLiveData.observe(requireActivity(), new Observer<LikeResponse>() {
                    @Override
                    public void onChanged(LikeResponse response) {
                        if (response.isSuccess) {
                            post.isLiked = response.post.isLiked;
                            binding.liked.setImageResource(post.isLiked ? R.drawable.ic_fav_true : R.drawable.ic_fav_false);
                        }
                    }
                });
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.savePost(post);
                viewModel.savePostLiveData.observe(requireActivity(), new Observer<SaveResponse>() {
                    @Override
                    public void onChanged(SaveResponse saveResponse) {
                        if (!saveResponse.isError) {
                            post.isSaved = saveResponse.isSaved;
                            Toast.makeText(requireContext(), post.isSaved ? "Saved!" : "Unsaved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), saveResponse.statusMessage, Toast.LENGTH_SHORT).show();
                        }

                        binding.saveBtnImg.setImageResource(post.isSaved ? R.drawable.ic_download_true : R.drawable.ic_download_false);

                        if (viewModel.savePostLiveData.hasActiveObservers()) {
                            viewModel.savePostLiveData.removeObservers(requireActivity());
                        }
                    }
                });
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

    private void initExoPlayer(String videoText){
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        binding.video.setPlayer(player);

        Uri uri = Uri.parse(post.link);
        MediaSource mediaSource = buildMediaSource(uri);
        player.setPlayWhenReady(playWhenReady);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == Player.STATE_READY) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.video.setVisibility(View.VISIBLE);

                    if(!videoText.equals("")) {
                        binding.text.setText(videoText);
                    }
                }
            }
        });

        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(requireContext(), "top5");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        binding.video.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private void afterCommentsLoaded() {
        binding.commentsList.setHasFixedSize(true);

        binding.commentsList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        commentsListAdapter = new CommentsListAdapter(comments, new CommentsListItemClickListener() {
            @Override
            public void OnDeleteClicked(int index) {
                viewModel.deleteCommentFromPost(post.category.id, comments.get(index));
                viewModel.deleteCommentLiveData.observe(requireActivity(), new Observer<CommentsResponse>() {
                    @Override
                    public void onChanged(CommentsResponse commentsResponse) {
                        comments.remove(index);
                        commentsListAdapter.notifyDataSetChanged();

                        viewModel.deleteCommentLiveData.removeObservers(requireActivity());
                    }
                });
            }
        });
        binding.commentsList.setAdapter(commentsListAdapter);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentMsg = binding.commentInp.getText().toString().trim();
                if(commentMsg.length() > 0) {
                    viewModel.commentOnPost(post.category.id, new Comment(post, "", "", binding.commentInp.getText().toString().trim(), Calendar.getInstance().getTimeInMillis()));
                    viewModel.commentOnPostLiveData.observe(requireActivity(), new Observer<CommentsResponse>() {
                        @Override
                        public void onChanged(CommentsResponse commentsResponse) {
                            if(!commentsResponse.isError) {
                                binding.commentInp.setText("");
                                comments.addAll(commentsResponse.comments);
                                commentsListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(requireContext(), commentsResponse.statusMessage, Toast.LENGTH_SHORT).show();
                            }

                            if(viewModel.commentOnPostLiveData.hasActiveObservers()) {
                                viewModel.commentOnPostLiveData.removeObservers(requireActivity());
                            }
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid comment!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
