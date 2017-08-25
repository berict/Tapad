package com.bedrock.padder.model.preset;

import java.util.Date;

public class Review {

    private Integer rating;

    private String comment;

    private Integer version;

    private Date date;

    public Review(Integer rating, String comment, Integer version, Date date) {
        this.rating = rating;
        this.comment = comment;
        this.version = version;
        this.date = date;
    }

    public Review(Integer rating, Integer version, Date date) {
        this.rating = rating;
        this.version = version;
        this.date = date;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Integer getVersion() {
        return version;
    }

    public Date getDate() {
        return date;
    }
}
