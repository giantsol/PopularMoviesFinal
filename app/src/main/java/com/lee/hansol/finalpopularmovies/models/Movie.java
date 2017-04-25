package com.lee.hansol.finalpopularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public int movieId;
    public String title;
    public String thumbnailPath;
    public String date;
    public double rating;
    public String overview;

    public Movie(int movieId, String title, String thumbnailPath, String date, double rating, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.thumbnailPath = thumbnailPath;
        this.date = date;
        this.rating = rating;
        this.overview = overview;
    }

    private Movie(Parcel in) {
        this.movieId = in.readInt();
        this.title = in.readString();
        this.thumbnailPath = in.readString();
        this.date = in.readString();
        this.rating = in.readDouble();
        this.overview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(thumbnailPath);
        dest.writeString(date);
        dest.writeDouble(rating);
        dest.writeString(overview);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}

