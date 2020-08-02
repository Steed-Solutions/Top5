package com.mars_tech.shehriyar.top5.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mars_tech.shehriyar.top5.pojo.AuthResponse;
import com.mars_tech.shehriyar.top5.pojo.User;

public class CredentialsModel {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

    public MutableLiveData<User> checkIfUserIsAuthenticatedInFirebase() {
        final MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        User user = new User();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {

            user.isAuthenticated = false;
            authenticatedUserMutableLiveData.setValue(user);
            Log.d("USER", "WTH NO");
        } else {
            Log.d("USER", firebaseUser.getDisplayName());
            user.uid = firebaseUser.getUid();
            user.name = firebaseUser.getDisplayName();
            user.email = firebaseUser.getEmail();
            user.isAuthenticated = true;
            authenticatedUserMutableLiveData.setValue(user);
        }
        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<AuthResponse> firebaseSignInWithCredentials(User credentials) {
        final MutableLiveData<AuthResponse> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(credentials.email, credentials.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> authTask) {
                AuthResponse authResponse = new AuthResponse();
                if (authTask.isSuccessful()) {
                    boolean isNewUser = authTask.getResult().getAdditionalUserInfo().isNewUser();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        User user = new User();
                        user.uid = firebaseUser.getUid();
                        user.name = firebaseUser.getDisplayName();
                        user.email = firebaseUser.getEmail();
                        user.isNew = isNewUser;
                        authResponse.user = user;
                        authenticatedUserMutableLiveData.setValue(authResponse);
                    }
                } else {
                    authResponse.isError = true;
                    authResponse.statusMessage = authTask.getException().getMessage();
                    authenticatedUserMutableLiveData.setValue(authResponse);
                }
            }
        });
        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<AuthResponse> firebaseSignUpWithCredentials(final User credentials) {
        final MutableLiveData<AuthResponse> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(credentials.email, credentials.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> authTask) {
                final AuthResponse authResponse = new AuthResponse();
                if (authTask.isSuccessful()) {
                    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(credentials.name).build();
                        final User user = new User();
                        user.uid = firebaseUser.getUid();
                        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.name = firebaseUser.getDisplayName();
                                    user.email = firebaseUser.getEmail();
                                    user.isNew = true;
                                    authResponse.user = user;

                                    firebaseDatabase.child("users").child(user.uid).setValue(user.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                authenticatedUserMutableLiveData.setValue(authResponse);
                                            } else {
                                                authResponse.isError = true;
                                                authResponse.statusMessage = task.getException().getMessage();
                                                authenticatedUserMutableLiveData.setValue(authResponse);
                                            }
                                        }
                                    });
                                } else {
                                    authResponse.isError = true;
                                    authResponse.statusMessage = task.getException().getMessage();
                                    authenticatedUserMutableLiveData.setValue(authResponse);
                                }
                            }
                        });
                    }
                } else {
                    authResponse.isError = true;
                    authResponse.statusMessage = authTask.getException().getMessage();
                    authenticatedUserMutableLiveData.setValue(authResponse);
                }
            }
        });
        return authenticatedUserMutableLiveData;
    }

}
