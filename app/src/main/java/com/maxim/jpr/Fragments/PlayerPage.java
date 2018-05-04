package com.maxim.jpr.Fragments;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxim.jpr.MainActivity;
import com.maxim.jpr.MediaPlayerService;
import com.maxim.jpr.Models.Station;
import com.maxim.jpr.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerPage extends Fragment {

    private ImageView nextImg;
    private ImageView prevImg;
    private ImageView playImg;
    private ImageView albumArt;
    private TextView titleText;
    private TextView infoText;

    private MainActivity activity;
    private MediaPlayerService mediaPlayer;

    public PlayerPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_page, container, false);

        activity = (MainActivity) getActivity();
        mediaPlayer = activity.getMediaPlayer();


        titleText = (TextView) view.findViewById(R.id.textViewTitle);
        infoText = (TextView) view.findViewById(R.id.textViewSongInfo);
        playImg = (ImageView) view.findViewById(R.id.playImg);
        albumArt = (ImageView) view.findViewById(R.id.albumArt);

        setSongInfo(activity.getCurrentSong());

        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                playImg.setTag(R.drawable.ic_play_arrow);
            } else {
                playImg = (ImageView) view.findViewById(R.id.playImg);
                playImg.setTag(R.drawable.ic_pause);
                playImg.setImageResource(R.drawable.ic_pause);
            }
        }



        createListeners();
        return view;
    }

    private void createListeners() {
        playImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if ((int) playImg.getTag() == R.drawable.ic_play_arrow) {
                        mediaPlayer.playMedia();
                        playImg.setImageResource(R.drawable.ic_pause);
                        playImg.setTag(R.drawable.ic_pause);
                    } else {
                        mediaPlayer.pauseMedia();
                        playImg.setImageResource(R.drawable.ic_play_arrow);
                        playImg.setTag(R.drawable.ic_play_arrow);
                    }
                } else {
                    if(activity.getFirstSong() != null) {
                        activity.playAudio(activity.getFirstSong().getUrl(), 0);
                        mediaPlayer = activity.getMediaPlayer();
                        setSongInfo(activity.getFirstSong());
                        playImg.setImageResource(R.drawable.ic_pause);
                        playImg.setTag(R.drawable.ic_pause);
                    }
                }
            }
        });
    }

    public void setSongInfo(Station file) {
        if(file != null) {
            titleText.setText(file.getTitle());
//            infoText.setText(file.getArtist() + " - " + file.getAlbum());
            albumArt.setImageBitmap(file.getAlbumArt());
        }
    }
}
