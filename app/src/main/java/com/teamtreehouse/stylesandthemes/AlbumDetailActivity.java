package com.teamtreehouse.stylesandthemes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import jp.wasabeef.blurry.Blurry;

public class AlbumDetailActivity extends Activity {

    public static final String EXTRA_ALBUM_ART_RESID = "EXTRA_ALBUM_ART_RESID";

    @BindView(R.id.song_list) RecyclerView songListView;

    @BindView(R.id.album_art_blurred_background) ImageView albumArtView;

    int albumArtResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        ButterKnife.bind(this);
        populate();

        albumArtView.post(new Runnable() {
            @Override public void run() {
                Blurry.with(AlbumDetailActivity.this)
                    .radius(15)
                    .sampling(20)
                    .color(Color.argb(32, 0, 0, 0))
                    .capture(albumArtView)
                    .into(albumArtView);
            }
        });
    }

    interface OnSongViewHolderClickedListener {
        void onSongClicked(Song song);
    }

    static class SongVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Song mSong;
        private OnSongViewHolderClickedListener mListener;

        @BindView(R.id.song_title) TextView title;
        @BindView(R.id.song_duration) TextView songLength;

        public SongVH(View itemView, OnSongViewHolderClickedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mListener = listener;
        }

        public void bind(Song song) {
            mSong = song;
            title.setText(song.getTitle());
            songLength.setText(Song.getFormattedSongTime(mSong.getLength()));
        }

        @Override public void onClick(View v) {
            mListener.onSongClicked(mSong);
        }
    }

    private void populate() {
        albumArtResId = getIntent().getIntExtra(
            EXTRA_ALBUM_ART_RESID,
            R.drawable.mean_something_kinder_than_wolves);
        albumArtView.setImageResource(albumArtResId);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        songListView.setLayoutManager(lm);

        final List<Song> songs = Song.songList;

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<SongVH>() {
            @Override
            public SongVH onCreateViewHolder(ViewGroup parent, int viewType) {
                View songView = getLayoutInflater().inflate(R.layout.song_list_item, parent, false);
                return new SongVH(songView, new OnSongViewHolderClickedListener() {
                    @Override public void onSongClicked(Song song) {
                        Intent intent = new Intent(AlbumDetailActivity.this, SongPlaybackActivity.class);
                        intent.putExtra(SongPlaybackActivity.EXTRA_SONG, song);
                        intent.putExtra(SongPlaybackActivity.EXTRA_ALBUM_ART_RESID, albumArtResId);

                        startActivity(intent);
                    }
                });
            }

            @Override public void onBindViewHolder(SongVH holder, int position) {
                holder.bind(songs.get(holder.getAdapterPosition() % songs.size()));
            }

            @Override public int getItemCount() {
                return songs.size() * 4;
            }
        };

        songListView.setAdapter(adapter);
    }
}
