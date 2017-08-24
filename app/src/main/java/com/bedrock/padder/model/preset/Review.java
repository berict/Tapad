package com.bedrock.padder.model.preset;

public class Review {

    private Integer rating;

    private String comment;

    private Integer version;

    private Long date;

    public Review(Integer rating, String comment, Integer version, Long date) {
        this.rating = rating;
        this.comment = comment;
        this.version = version;
        this.date = date;
    }

    public Review(Integer rating, Integer version, Long date) {
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

    public Long getLong() {
        return date;
    }
}
