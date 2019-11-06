package com.tmx.test;

import com.tmx.dao.FilmDao;
import com.tmx.domain.Film;
import org.junit.Test;

public class FilmDaoTest {
    @Test
    public void test(){
        Film film = new Film();
        film.setFilm_downLoadURL("ni");
        film.setFilm_name("毒杀");
        FilmDao filmDao = new FilmDao();
        filmDao.addFilm(film);
    }
}
