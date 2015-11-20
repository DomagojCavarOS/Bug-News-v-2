package com.d42gmail.cavar.bugnews;

/**
 * Created by Enigma on 19.11.2015..
 */
public class Bug {
    String Title, Description, Link, Category, Imageurl;

    public Bug(String title, String description, String link, String category, String imageurl) {
        Title = title;
        Description = description;
        Link = link;
        Category = category;
        Imageurl = imageurl;
    }

    public Bug()
    {
        Title="N/A";
        Description="N/A";
        Link="N/A";
        Category="N/A";
        Imageurl= String.valueOf(R.drawable.bug);
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}
