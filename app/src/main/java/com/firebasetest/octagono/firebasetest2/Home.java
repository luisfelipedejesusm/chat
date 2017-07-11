package com.firebasetest.octagono.firebasetest2;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.firebasetest.octagono.firebasetest2.Fragments.HomeFragments.ChatsFragment;
import com.firebasetest.octagono.firebasetest2.Fragments.HomeFragments.UsersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.firebasetest.octagono.firebasetest2.CustomAdapters.ViewPageAdapter;

public class Home extends AppCompatActivity {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent newIntent = new Intent(this, Settings.class);
                startActivity(newIntent);
                break;
            default:
                break;
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void LogoutAccount(View view){
        fAuth.signOut();
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }
}
