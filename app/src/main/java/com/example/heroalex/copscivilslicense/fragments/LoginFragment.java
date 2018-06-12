package com.example.heroalex.copscivilslicense.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heroalex.copscivilslicense.R;
import com.example.heroalex.copscivilslicense.managers.AuthManager;
import com.example.heroalex.copscivilslicense.utils.DataUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {


    @BindView(R.id.sign_up_email)
    EditText mEmailField;
    @BindView(R.id.layout_sing_up_email)
    TextInputLayout mLayoutEmail;
    @BindView(R.id.input_password_register)
    EditText mInputPasswordField;
    @BindView(R.id.layout_input_password)
    TextInputLayout mLayoutPassword;
    @BindView(R.id.button_registration)
    Button mBtnRegistration;
    @BindView(R.id.btn_text_forgot_password)
    TextView mBtnTextForgotPassword;
    @BindView(R.id.btn_text_register)
    TextView mBtnTextRegister;
    Unbinder unbinder;

    private AuthManager mAuthManager;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mAuthManager = AuthManager.getInstance();
        if (mAuthManager.isUserSignedIn()) openFragment(new IndexFragment());

    }

    private void initView() {
        mBtnRegistration.setOnClickListener(mClickListener);
        mBtnTextForgotPassword.setOnClickListener(mClickListener);
        mBtnTextRegister.setOnClickListener(mClickListener);
    }

    private boolean validateFields() {
        clearErrors();
        boolean isValid = true;
        if (!DataUtils.isEmailValid(mEmailField.getText().toString())) {
            isValid = false;
            mLayoutEmail.setError(getString(R.string.email_empty_error));
            mLayoutEmail.setErrorEnabled(true);
        }
        if (!DataUtils.isPasswordValid(mInputPasswordField.getText().toString())) {
            isValid = false;
            mLayoutPassword.setError(getString(R.string.password_too_short_error));
            mLayoutPassword.setErrorEnabled(true);
        }

        return isValid;
    }

    private void clearErrors() {
        mLayoutEmail.setError("");
        mLayoutEmail.setErrorEnabled(false);
        mLayoutPassword.setError("");
        mLayoutPassword.setErrorEnabled(false);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_registration:
                    if (validateFields()) {
                        showLoadingProgress();
                        mAuthManager.startUserLogin(mEmailField.getText().toString(), mInputPasswordField.getText().toString(), mLoginCompleteListener, mLoginFailureListener);
                    }
                    break;
                case R.id.btn_text_register:
                    getActivity().getSupportFragmentManager().popBackStack(); // maybe null check ... removing current fragment
                    openFragment(new RegisterFragment());
                    break;
                case R.id.btn_text_forgot_password:
                    openFragment(new ResetPasswordFragment()); // not removing fragment so we can navigate back
                    break;
            }
        }
    };

    private OnCompleteListener<AuthResult> mLoginCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            hideLoadingProgress();
            if (task.isSuccessful()) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack(); // remove current fragment
                    openFragment(new IndexFragment());
                }
            } else {
                showToastMessage(R.string.auth_error_invalid_credentials);
            }

        }
    };

    private OnFailureListener mLoginFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            hideLoadingProgress();
            showToastMessage(R.string.generic_error_message);
        }
    };
}
