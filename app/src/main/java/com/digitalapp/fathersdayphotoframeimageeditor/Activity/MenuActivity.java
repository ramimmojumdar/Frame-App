package com.digitalapp.fathersdayphotoframeimageeditor.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.digitalapp.fathersdayphotoframeimageeditor.Adapter.FragAdapter;

import com.digitalapp.fathersdayphotoframeimageeditor.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MenuActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    ImageView imageMenu;


    TabLayout tabLayout;
    ViewPager viewPager;

    FragAdapter fragAdapter;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        fragmentManager = getSupportFragmentManager();

        fragAdapter = new FragAdapter(fragmentManager);

        viewPager.setAdapter(fragAdapter);

        viewPager.setAdapter(fragAdapter);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);


        toggle = new ActionBarDrawerToggle(MenuActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.shareId:
                        Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        String link = "https://play.google.com/store/apps/details?id" + getPackageName();
                        String subject = "Photo Frame...";

                        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        startActivity(Intent.createChooser(shareIntent, "Share Using..."));
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.homePage:

                        Toast.makeText(MenuActivity.this, "Welcome Home...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        drawerLayout.closeDrawers();
                        break;
                }

                return false;
            }
        });


        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }


}
