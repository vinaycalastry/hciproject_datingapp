package com.example.android.testamante.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.testamante.R;
import com.example.android.testamante.ui.fragments.AboutFragment;
import com.example.android.testamante.ui.fragments.ProfileDetailsFragment;
import com.example.android.testamante.ui.fragments.ProfilePicFragment;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;


public class ProfileActivity extends AppCompatActivity implements ProfileDetailsFragment.OnFragmentInteractionListener, ProfilePicFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private SwipeSelector swipeSelector;
    private Boolean fromMatching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent extras = getIntent();
        Bundle extasBundle = extras.getExtras();
        if (!extasBundle.isEmpty()) {
            fromMatching = (Boolean) extras.getExtras().get("fromMatches");
            //Log.v("From",(Boolean)extras.getExtras().get("fromMatches")+"");
        } else {
            fromMatching = false;
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Log.v("Slider","Page Selected to "+position);
                swipeSelector.selectItemAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        swipeSelector = (SwipeSelector) findViewById(R.id.conditionSelector);
        swipeSelector.setItems(
                new SwipeItem(0, "Profile Information", ""),
                new SwipeItem(1, "Profile Picture", ""),
                new SwipeItem(2, "Additional Info ", "")
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_profile, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(int i) {
        switch (i) {
            case 0:
                mViewPager.setCurrentItem(i + 1);
                break;
            case 1:
                mViewPager.setCurrentItem(i + 1);
                break;
            case 2:
                if (fromMatching) {
                    finish();
                } else {
                    startActivity(new Intent(this, MatchProfilesActivity.class));
                    finish();
                }
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    return ProfileDetailsFragment.newInstance("", "");

                case 1:
                    return ProfilePicFragment.newInstance("", "");

                case 2:
                    return AboutFragment.newInstance("", "");

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
