package com.mars_tech.shehriyar.top5.view.credentials;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.core.Platform;
import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.pojo.User;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;
import com.mars_tech.shehriyar.top5.viewmodel.CredentialsViewModel;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    private NavController controller;
    private CredentialsViewModel viewModel;

    private Handler handler = new Handler();

    private UserSingleton userSingleton = UserSingleton.getInstance();

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        initController();
        initViewModel();

        // creating timer task, timer
        TimerTask tasknew = new TimerScheduleDelay();
        Timer timer = new Timer();

        // scheduling the task at interval
        timer.schedule(tasknew, 1000);


        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    private class TimerScheduleDelay extends TimerTask {
        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    viewModel.checkIfUserIsAuthenticated();
                    viewModel.userCheckLiveData.observe(requireActivity(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            Log.d("UMM WHAT", "NO");
                            if (user.isAuthenticated) {
                                userSingleton.currentUser = user;
                                Log.d("USER_NEW", "" + userSingleton.currentUser.isNew);
                                controller.navigate(SplashFragmentDirections.actionSplashFragmentToMainActivity());
                            } else {
                                controller.navigate(SplashFragmentDirections.actionSplashFragmentToStartFragment(), new NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build());
                            }
                        }
                    });
                }
            });
        }
    }

    public void run() {
        System.out.println("timer working");
    }

    private void initController() {
        controller = NavHostFragment.findNavController(this);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CredentialsViewModel.class);
    }

}
