package com.example.login_firebase;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ScreenSlidePageFragment2 extends Fragment {

    private Button btnSignup, btnLogin;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        public void changeFragment(int id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page2, container, false);

        btnSignup = rootView.findViewById(R.id.btn_signup);
        btnLogin = rootView.findViewById(R.id.btn_login);

        btnSignup.setOnClickListener(v -> {
            mListener.changeFragment(0);
        });

        btnLogin.setOnClickListener(v -> {
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
}
