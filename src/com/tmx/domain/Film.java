package com.tmx.domain;

public class Film {
    private Integer film_id;
    private String film_name;
    private String film_downLoadURL;

    @Override
    public String toString() {
        return "film{" +
                "film_id=" + film_id +
                ", film_name='" + film_name + '\'' +
                ", film_downLoadURL='" + film_downLoadURL + '\'' +
                '}';
    }

    public Integer getFilm_id() {
        return film_id;
    }

    public void setFilm_id(Integer film_id) {
        this.film_id = film_id;
    }

    public String getFilm_name() {
        return film_name;
    }

    public void setFilm_name(String film_name) {
        this.film_name = film_name;
    }

    public String getFilm_downLoadURL() {
        return film_downLoadURL;
    }

    public void setFilm_downLoadURL(String film_downLoadURL) {
        this.film_downLoadURL = film_downLoadURL;
    }
}
