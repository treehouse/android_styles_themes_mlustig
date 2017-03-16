package com.teamtreehouse.stylesandthemes;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Song implements Parcelable {

  String title;
  int length;
  String artist;
  String album;

  public Song(String title, int length) {
    this.title = title;
    this.length = length;
    artist = "Kinder Than Wolves";
    album = "Mean Something";
  }

  public static final List<Song> songList =
      Arrays.asList(
          new Song("Hazel Days", 255),
          new Song("Weapons", 236),
          new Song("Hover", 266),
          new Song("Mean Something", 286),
          new Song("Make You Feel", 308)
      );

  public String getTitle() {
    return title;
  }

  public int getLength() {
    return length;
  }

  public String getArtist() {
    return artist;
  }

  public String getAlbum() {
    return album;
  }

  public static String getFormattedSongTime(int duration) {

    int mns = (duration / 60) % 60;
    int scs = duration % 60;

    DecimalFormat formatter = new DecimalFormat("00");
    String seconds = formatter.format(scs);

    String songTime = String.format("%02d",  mns);
    return songTime + ":" + seconds;
  }

  @Override public String toString() {
    return "Song:\t" + title + " ---------- " + length;
  }

  protected Song(Parcel in) {
    title = in.readString();
    artist = in.readString();
    album = in.readString();
    length = in.readInt();
  }

  public static final Creator<Song> CREATOR = new Creator<Song>() {
    @Override public Song createFromParcel(Parcel in) {
      return new Song(in);
    }

    @Override public Song[] newArray(int size) {
      return new Song[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(artist);
    dest.writeString(album);
    dest.writeInt(length);
  }
}
