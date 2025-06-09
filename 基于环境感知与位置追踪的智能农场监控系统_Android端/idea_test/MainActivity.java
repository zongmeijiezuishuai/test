package com.example.idea_test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.idea_test.fragment.AnimalManagementFragment;
import com.example.idea_test.fragment.Beforedata;
import com.example.idea_test.fragment.DataFragment;
import com.example.idea_test.fragment.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DataFragment())
                .commit();
        // 默认加载地图Fragment
        loadFragment(new MapFragment(),"MapFragment");

        // 默认加载 DataFragment，并设置 tag
       // loadFragment(new DataFragment(), "DataFragment");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String tag = null;
                switch (item.getItemId()) {
                    case R.id.nav_map:
                        selectedFragment = new MapFragment();
                        tag = "MapFragment";
                        break;
                    case R.id.nad_data:
                        selectedFragment = new DataFragment();
                        tag = "DataFragment";
                        break;
                    case R.id.before_data:
                        selectedFragment=new Beforedata();
                        tag = "Beforedata";
                        break;
                    case R.id.animal:
                        selectedFragment=new AnimalManagementFragment();
                        tag = "AnimalManagementFragment";
                        break;
                }

               // loadFragment(selectedFragment);

                if (selectedFragment != null) {
                    loadFragment(selectedFragment, tag);
                }
                return true;
            };

//    private void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }
        private void loadFragment(Fragment fragment, String tag) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .commit();
        }
}