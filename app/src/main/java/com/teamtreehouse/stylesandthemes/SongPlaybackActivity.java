package com.teamtreehouse.stylesandthemes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

public class SongPlaybackActivity extends AppCompatActivity {

  public static final String EXTRA_SONG = "EXTRA_SONG";
  public static final String EXTRA_ALBUM_ART_RESID = "EXTRA_ALBUM_ART_SONG";

  @BindView(R.id.blurred_background) ImageView background;
  @BindView(R.id.main_album_art) ImageView mainAlbumImage;
  @BindView(R.id.song_title) TextView songDisplay;
  @BindView(R.id.song_seek_bar) SeekBar songSeekBar;
  @BindView(R.id.song_duration) TextView songDuration;
  @BindView(R.id.song_current_time) TextView songCurrentTimeView;
  @BindView(R.id.play_pause_btn) ImageButton playPauseButton;

  Song song;
  int currentSongTime;
  private boolean playing = true;
  private int albumArtResId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_playback);
    ButterKnife.bind(this);

    song = getIntent().getParcelableExtra(EXTRA_SONG);

    songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          currentSongTime = song.getLength() * progress / 100;
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    populateAlbumArt();
    displaySong(song);

    play();
  }

  private void populateAlbumArt() {
    albumArtResId = getIntent().getIntExtra(
        EXTRA_ALBUM_ART_RESID,
        R.drawable.mean_something_kinder_than_wolves);
    background.setImageResource(albumArtResId);
    mainAlbumImage.setImageResource(albumArtResId);

    background.post(new Runnable() {
      @Override public void run() {
        Blurry.with(SongPlaybackActivity.this)
            .radius(15)
            .sampling(20)
            .color(Color.argb(32, 0, 0, 0))
            .capture(background)
            .into(background);
      }
    });
  }

  private void displaySong(Song song) {
    songDisplay.setText(song.getTitle());
    currentSongTime = 0;
    songDuration.setText(Song.getFormattedSongTime(song.getLength()));
  }

  private Song getNextSong(Song currentSong) {
    int currentSongIndex = Song.songList.indexOf(currentSong);
    int numberOfSongs = Song.songList.size();
    int nextSongIndex = currentSongIndex + 1;
    if (nextSongIndex > numberOfSongs - 1) nextSongIndex = 0;
    return Song.songList.get(nextSongIndex);
  }

  private Song getPreviousSong(Song currentSong) {
    int currentSongIndex = Song.songList.indexOf(currentSong);
    int numberOfSongs = Song.songList.size();
    int previousSongIndex = currentSongIndex - 1;
    if (previousSongIndex < 0) previousSongIndex = numberOfSongs - 1;
    return Song.songList.get(previousSongIndex);
  }

  public void onClickRewind(View view) {
    song = getPreviousSong(song);

    displaySong(song);
  }

  public void onClickPlayPause(View view) {
    if (playing) {
      pause();
    } else {
      play();
    }
  }

  private void play() {
    playing = true;
    playPauseButton.setImageResource(R.drawable.ic_pause);
    simulatePlaybackRunnable.run();
  }

  private void pause() {
    playing = false;
    playPauseButton.setImageResource(R.drawable.ic_play_arrow);
    songPlaybackSimulationHandler.removeCallbacks(simulatePlaybackRunnable);
  }

  public void onClickFastForward(View view) {
    song = getNextSong(song);

    displaySong(song);
  }

  Handler songPlaybackSimulationHandler = new Handler();
  final Runnable simulatePlaybackRunnable = new Runnable() {
    @Override public void run() {
      currentSongTime++;

      if (currentSongTime >= song.getLength()) {
        song = getNextSong(song);
        displaySong(song);
      }

      songCurrentTimeView.setText(Song.getFormattedSongTime(currentSongTime));
      int progress = (currentSongTime) * 100 / song.getLength();
      songSeekBar.setProgress(progress);
      songPlaybackSimulationHandler.postDelayed(simulatePlaybackRunnable, 1000);
    }
  };

  @Override protected void onDestroy() {
    super.onDestroy();

    songPlaybackSimulationHandler.removeCallbacks(simulatePlaybackRunnable);
  }

  public void popupMenu(View view) {
    PopupMenu menu = new PopupMenu(this, view);

    menu.getMenu().add("Settings").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        startActivity(new Intent(SongPlaybackActivity.this, SettingsActivity.class));
        return true;
      }
    });
    menu.show();
  }
}
