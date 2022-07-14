package ru.oepak22.data.mapper;

import ru.oepak22.domain.model.Movie;
import rx.functions.Func1;

// класс маппера полученных данных
public class MoviesMapper implements Func1<ru.oepak22.data.model.content.Movie, Movie> {

    @Override
    public Movie call(ru.oepak22.data.model.content.Movie movie) {
        return new Movie(movie.getId(), movie.getPosterPath(),
                            movie.getOverview(),
                            movie.getTitle(),
                            movie.getReleasedDate(),
                            movie.getVoteAverage());
    }
}
