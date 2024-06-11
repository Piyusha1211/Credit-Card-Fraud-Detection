package com.example.creditcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.Fragments.HelperFragment;
import com.example.Fragments.HomeFragment;
import com.example.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView btmNavignVw;
    private FrameLayout frmLat;
    private FirebaseAuth athProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tolbr);
        setSupportActionBar(toolbar);

        btmNavignVw = findViewById(R.id.BtmNav);
        frmLat = findViewById(R.id.frag_contr);
        athProf = FirebaseAuth.getInstance();

        btmNavignVw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.nav_Helper) {
                    loadFragment(new HelperFragment(), false);
                } else if (itemId == R.id.nav_Profile) {
                    loadFragment(new ProfileFragment(), false);
                }

                return true;
            }
        });

        // Load the default fragment
        loadFragment(new HomeFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frag_contr, fragment);
        } else {
            fragmentTransaction.replace(R.id.frag_contr, fragment);
        }

        fragmentTransaction.commit();
    }

    // Create an Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    // When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            // Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
            return true;
        } else if (id == R.id.menu_logout) {
            athProf.signOut();
            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Login.class);

            // After logging out, remove the stack to stop users from returning to their profile activity by using the back button
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close Profile Activity
            return true;
        } else {
            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
        }
    }
}
