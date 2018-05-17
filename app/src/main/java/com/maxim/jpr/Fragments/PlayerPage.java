package com.maxim.jpr.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxim.jpr.MainActivity;
import com.maxim.jpr.MediaPlayerService;
import com.maxim.jpr.Models.Station;
import com.maxim.jpr.R;
import com.maxim.jpr.Util.FileHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerPage extends Fragment {

    private ImageView playImg;
    private ImageView albumArt;
    private TextView titleText;
    private TextView infoText;
    private Button songButton;
    private Button allSongsButton;
    
    private Station station;

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
        Bundle bundle = this.getArguments();
        boolean fromPress = bundle.getBoolean("fromPress");

        titleText = (TextView) view.findViewById(R.id.textViewTitle);
        infoText = (TextView) view.findViewById(R.id.textViewSongInfo);
        playImg = (ImageView) view.findViewById(R.id.playImg);
        albumArt = (ImageView) view.findViewById(R.id.albumArt);
        songButton = (Button) view.findViewById(R.id.songButton);
        allSongsButton = (Button) view.findViewById(R.id.allSongsButton);

        setSongInfo(activity.getCurrentSong());

        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                playImg.setTag(R.drawable.ic_play_arrow);
            } else {
                playImg = (ImageView) view.findViewById(R.id.playImg);
                playImg.setTag(R.drawable.ic_pause);
                playImg.setImageResource(R.drawable.ic_pause);
            }
        } else if(fromPress) {
            playImg.setTag(R.drawable.ic_pause);
            playImg.setImageResource(R.drawable.ic_pause);
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

        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSongName();
            }
        });
        
        allSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllSongs();
            }
        });
    }

    private void showAllSongs() {
        String[] names = FileHelper.getSongList(this.getContext());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Song names");
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://www.youtube.com/results?search_query=";
                String song = lv.getItemAtPosition(position).toString().replace(" ", "+");
                url += song;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        alertDialog.show();
    }

    public void setSongInfo(Station file) {
        this.station = file;
        if(file != null) {
            titleText.setText(file.getTitle());
            infoText.setText(file.getDescription());
            albumArt.setImageBitmap(file.getAlbumArt());
        }
    }

    public void getSongName() {
        GetSong songGetter = new GetSong();
        songGetter.execute(station.getInfoURL());

    }

    private class GetSong extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            final StringBuilder builder = new StringBuilder();
            final String url = strings[0];
            try {
                Document doc = Jsoup.connect(url).get();
                String[] test = doc.toString().split("\"");
                for(int i = 0; i < test.length; i++) {
                    String s = test[i];
                    if (s.equals("song")) {
                        builder.append(test[i + 2]);
                    }
                }
            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), result,
                    Toast.LENGTH_LONG).show();
            FileHelper.appendSong(getContext(), result);
        }
    }
}
