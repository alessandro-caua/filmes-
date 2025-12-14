package com.outracoisa.avaliacao3appn2.data.repository

import com.outracoisa.avaliacao3appn2.data.local.dao.MovieDao
import com.outracoisa.avaliacao3appn2.data.local.entity.Movie
import com.outracoisa.avaliacao3appn2.data.remote.ApiService
import com.outracoisa.avaliacao3appn2.data.remote.RetrofitClient
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val movieDao: MovieDao,
    private val apiService: ApiService
) {
    fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies()
    }

    fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    suspend fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)
    }

    suspend fun toggleFavorite(movie: Movie) {
        val updatedMovie = movie.copy(isFavorite = !movie.isFavorite)
        movieDao.update(updatedMovie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.delete(movie)
    }

    suspend fun fetchPopularMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getPopularMovies(RetrofitClient.API_KEY)
            val movies = response.results.map { apiMovie ->
                Movie(
                    id = apiMovie.id,
                    title = apiMovie.title,
                    overview = apiMovie.overview,
                    posterPath = apiMovie.posterPath,
                    backdropPath = apiMovie.backdropPath,
                    releaseDate = apiMovie.releaseDate,
                    voteAverage = apiMovie.voteAverage,
                    voteCount = apiMovie.voteCount,
                    isFavorite = false
                )
            }
            movieDao.deleteNonFavorites()
            movieDao.insertAll(movies)
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchNowPlayingMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getNowPlayingMovies(RetrofitClient.API_KEY)
            val movies = response.results.map { apiMovie ->
                Movie(
                    id = apiMovie.id,
                    title = apiMovie.title,
                    overview = apiMovie.overview,
                    posterPath = apiMovie.posterPath,
                    backdropPath = apiMovie.backdropPath,
                    releaseDate = apiMovie.releaseDate,
                    voteAverage = apiMovie.voteAverage,
                    voteCount = apiMovie.voteCount,
                    isFavorite = false
                )
            }
            movieDao.deleteNonFavorites()
            movieDao.insertAll(movies)
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchTopRatedMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getTopRatedMovies(RetrofitClient.API_KEY)
            val movies = response.results.map { apiMovie ->
                Movie(
                    id = apiMovie.id,
                    title = apiMovie.title,
                    overview = apiMovie.overview,
                    posterPath = apiMovie.posterPath,
                    backdropPath = apiMovie.backdropPath,
                    releaseDate = apiMovie.releaseDate,
                    voteAverage = apiMovie.voteAverage,
                    voteCount = apiMovie.voteCount,
                    isFavorite = false
                )
            }
            movieDao.deleteNonFavorites()
            movieDao.insertAll(movies)
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchUpcomingMovies(): Result<List<Movie>> {
        return try {
            val response = apiService.getUpcomingMovies(RetrofitClient.API_KEY)
            val movies = response.results.map { apiMovie ->
                Movie(
                    id = apiMovie.id,
                    title = apiMovie.title,
                    overview = apiMovie.overview,
                    posterPath = apiMovie.posterPath,
                    backdropPath = apiMovie.backdropPath,
                    releaseDate = apiMovie.releaseDate,
                    voteAverage = apiMovie.voteAverage,
                    voteCount = apiMovie.voteCount,
                    isFavorite = false
                )
            }
            movieDao.deleteNonFavorites()
            movieDao.insertAll(movies)
            Result.success(movies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
