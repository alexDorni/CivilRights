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

import com.example.heroalex.copscivilslicense.R;
import com.example.heroalex.copscivilslicense.managers.AuthManager;
import com.example.heroalex.copscivilslicense.utils.DataUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends BaseFragment {

    @BindView(R.id.sign_up_email)
    EditText mEmailField;
    @BindView(R.id.layout_sing_up_email)
    TextInputLayout mLayoutEmail;
    @BindView(R.id.button_reset_password)
    Button mBtnResetPassword;
    Unbinder unbinder;

    private AuthManager mAuthManager;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
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
        mAuthManager = AuthManager.getInstance();
        initView();
    }

    private void initView() {
        mBtnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataUtils.isEmailValid(mEmailField.getText().toString())) {
                    mLayoutEmail.setErrorEnabled(false);
                    mLayoutEmail.setError("");
                    showLoadingProgress();
                    mAuthManager.startUserResetPassword(mEmailField.getText().toString(), mResetPasswordCompleteListener, mResetFailureListener);
                } else {
                    mLayoutEmail.setError(getString(R.string.email_empty_error));
                    mLayoutEmail.setErrorEnabled(true);
                }
            }
        });
    }

    private OnCompleteListener<Void> mResetPasswordCompleteListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            hideLoadingProgress();
            if (task.isSuccessful()) {
                showToastMessage(R.string.auth_password_reset_change);
            } else {
                showToastMessage(R.string.generic_error_message);
            }
        }
    };

    private OnFailureListener mResetFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            hideLoadingProgress();
            showToastMessage(R.string.generic_error_message);
        }
    };
}
