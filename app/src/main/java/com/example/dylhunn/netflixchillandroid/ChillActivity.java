package com.example.dylhunn.netflixchillandroid;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.common.ConnectionResult;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import java.lang.Thread;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class ChillActivity extends ActionBarActivity implements ActionBar.TabListener, FindMatchesFragment.OnFragmentInteractionListener,
        ConnectionCallbacks, OnConnectionFailedListener, ItemFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static int uid;

    private Toast activelyWorkingToast;

    private GoogleApiClient mGoogleApiClient;

    // horrible data sharing :)
    public static Location lastLocation = null;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    protected synchronized void buildGoogleApiClient() {
        Log.i("NetflixAndChill", "Building API Client");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chill);

        buildGoogleApiClient();

        Intent intent = getIntent();
        int uid = intent.getIntExtra("uid", -1);
        assert (uid >= 0);
        this.uid = uid;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        activelyWorkingToast = Toast.makeText(this.getApplicationContext(),
                "Making your request...", Toast.LENGTH_SHORT);

        setTitle("Chillax");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chill, menu);
        return true;
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

        if (id == R.id.action_logout) {
            DataPersist d = new DataPersist(getApplicationContext());
            d.clearStoredUserId();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
            switch(position) {
                case 0:
                    // return the planning page fragment
                    return FindMatchesFragment.newInstance("" + uid);
                case 1:
                    // return the matches page fragment
                    return ItemFragment.newInstance("foo", "bar");
                default:

            }

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FIND MATCHES";
                case 1:
                    return "MY MATCHES";
            }
            return null;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("NetflixAndChill", "Error connecting to location API");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("NetflixAndChill", "OnConnected Triggered");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) Log.i("NetflixAndChill", "Found location " + lastLocation.getLatitude() + " " + lastLocation.getLongitude());
        else Log.e("NetflixAndChill", "Location was null!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult c) {
        Log.e("NetflixAndChill", "Error connecting to location API");

    }

    public void goClicked(View v) {
        Spinner genreSpn = (Spinner) findViewById(R.id.genreSpinner);
        Spinner typeSpn = (Spinner) findViewById(R.id.typeSpinner);
        Spinner daySpn = (Spinner) findViewById(R.id.daySpinner);
        Spinner whenSpn = (Spinner) findViewById(R.id.whenSpinner);
        String type = typeSpn.getSelectedItem().toString();
        ChillRequest.MediaType mtype;
        if (type.contains("TV")) {
            mtype = ChillRequest.MediaType.TV_SHOW;
        } else {
            mtype = ChillRequest.MediaType.FILM;
        }

        ChillRequest cr = new ChillRequest(genreSpn.getSelectedItem().toString(), mtype,
                daySpn.getSelectedItem().toString(), whenSpn.getSelectedItem().toString());

        ApiService.makeChillRequest(uid, cr, this);

        activelyWorkingToast.show();
    }

    public void chillRequestSucceeded(int chill_id) {
        // slide to next tab
        getSupportActionBar().setSelectedNavigationItem(1);
        Context context = getApplicationContext();
        //if (activelyWorkingToast != null) activelyWorkingToast.cancel();
        CharSequence text = "Done. We will find you some matches!";
        activelyWorkingToast.setText(text);
        activelyWorkingToast.show();
    }

    public void chillRequestFailed() {
        Context context = getApplicationContext();
        CharSequence text = "Something went wrong!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void chillDeleteSuccess() {
        Context context = getApplicationContext();
        CharSequence text = "Deleted.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void chillDeleteFailure() {
        Context context = getApplicationContext();
        CharSequence text = "Oops: unable to delete.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_chill, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
