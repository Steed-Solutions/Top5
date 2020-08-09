package com.mars_tech.shehriyar.top5.view.main;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

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
import com.mars_tech.shehriyar.top5.databinding.FragmentContentBinding;
import com.mars_tech.shehriyar.top5.pojo.Post;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private FragmentContentBinding binding;
    private NavController controller;

    private Post post;

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
        initController();

        post = ContentFragmentArgs.fromBundle(getArguments()).getPostArg();



        binding.postName.setText(post.name);

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
                    return false;
                }
            }).into(binding.img);
        } else if (post.type.contains("vid")) {
            hideSystemUi();
            initExoPlayer();
//            binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    binding.progressBar.setVisibility(View.GONE);
//                    binding.video.setVisibility(View.VISIBLE);
//                    binding.video.start();
//                }
//            });
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigateUp();
            }
        });

        return binding.getRoot();
    }

    private void initController() {
        controller = NavHostFragment.findNavController(this);
    }

    private void initExoPlayer(){
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

}
