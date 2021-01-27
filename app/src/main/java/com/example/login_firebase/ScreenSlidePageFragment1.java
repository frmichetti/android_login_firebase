package com.example.login_firebase;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;



import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class ScreenSlidePageFragment1 extends Fragment {

    private TextInputEditText inputPassword, inputConfirmPassword, inputEmail;
    private Button btnSignup;
    private TextView tvHaveAccount;
    private FirebaseAuth mAuth;
    private static final String TAG = "GOOGLE-SIGNIN";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page1, container, false);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        TextInputEditText inputPassword = (TextInputEditText) rootView.findViewById(R.id.input_password);
        TextInputEditText inputConfirmPassword = (TextInputEditText) rootView.findViewById(R.id.input_confirm_password);
        TextInputEditText inputEmail = (TextInputEditText) rootView.findViewById(R.id.input_email);

        Button btnSignup = (Button) rootView.findViewById(R.id.btn_signup);

        TextView tvHaveAccount = (TextView) rootView.findViewById(R.id.tv_have_an_account);


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

        });


        return rootView;
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
                            Toast.makeText(getContext(), user.getEmail(), Toast.LENGTH_SHORT).show();

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
