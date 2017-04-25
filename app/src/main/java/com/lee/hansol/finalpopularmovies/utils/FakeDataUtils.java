package com.lee.hansol.finalpopularmovies.utils;

import com.lee.hansol.finalpopularmovies.models.Movie;

public class FakeDataUtils {

    public static Movie[] getFakeMoviesData() {
        Movie movie1 = new Movie(270, "Movie1", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");
        Movie movie2 = new Movie(271, "Movie2", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");
        Movie movie3 = new Movie(272, "Movie3", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");
        Movie movie4 = new Movie(273, "Movie4", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");
        Movie movie5 = new Movie(274, "Movie5", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");
        Movie movie6 = new Movie(275, "Movie6", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");
        Movie movie7 = new Movie(276, "Movie7", "https://s-media-cache-ak0.pinimg.com/236x/4d/11/ab/4d11ab483077afd9021fe86530c38270.jpg",
                "2017-03-03", "8.3", "Story about pikachu");

        return new Movie[] {movie1, movie2, movie3, movie4, movie5, movie6, movie7};
    }
}
