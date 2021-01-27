package com.example.login_firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ScreenSlidePageFragment1 extends Fragment {

    private TextInputEditText inputPassword, inputConfirmPassword, inputEmail;
    private Button btnSignup;
    private TextView tvHaveAccount;
    private FirebaseAuth mAuth;
    private static final String TAG = "GOOGLE-SIGNIN";

    private OnFragmentInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page1, container, false);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        inputPassword = (TextInputEditText) rootView.findViewById(R.id.input_password);
        inputConfirmPassword = (TextInputEditText) rootView.findViewById(R.id.input_confirm_password);
        inputEmail = (TextInputEditText) rootView.findViewById(R.id.input_email);

        btnSignup = (Button) rootView.findViewById(R.id.btn_signup);

        tvHaveAccount = (TextView) rootView.findViewById(R.id.tv_forgot_password);


        btnSignup.setOnClickListener(v -> {

            String email = "", password = "", confirmPassword = "";
            if (inputEmail.getText() != null && !inputEmail.getText().equals("")) {
                email = inputEmail.getText().toString();
            }
            if (inputPassword.getText() != null && !inputPassword.getText().equals("")) {
                password = inputPassword.getText().toString();
            }
            if (inputConfirmPassword.getText() != null && !inputConfirmPassword.getText().equals("")) {
                confirmPassword = inputConfirmPassword.getText().toString();
            }

            emailPasswordRegister(email, password, confirmPassword);

        });

        tvHaveAccount.setOnClickListener(v -> {
            mListener.changeFragment(2);
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    private void emailPasswordRegister(String email, String password, String confirmPassword) {
        try {

            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("password and confirmPassword not are equal");
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

                            Bundle bundle = new Bundle();
                            bundle.putString("name", user.getDisplayName());
                            bundle.putString("email", user.getEmail());
                            bundle.putString("provider", user.getProviderId());


                            startActivity(new Intent(getContext(), MainActivity.class).putExtras(bundle));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }

                    });
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
