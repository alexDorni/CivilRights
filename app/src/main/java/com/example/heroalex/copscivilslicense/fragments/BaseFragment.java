package com.example.heroalex.copscivilslicense.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heroalex.copscivilslicense.BaseActivity;
import com.example.heroalex.copscivilslicense.R;
import com.example.heroalex.copscivilslicense.utils.ViewUtils;

import java.lang.ref.WeakReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    public BaseActivity mActivity;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
    }


    public void showToastMessage(int messageResId) {
        ViewUtils.showToastMessage(mActivity, messageResId);
    }

    public void goBack() {
        if (mActivity != null) mActivity.goBack();
    }

    public void openFragment(Fragment fragment, Bundle args) {
        if (mActivity != null) mActivity.openFragment(fragment, args);
    }

    public void openFragment(Fragment fragment) {
        openFragment(fragment, new Bundle());
    }

    public void setToolbarTitle(String title) {
        if (mActivity != null) mActivity.setToolbarTitle(title);
    }

    public void showLoadingProgress() {
        if (mActivity != null) mActivity.showProgress();
    }

    public void hideLoadingProgress() {
        if (mActivity != null) mActivity.hideProgress();
    }

}
