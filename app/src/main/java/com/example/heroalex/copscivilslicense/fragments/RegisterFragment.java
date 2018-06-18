package com.example.heroalex.copscivilslicense.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
public class RegisterFragment extends BaseFragment {


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
    @BindView(R.id.input_first_name)
    EditText mFirstNameField;
    @BindView(R.id.layout_input_first_name)
    TextInputLayout mLayoutFirstName;
    @BindView(R.id.btn_text_login)
    TextView mBtnTextLogin;
    Unbinder unbinder;

    private AuthManager mAuthManager;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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
    }

    private void initView() {
        mBtnRegistration.setOnClickListener(mClickListener);
        mBtnTextLogin.setOnClickListener(mClickListener);
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
        if (TextUtils.isEmpty(mFirstNameField.getText().toString())) {
            isValid = false;
            mLayoutFirstName.setError(getString(R.string.name_empty_error));
            mLayoutFirstName.setErrorEnabled(true);
        }

        return isValid;
    }

    private void clearErrors() {
        mLayoutEmail.setError("");
        mLayoutEmail.setErrorEnabled(false);
        mLayoutPassword.setError("");
        mLayoutPassword.setErrorEnabled(false);
        mLayoutFirstName.setError("");
        mLayoutFirstName.setErrorEnabled(false);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_registration:
                    if (validateFields()) {
                        showLoadingProgress();
                        mAuthManager.startUserRegister(mEmailField.getText().toString(), mInputPasswordField.getText().toString(), mRegisterCompleteListener, mRegisterFailureListener);

                    }
                    break;
                case R.id.btn_text_login:
                    getActivity().getSupportFragmentManager().popBackStack(); // maybe null check ... removing current fragment
                    openFragment(new LoginFragment());
                    break;
            }
        }
    };

    private OnCompleteListener<AuthResult> mRegisterCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (getActivity() == null) return;
            hideLoadingProgress();
            if (task.isSuccessful()) {
                mAuthManager.updateUserName(mFirstNameField.getText().toString(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStack(); // remove current fragment

                                openFragment(new IndexFragment());
                            }
                        }
                        else {
                            showToastMessage(R.string.generic_error_message);
                        }
                    }
                });
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStack(); // remove current fragment

                    openFragment(new IndexFragment());
                }
            }
            else {
                showToastMessage(R.string.generic_error_message);
            }
        }
    };

    private OnFailureListener mRegisterFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            hideLoadingProgress();
            showToastMessage(R.string.generic_error_message);
        }
    };
}
