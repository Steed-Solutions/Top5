package com.mars_tech.shehriyar.top5.view.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.ActivityMainBinding;
import com.mars_tech.shehriyar.top5.pojo.User;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;
import com.mars_tech.shehriyar.top5.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController controller;
    private MainViewModel viewModel;

    private UserSingleton userSingleton = UserSingleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        controller = Navigation.findNavController(this, R.id.main_app_nav_host_fragment);

        initViewModel();

        binding.greetingsUser.setText("HEY " + userSingleton.currentUser.name.toUpperCase() + "!");

        NavigationUI.setupWithNavController(binding.footer, controller);

        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getLabel().equals("UserPrefFragment")) {
                    binding.footer.setVisibility(View.GONE);
                    binding.filterBtn.setVisibility(View.INVISIBLE);
                    binding.userBtn.setVisibility(View.INVISIBLE);
                } else {
                    binding.filterBtn.setVisibility(View.VISIBLE);
                    binding.userBtn.setVisibility(View.VISIBLE);

                    if (destination.getLabel().equals("FiltersFragment")) {
                        binding.footer.setVisibility(View.GONE);
                        binding.userBtn.setImageResource(R.drawable.user);
                        binding.filterBtn.setImageResource(R.drawable.filter_selected);
                        binding.logoutTxt.setVisibility(View.INVISIBLE);
                    } else if (destination.getLabel().equals("ProfileFragment")) {
                        binding.footer.setVisibility(View.GONE);
                        binding.logoutTxt.setVisibility(View.VISIBLE);
                        binding.filterBtn.setImageResource(R.drawable.filter);
                        binding.userBtn.setImageResource(R.drawable.user_selected);
                    } else {
                        binding.footer.setVisibility(View.VISIBLE);
                        binding.filterBtn.setImageResource(R.drawable.filter);
                        binding.userBtn.setImageResource(R.drawable.user);
                        binding.logoutTxt.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        binding.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller.getCurrentDestination().getLabel().equals("HomeFragment")) {
                    controller.navigate(R.id.action_homeFragment_to_filtersFragment);
                } else if (controller.getCurrentDestination().getLabel().equals("BrowseFragment")) {
                    controller.navigate(R.id.action_browseFragment_to_filtersFragment);
                }
            }
        });

        binding.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDestinationLabel = controller.getCurrentDestination().getLabel().toString();
                if (currentDestinationLabel.equals("HomeFragment")) {
                    controller.navigate(R.id.action_homeFragment_to_profileFragment);
                } else if (currentDestinationLabel.equals("BrowseFragment")) {
                    controller.navigate(R.id.action_browseFragment_to_profileFragment);
                } else if (currentDestinationLabel.equals("ProfileFragment")) {
                    viewModel.signOutUser();
                    viewModel.signOutLiveData.observe(MainActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            controller.navigate(ProfileFragmentDirections.actionProfileFragmentToCredentialsActivity());
                        }
                    });
                }
            }
        });
    }

    void initViewModel(){
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }
}
