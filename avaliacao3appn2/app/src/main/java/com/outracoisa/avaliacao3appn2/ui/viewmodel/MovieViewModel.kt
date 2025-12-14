package com.outracoisa.avaliacao3appn2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.outracoisa.avaliacao3appn2.data.local.entity.Movie
import com.outracoisa.avaliacao3appn2.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MovieUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val showFavoritesOnly: Boolean = false
)

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
        // Busca filmes populares automaticamente na primeira vez
        fetchPopularMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val flow = if (_uiState.value.showFavoritesOnly) {
                movieRepository.getFavoriteMovies()
            } else {
                movieRepository.getAllMovies()
            }
            flow.collect { movies ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    movies = movies
                )
            }
        }
    }

    fun toggleShowFavorites() {
        _uiState.value = _uiState.value.copy(showFavoritesOnly = !_uiState.value.showFavoritesOnly)
        loadMovies()
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            
            val result = movieRepository.fetchPopularMovies()
            
            result.fold(
                onSuccess = { movies ->
                    println("✅ Filmes carregados com sucesso: ${movies.size} filmes")
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    println("❌ Erro ao buscar filmes: ${exception.message}")
                    exception.printStackTrace()
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = "Erro: ${exception.message}"
                    )
                }
            )
        }
    }

    fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            val result = movieRepository.fetchNowPlayingMovies()
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = exception.message ?: "Erro ao buscar filmes"
                    )
                }
            )
        }
    }

    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            val result = movieRepository.fetchTopRatedMovies()
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = exception.message ?: "Erro ao buscar filmes"
                    )
                }
            )
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            val result = movieRepository.fetchUpcomingMovies()
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = exception.message ?: "Erro ao buscar filmes"
                    )
                }
            )
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movie)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    class Factory(private val movieRepository: MovieRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MovieViewModel(movieRepository) as T
        }
    }
}
