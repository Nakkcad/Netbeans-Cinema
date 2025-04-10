/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import dao.FilmDAO;
import java.io.IOException;
import java.util.List;
import model.Film;

/**
 *
 * @author ACER
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TMDBFetcher fetcher = new TMDBFetcher();
        List<Film> films = fetcher.fetchMovies(50);

        FilmDAO dao = new FilmDAO();
        dao.insertFilms(films);
    }
}
