package com.sethu.andpopularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sethu on 8/29/2016.
 */
public class BeanMovies implements Parcelable{
    private String title;
    private String image_url;
    private String overview;
    private String user_rating;
    private String release_date;
    public BeanMovies(String _title,String _image_url,String _overview,String _user_rating,String _release_date){
        title=_title;
        image_url=_image_url;
        overview=_overview;
        user_rating=_user_rating;
        release_date=_release_date;
    }
    public BeanMovies(){

        }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(image_url);
        parcel.writeString(overview);
        parcel.writeString(user_rating);
        parcel.writeString(release_date);

    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<BeanMovies> CREATOR = new Parcelable.Creator<BeanMovies>() {
        public BeanMovies createFromParcel(Parcel in) {
            return new BeanMovies(in);
        }

        public BeanMovies[] newArray(int size) {
            return new BeanMovies[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private BeanMovies(Parcel in) {
//        mData = in.readInt();
        title=in.readString();
        image_url=in.readString();
        overview=in.readString();
        user_rating=in.readString();
        release_date=in.readString();
    }
}
