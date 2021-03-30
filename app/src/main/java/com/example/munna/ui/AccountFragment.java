package com.example.munna.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.munna.MainActivity;
import com.example.munna.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    private Button signUpbtn,signInbtn;
    private FirebaseAuth auth;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        signInbtn = root.findViewById(R.id.login);
        signUpbtn = root.findViewById(R.id.getStarted);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInbtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_accountFragment_to_loginFragment);
        });
        signUpbtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_accountFragment_to_signUpFragment);
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
    }
}