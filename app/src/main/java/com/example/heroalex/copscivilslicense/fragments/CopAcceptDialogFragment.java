package com.example.heroalex.copscivilslicense.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.heroalex.copscivilslicense.CopMap;
import com.example.heroalex.copscivilslicense.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hero Alex on 7/3/2018.
 */

public class CopAcceptDialogFragment extends DialogFragment{
    @Override
    public void dismiss() {
        Toast.makeText(getActivity(), "Este cineva asignat", Toast.LENGTH_SHORT).show();
        super.dismiss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (CopMap.unickDialogStatic) {
            dismiss();
            return builder.create();
        } else {
            builder.setMessage(R.string.dialog_cop)
                    .setPositiveButton(R.string.dialog_cop_accept, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            FirebaseUser userCop = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                            mRootRef.child(CopMap.globalUidCivil).child("firstName").setValue(userCop.getUid(),new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Log.d("Data not saved. ", databaseError.getMessage());
                                    } else {
                                        Log.d("Data saved ", "Success");
                                    }
                                }

                            });

                            mRootRef.child(CopMap.globalUidCivil).child("statusPoint").setValue("2", new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Log.d("Data not saved. ", databaseError.getMessage());
                                    } else {
                                        Log.d("Data saved ", "Success");
                                    }
                                }

                            });
                            if (!TextUtils.isEmpty(userCop.getUid())) {

                                Log.d("SuccUpdateChild", userCop.getUid());
                                mRootRef.child(userCop.getUid()).child("statusPoint").setValue("-2", new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Log.d("Data not saved. ", databaseError.getMessage());
                                        } else {
                                            Log.d("Data saved ", "Success");
                                        }
                                    }
                                });

                                mRootRef.child(userCop.getUid()).child("firstName").setValue(CopMap.globalUidCivil, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Log.d("Data not saved. ", databaseError.getMessage());
                                        } else {
                                            Log.d("Data saved ", "Success");
                                        }
                                    }

                                });
                            } else {
                                Log.d("failUpdateChild", userCop.getUid());
                            }


                        }
                    });
            return builder.create();

        }
    }
}
