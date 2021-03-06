package com.example.mad_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    Button inquiries, solutions, profile, setting, help, signout;
    TextView profileName, profileUsername;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_profile, container, false);

       profile = v.findViewById(R.id.profile);
       setting = v.findViewById(R.id.settings);
       solutions = v.findViewById(R.id.solutions);
       inquiries = v.findViewById(R.id.inquiries);
       profileName = v.findViewById(R.id.profileName);
       profileUsername = v.findViewById(R.id.profileUsername);
       signout = v.findViewById(R.id.signout);

       bundle = this.getArguments();
       String username = null;

       if (bundle != null){
           username = bundle.getString("username");
       }

        DatabaseReference reference = FirebaseDatabase.getInstance ( "https://kuppiya-mad-default-rtdb.asia-southeast1.firebasedatabase.app" ).getReference ( "users" );

        Query checkUser = reference.orderByChild ("username").equalTo(username);

        String finalBundleUsername = username;
        checkUser.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists ( )) {

                    String usernameFromDB = snapshot.child ( finalBundleUsername ).child ( "username" ).getValue ( String.class );
                    String nameFromDB = snapshot.child ( finalBundleUsername ).child ( "name" ).getValue ( String.class );
                    String emailFromDB = snapshot.child ( finalBundleUsername ).child ( "email" ).getValue ( String.class );
                    String dobFromDB = snapshot.child ( finalBundleUsername ).child ( "dob" ).getValue ( String.class );
                    String genderFromDB = snapshot.child ( finalBundleUsername ).child ( "gender" ).getValue ( String.class );
                    String statusFromDB = snapshot.child ( finalBundleUsername ).child ( "employment" ).getValue ( String.class );

                    Bundle bundle = new Bundle ( );
                    bundle.putString ( "username" , usernameFromDB );
                    bundle.putString ( "name" , nameFromDB );
                    bundle.putString ( "email" , emailFromDB );
                    bundle.putString ( "dob" , dobFromDB );
                    bundle.putString ( "gender" , genderFromDB );
                    bundle.putString ( "status" , statusFromDB );

                    profileName.setText(nameFromDB);
                    profileUsername.setText(usernameFromDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UserProfile();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Settings();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });

        solutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_container, new MySolutionsFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        inquiries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.dashboard_container, new MyInquiriesFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

       return v;
    }


}