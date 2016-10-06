package com.sethu.andpopularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sethugayu on 10/5/16.
 */
public class BeanReviews implements Parcelable {
    String id;
    String author;
    String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);

    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<BeanReviews> CREATOR = new Parcelable.Creator<BeanReviews>() {
        public BeanReviews createFromParcel(Parcel in) {
            return new BeanReviews(in);
        }

        public BeanReviews[] newArray(int size) {
            return new BeanReviews[size];
        }
    };
public BeanReviews(){

}
    private BeanReviews(Parcel in) {
//        mData = in.readInt();
        id=in.readString();
        content=in.readString();
        author=in.readString();

    }
}
