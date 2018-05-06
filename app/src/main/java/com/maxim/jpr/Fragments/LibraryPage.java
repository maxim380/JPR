package com.maxim.jpr.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxim.jpr.MainActivity;
import com.maxim.jpr.Models.Station;
import com.maxim.jpr.R;

import java.util.ArrayList;

public class LibraryPage extends Fragment {

    ArrayList<Station> files;

    public LibraryPage() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        Bundle bundle = this.getArguments();
        files = (ArrayList<Station>)bundle.getSerializable("files");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);

        ListAdapter listAdapter = new ListAdapter();
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        LinearLayout.LayoutParams contentViewLayout = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT );
        view.setLayoutParams( contentViewLayout );

        return view;
    }

    private class ListAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_song_item, parent, false);
            return new LibraryPage.ListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((LibraryPage.ListViewHolder) holder).bindView(position);
        }

        @Override
        public int getItemCount() {
            return files.size();
        }
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mItemText;
        private ImageView mItemImage;
        private TextView mItemDescription;

        public ListViewHolder(View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.itemText);
            mItemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            mItemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            mItemText.setText(files.get(position).getTitle());
            mItemImage.setImageBitmap(files.get(position).getAlbumArt());
            mItemDescription.setText(files.get(position).getDescription());
        }

        public void onClick(View view) {
            MainActivity activity = (MainActivity) getActivity();
            activity.playAudio(files.get(this.getAdapterPosition()).getUrl(), this.getAdapterPosition());
            activity.setNavBarNowPlayingSelected();
            activity.loadNowPlayingPage(true);
        }
    }

}


