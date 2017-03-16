package com.teamtreehouse.stylesandthemes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumListActivity extends Activity {

    public static final int[] albumArts = {
        R.drawable.mean_something_kinder_than_wolves,
        R.drawable.cylinders_chris_zabriskie,
        R.drawable.broken_distance_sutro,
        R.drawable.playing_with_scratches_ruckus_roboticus,
        R.drawable.keep_it_together_guster,
        R.drawable.the_carpenter_avett_brothers,
        R.drawable.please_sondre_lerche,
        R.drawable.direct_to_video_chris_zabriskie };

    @BindView(R.id.album_list) RecyclerView mAlbumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        ButterKnife.bind(this);
        populate();
    }

    interface OnVHClickedListener {
        void onVHClicked(AlbumVH vh);
    }

    static class AlbumVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OnVHClickedListener mListener;
        @BindView(R.id.album_art)
        ImageView albumArt;

        public AlbumVH(View itemView, OnVHClickedListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onVHClicked(this);
        }
    }

    private void populate() {
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAlbumList.setLayoutManager(lm);

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<AlbumVH>() {
            @Override
            public AlbumVH onCreateViewHolder(ViewGroup parent, int viewType) {
                View albumView = getLayoutInflater().inflate(R.layout.album_grid_item, parent, false);
                return new AlbumVH(albumView, new OnVHClickedListener() {
                    @Override
                    public void onVHClicked(AlbumVH vh) {
                        int albumArtResId = albumArts[vh.getAdapterPosition() % albumArts.length];
                        Intent intent = new Intent(AlbumListActivity.this, AlbumDetailActivity.class);
                        intent.putExtra(AlbumDetailActivity.EXTRA_ALBUM_ART_RESID, albumArtResId);

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onBindViewHolder(AlbumVH holder, int position) {
                holder.albumArt.setImageResource(albumArts[position % albumArts.length]);
            }

            @Override
            public int getItemCount() {
                return albumArts.length * 4;
            }
        };

        mAlbumList.setAdapter(adapter);
    }

}
