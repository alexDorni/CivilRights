package com.example.heroalex.copscivilslicense.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heroalex.copscivilslicense.CivilData;
import com.example.heroalex.copscivilslicense.CopMap;
import com.example.heroalex.copscivilslicense.MainActivity;
import com.example.heroalex.copscivilslicense.R;
import com.example.heroalex.copscivilslicense.ServiceLogin;
import com.example.heroalex.copscivilslicense.managers.AuthManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseFragment {

    // bind toate layout-urile
    @BindView(R.id.hello_text)
    TextView mHelloText;
    Unbinder unbinder;
    @BindView(R.id.btn_log_out)
    TextView mBtnLogOut;
    @BindView(R.id.button_notification_alert)
    Button mBtnNofiticationAlarm;

    public static TextView mEditButtVol;


    // userId pentru update automat
    public static String mUserUid;

    // coordonates
    private String mGPSCoordinates = "null";
    private String mCivilStatus = "0";
    private String mCopStatus = "-1";

    // background service
    private Intent startIntent;

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        // atribuie in mod direct fara a specifica findById
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (MainActivity.mCopBool == false) {

                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                // get uid
                mUserUid = user.getUid();

                // inregistrare in baza de date
                DatabaseReference usersRef = mRootRef.child(mUserUid);

                // adaugare in baza de date
                usersRef.setValue(new CivilData.CivilDataBuilder()
                        .firstName(user.getDisplayName())
                        .status(mCivilStatus)
                        .gps(mGPSCoordinates)
                        .build());

                mHelloText.setText("Sunteti logat cu user-ul : " + user.getDisplayName());



                // start background service
                startIntent = new Intent(getContext(), ServiceLogin.class);
                startIntent.setAction("start");
                getActivity().startService(startIntent);


                mBtnNofiticationAlarm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {


                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference usersRef = mRootRef.child(user.getUid());

                            // alarm button set
                            usersRef.child("statusPoint").setValue("1");
                        }
                    }
                });

            }
            else
                if (MainActivity.mCopBool == true) {
                    // start serviciu cop + googleMaps + notificare
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                    // get uid
                    mUserUid = user.getUid();

                    // inregistrare in baza de date
                    DatabaseReference usersRef = mRootRef.child(mUserUid);

                    // adaugare in baza de date
                    usersRef.setValue(new CivilData.CivilDataBuilder()
                            .firstName(user.getDisplayName())
                            .status(mCopStatus)
                            .gps(mGPSCoordinates)
                            .build());

                    mHelloText.setText("Sunteti logat cu user-ul : " + user.getDisplayName());


                    startActivity(new Intent(getContext(), CopMap.class));
                }
        }


        mBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthManager.getInstance().startLogOut();

                // back to login
                getActivity().getSupportFragmentManager().popBackStack();
                openFragment(new LoginFragment());

                    // stop service
                if (startIntent != null) {
                    getActivity().stopService(startIntent);
                }
            }
        });


    }

}
