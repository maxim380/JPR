package com.maxim.jpr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.maxim.jpr.Fragments.LibraryPage;
import com.maxim.jpr.Fragments.PlayerPage;
import com.maxim.jpr.Fragments.SettingsPage;
import com.maxim.jpr.Models.Station;
import com.maxim.jpr.Util.StationSupplier;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MediaPlayerService mediaPlayer;
    private boolean serviceBound = false;
    private ArrayList<Station> stationlist;
    private int currentSongIndex;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        loadApp();
        changeColor(getColor());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_library:
                    loadLibraryPage();
                    return true;
                case R.id.navigation_nowPlaying:
                    loadNowPlayingPage(false);
                    return true;
                case R.id.navigation_settings:
                    loadSettingsPage();
                    return true;
            }
            return false;
        }
    };

    private void loadApp() {
        BottomNavigationView nav = findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.stationlist = StationSupplier.getStations();
        loadLibraryPage();
    }

    private void loadLibraryPage() {
        if (stationlist != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("files", stationlist);

            LibraryPage fragment = new LibraryPage();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content, fragment, "FragmentName");
            fragmentTransaction.commit();
        }
    }

    public void loadNowPlayingPage(Boolean fromPress) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromPress", fromPress);


        PlayerPage fragment = new PlayerPage();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "FragmentName");
        fragmentTransaction.commit();
    }

    private void loadSettingsPage() {
        SettingsPage fragment = new SettingsPage();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "FragmentName");
        fragmentTransaction.commit();
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayer = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    public void playAudio(String media, int index) {
        if (!serviceBound) {
            currentSongIndex = index;
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            currentSongIndex = index;
            mediaPlayer.playMedia(media);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            mediaPlayer.stopSelf();
        }
    }

    public ArrayList<Station> getStationlist() {
        return this.stationlist;
    }

    public Station getCurrentSong() {
        if (stationlist.size() != 0) {
            return stationlist.get(currentSongIndex);
        }
        return null;
    }

    public Station getFirstSong() {
        if (stationlist.size() > 0) {
            return stationlist.get(0);
        }
        return null;
    }

    public MediaPlayerService getMediaPlayer() {
        return this.mediaPlayer;
    }

    public int getColor() {
        SharedPreferences prefs = getSharedPreferences("colorPrefs", Context.MODE_PRIVATE);

        String color = prefs.getString("colorString", "x");

        switch (color) {
            case "blue":
                return getResources().getColor(R.color.colorPrimary);
            case "red":
                return getResources().getColor(R.color.colorRed);
            case "purple":
                return getResources().getColor(R.color.colorPurple);
            case "green":
                return getResources().getColor(R.color.colorGreen);
            default:
                return getResources().getColor(R.color.colorPrimary);
        }
    }

    public void changeColor(int color) {
        getWindow().setStatusBarColor(color);
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);
        switch (color) {
            case -12627531:
                nav.setItemBackgroundResource(R.color.colorPrimary);
                break;
            case -65536:
                nav.setItemBackgroundResource(R.color.colorRed);
                break;
            case -10092289:
                nav.setItemBackgroundResource(R.color.colorPurple);
                break;
            case -16744448:
                nav.setItemBackgroundResource(R.color.colorGreen);
                break;

        }
    }

    public void setNavBarNowPlayingSelected() {
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.navigation);
        nav.setSelectedItemId(R.id.navigation_nowPlaying);
    }

    public int getCurrentSongIndex() {
        return this.currentSongIndex;
    }

    public boolean currentSongIsFirstSong() {
        return currentSongIndex == 0;
    }

    public boolean currentSongIsLastSong() {
        return currentSongIndex == stationlist.size() - 1;
    }
}
