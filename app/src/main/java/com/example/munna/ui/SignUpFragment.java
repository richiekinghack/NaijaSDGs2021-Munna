package com.example.munna.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.munna.MainActivity;
import com.example.munna.NetworkUtils;
import com.example.munna.R;
import com.example.munna.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextInputEditText email,password,confirmedPassword;
    private TextInputLayout passwordLayout,confirmedPasswordLayout;
    private Button signUpButton;
    private static final String TAG = "signUp";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);
        //firebase Instance
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        //View Instances
        signUpButton = root.findViewById(R.id.signUpButton);
        email = root.findViewById(R.id.email_input_signUp);
        confirmedPassword = root.findViewById(R.id.confirm_password_input_signUp);
        password = root.findViewById(R.id.password_input_signUp);
        passwordLayout = root.findViewById(R.id.password_input_layout_signUp);
        confirmedPasswordLayout = root.findViewById(R.id.confirm_password_input_layout_signUp);
        progressBar = root.findViewById(R.id.progressBar_signUp);
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpButton.setOnClickListener(view1 -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            String confirmedPasswordText = confirmedPassword.getText().toString();
            if (!TextUtils.isEmpty(emailText)
                    && !TextUtils.isEmpty(passwordText) && !TextUtils.isEmpty(confirmedPasswordText))
            {
                if (passwordRequirement(passwordText)){
                    passwordLayout.setErrorEnabled(false);
                    if (Objects.equals(confirmedPasswordText, passwordText)){
                        confirmedPasswordLayout.setErrorEnabled(false);
                        passwordLayout.setErrorEnabled(false);
                        if (NetworkUtils.isNetworkConnected(getContext())){
                            progressBar.setVisibility(View.VISIBLE);
                            requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            signUp(emailText,passwordText);
                            progressBar.setVisibility(View.GONE);
                            requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }else {
                            Snackbar.make(view1, "Internet connection failed", Snackbar.LENGTH_LONG).show();
                        }
                    }else{
                        confirmedPasswordLayout.setErrorEnabled(true);
                        confirmedPasswordLayout.setErrorIconDrawable(0);
                        confirmedPasswordLayout.setError("Password not the same");
                    }
                }else{
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setErrorIconDrawable(0);
                    passwordLayout.setError("Use a minimum of 8 characters with a mix of letters and numbers");
                }
            }else {
                Snackbar.make(view1,"Fill All Fields", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    private void signUp(final String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser currentUser = auth.getCurrentUser();
                        saveUser(email);
                        updateUI(currentUser);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    private void saveUser(String email) {
        String userID = auth.getCurrentUser().getUid();
        User user = new User(userID,email);
        databaseReference.child("Users").child(userID).setValue(user);
    }

    public boolean passwordRequirement(String s) {
        String n = ".*[0-9].*";
        String a = ".*[a-z].*";
        return s.matches(n) && s.matches(a);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }else {

        }
    }
}