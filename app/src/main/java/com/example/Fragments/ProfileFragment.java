package com.example.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditcard.R;
import com.example.creditcard.ReadWriteUserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView tVWc, tVFNam, tVEl, tViDoB, tViGender, tViMob;
    private ProgressBar proB;
    private String fn, el, doB, Gend, Mobil;
    private ImageView imgV;
    private FirebaseAuth firebAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tVWc = view.findViewById(R.id.shw_wlcm);
        tVFNam = view.findViewById(R.id.shw_f_nam);
        tVEl = view.findViewById(R.id.shw_eml);
        tViDoB = view.findViewById(R.id.shw_dob);
        tViGender = view.findViewById(R.id.shw_gend);
        tViMob = view.findViewById(R.id.shw_mob);
        proB = view.findViewById(R.id.progrb);

        firebAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebAuth.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "Something went wrong! User's Details are not available at the moment",
                    Toast.LENGTH_LONG).show();
        } else {
            proB.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        // Extracting User Reference from Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    fn = firebaseUser.getDisplayName();
                    el = firebaseUser.getEmail();
                    doB = readWriteUserDetails.dob;
                    Gend = readWriteUserDetails.gender;
                    Mobil = readWriteUserDetails.mobile;

                    tVWc.setText("Welcome, " + fn + "!");
                    tVFNam.setText(fn);
                    tVEl.setText(el);
                    tViDoB.setText(doB);
                    tViGender.setText(Gend);
                    tViMob.setText(Mobil);
                }
                proB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                proB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
