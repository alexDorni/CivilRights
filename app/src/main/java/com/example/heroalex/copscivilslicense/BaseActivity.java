package com.example.heroalex.copscivilslicense;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.heroalex.copscivilslicense.fragments.LoginFragment;

import java.lang.ref.WeakReference;

public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout mLoadingLayout;
    public BaseActivity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.toolbar);
        mLoadingLayout = findViewById(R.id.loading_screen);

        // must be null
        toolbar.setTitle("");

        // pune toolbar-ul in pagina
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // back-ul
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mActivity = this;
        openFragment(new LoginFragment());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        } else {
            finish();
        }
    }

    public void openFragment(Fragment fragment, Bundle args) {
        if (fragment == null) {
            finish();
            return;
        }


        // args orice
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void openFragment(Fragment fragment) {
        openFragment(fragment, new Bundle());
    }

    public void showProgress() {
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }
}
