package com.mars_tech.shehriyar.top5.view.credentials;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.mars_tech.shehriyar.top5.R;
import com.mars_tech.shehriyar.top5.databinding.FragmentSignupBinding;
import com.mars_tech.shehriyar.top5.pojo.AuthResponse;
import com.mars_tech.shehriyar.top5.pojo.User;
import com.mars_tech.shehriyar.top5.singleton.UserSingleton;
import com.mars_tech.shehriyar.top5.util.Constants;
import com.mars_tech.shehriyar.top5.viewmodel.CredentialsViewModel;

import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private NavController controller;
    private CredentialsViewModel viewModel;

    private UserSingleton userSingleton = UserSingleton.getInstance();

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);

        controller = NavHostFragment.findNavController(this);

        initViewModel();
        initClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel.authenticatedUserLiveData != null && viewModel.authenticatedUserLiveData.hasActiveObservers()) {
            viewModel.authenticatedUserLiveData.removeObservers(requireActivity());
        }
    }

    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CredentialsViewModel.class);
    }

    void initClickListeners() {
        binding.existingAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment(), new NavOptions.Builder().setPopUpTo(R.id.startFragment, false).build());
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    showOverlay();
                    viewModel.signUpWithCredentials(new User(binding.usernameInp.getText().toString().trim(), binding.emailInp.getText().toString().trim(), binding.passwordInp.getText().toString().trim()));
                    viewModel.authenticatedUserLiveData.observe(requireActivity(), new Observer<AuthResponse>() {
                        @Override
                        public void onChanged(AuthResponse authResponse) {
                            hideOverlay();
                            if(authResponse.isError) {
                                showError(authResponse.statusMessage);
                            } else {
                                userSingleton.currentUser = authResponse.user;
                                controller.navigate(SignupFragmentDirections.actionSignupFragmentToMainActivity());
                            }
                            viewModel.authenticatedUserLiveData.removeObservers(requireActivity());
                        }
                    });
                }
//                controller.navigate(R.id.action_signupFragment_to_mainActivity);
            }
        });
    }

    private boolean validateForm() {
        boolean isUsernameValid = validateUsernameInput();
        boolean isEmailValid = validateEmailInput();
        boolean isPasswordValid = validatePasswordInput();
        return isUsernameValid && isEmailValid && isPasswordValid;
    }

    private boolean validateUsernameInput() {
        String nameVal = binding.usernameInp.getText().toString().trim();
        boolean isValid = nameVal.length() > 0;
        binding.usernameInpWarning.setVisibility(isValid ? View.GONE : View.VISIBLE);
        return isValid;
    }

    private boolean validateEmailInput() {
        String emailVal = binding.emailInp.getText().toString().trim();
        Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
        boolean isValid = pattern.matcher(emailVal).matches();
        binding.emailInpWarning.setVisibility(isValid ? View.GONE : View.VISIBLE);
        return isValid;
    }

    private boolean validatePasswordInput() {
        String passwordVal = binding.passwordInp.getText().toString().trim();
        boolean isValid = passwordVal.length() >= 6;
        binding.passwordInpWarning.setVisibility(isValid ? View.GONE : View.VISIBLE);
        return isValid;
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
