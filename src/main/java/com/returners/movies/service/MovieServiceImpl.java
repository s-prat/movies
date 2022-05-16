package com.returners.movies.service;

import com.returners.movies.model.*;
import com.returners.movies.repository.CertificationRepository;
import com.returners.movies.repository.GenreRepository;
import com.returners.movies.repository.MovieRepository;
import com.returners.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    GenreRepository genreRepository;
    
    @Autowired
    UserRepository userRepository;

    @Override
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }

    @Override
    public Movie insertMovie(Movie movie) {
        return movieRepository.save(movie);

    }

    @Override
    public List<Movie> getMoviesBySearchCriteria(SearchCriteria search){

        Optional<Certification> cert = null;
        Optional<Genre> genre = null;
        if (search.getCertificationId() != null) {
            cert = certificationRepository.findById(search.getCertificationId());
        }
        if (search.getGenreId() != null) {
            genre = genreRepository.findById(search.getGenreId());
        }
        return movieRepository.findByIdOrRatingOrTitleOrYearOrCertificationOrGenre(
                search.getId(),
                search.getRating(),
                search.getTitle(),
                search.getYear(),
                cert,
                genre
        );
    }

    @Override
    public List<Movie> getMoviesForUserBySearchCriteria(Long userId,SearchCriteria search){

        List<Long> certList = getCertificationList(userId);
        Optional<Certification> cert = null;
        Optional<Genre> genre = null;
        if (search.getCertificationId() != null) {
            cert = certificationRepository.findById(search.getCertificationId());
        }
        if (search.getGenreId() != null) {
            genre = genreRepository.findById(search.getGenreId());
        }
        return movieRepository.findByIdOrRatingOrTitleOrYearOrCertificationOrGenre(
                search.getId(),
                search.getRating(),
                search.getTitle(),
                search.getYear(),
                cert,
                genre
        );
    }

    public List<Long> getCertificationList(Long userId){
        User user = userRepository.findById(userId).get();
        int userAge = 99;
        if(user != null) {
            userAge = user.getAge();
        }
        ArrayList<String> certNames = new ArrayList<>();

        certNames.add("U");
        certNames.add("12A");
        if (userAge >= 18){
            certNames.add("18");
            certNames.add("R");
            certNames.add("TBC");
        }
        if (userAge >= 15){
            certNames.add("15");
        }
        if (userAge >= 12){
            certNames.add("12");
        }
        if (userAge >= 8){
            certNames.add("PG");
        }

        List<Long> certList = certificationRepository.findByNames(certNames);
        return certList;
    }

}
