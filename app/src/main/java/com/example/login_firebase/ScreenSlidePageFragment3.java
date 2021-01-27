package com.example.login_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ScreenSlidePageFragment3 extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLE-SIGNIN";
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnLoginGoogle, btnLoginFacebook, btnLogin;
    private TextInputEditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page3, container, false);

        // Initialize Firebase Auth
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        btnLoginGoogle = (Button) rootView.findViewById(R.id.btn_login_google);
        btnLoginFacebook = (Button) rootView.findViewById(R.id.btn_login_facebook);

        inputEmail = (TextInputEditText) rootView.findViewById(R.id.input_email);
        inputPassword = (TextInputEditText) rootView.findViewById(R.id.input_password);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        btnLoginGoogle.setOnClickListener(v -> {
            googleSignIn();
        });

        btnLoginFacebook.setOnClickListener(v -> {

        });

        btnLogin.setOnClickListener(v -> {
            String email = "", password = "";
            if (inputEmail.getText() != null && !inputEmail.getText().equals("")) {
                email = inputEmail.getText().toString();
            }
            if (inputPassword.getText() != null && !inputPassword.getText().equals("")) {
                password = inputPassword.getText().toString();
            }
            emailPasswordSignIn(email, password);
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) {
            String email = account.getEmail();
            Toast.makeText(getContext(), email == null ? "" : email, Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString("name", account.getDisplayName());
            bundle.putString("email", account.getEmail());
            bundle.putString("provider", "Google");


            startActivity(new Intent(getContext(), MainActivity.class).putExtras(bundle));
        }

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            Toast.makeText(getContext(), email == null ? "" : email, Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString("name", currentUser.getDisplayName());
            bundle.putString("email", currentUser.getEmail());
            bundle.putString("provider", currentUser.getProviderId());


            startActivity(new Intent(getContext(), MainActivity.class).putExtras(bundle));
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void emailPasswordSignIn(String email, String password) {
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "LoginWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userEmail = user.getEmail();
                    Toast.makeText(getContext(), userEmail == null ? "" : userEmail, Toast.LENGTH_LONG).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", user.getDisplayName());
                    bundle.putString("email", user.getEmail());
                    bundle.putString("provider", user.getProviderId());


                    startActivity(new Intent(getContext(), MainActivity.class).putExtras(bundle));

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();

                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(getContext(), "Welcome " + account.getDisplayName(), Toast.LENGTH_LONG).show();


            Bundle bundle = new Bundle();
            bundle.putString("name", account.getDisplayName());
            bundle.putString("email", account.getEmail());
            bundle.putString("provider", "Google");


            startActivity(new Intent(getContext(), MainActivity.class).putExtras(bundle));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
