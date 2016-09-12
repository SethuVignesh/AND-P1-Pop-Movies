package com.sethu.andpopularmoviesstage1;

/**
 * Created by sethu on 8/29/2016.
 */
public class BeanMovies {
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



}
