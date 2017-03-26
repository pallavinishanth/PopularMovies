package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Adapter adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
//        adapter.addFragment(new PopularFragment(), "POPULAR");
//        adapter.addFragment(new TopRatedFragment(), "TOP RATED");
//        adapter.addFragment(new FavMovieFragment(), "FAVORITES");
//          adapter.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // without this listener the tabs would still get updated when fragments are swiped, but ....  (read the next comment)
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "tabSelected:  "
                        + tab.getText() + tab.getPosition(), Toast.LENGTH_SHORT).show();
                // no where in the code it is defined what will happen when tab is tapped/selected by the user
                // this is why the following line is necessary
                // we need to manually set the correct fragment when a tab is selected/tapped
                // and this is the problem in your code
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "tabReSelected:  " + tab.getText(), Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(tab.getPosition());
            // Reload your recyclerView here
            }
        });
//        if (savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MovieFragment())
//                    .commit();
//        }

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public static class Adapter extends FragmentPagerAdapter {

        private String Titles[] = new String[]{"POPULAR", "TOP RATED", "FAVORITES"};
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {


            //return mFragments.get(position);
            switch (position) {
                case 0:
                    Log.v("FragmentPagerAdapter", "on Page Adapter.. " + position);
                    PopularFragment popTab = new PopularFragment();
                    return popTab;
                case 1:
                    Log.v("FragmentPagerAdapter", "on Page Adapter.. " + position);
                    TopRatedFragment topTab = new TopRatedFragment();
                    return topTab;
                case 2:
                    Log.v("FragmentPagerAdapter", "on Page Adapter.. " + position);
                    FavMovieFragment favTab = new FavMovieFragment();
                    return favTab;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {

            //return mFragments.size();
            return Titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            //return mFragmentTitles.get(position);
            return Titles[position];
        }
    }

}
