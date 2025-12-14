package com.outracoisa.avaliacao3appn2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.outracoisa.avaliacao3appn2.data.local.AppDatabase
import com.outracoisa.avaliacao3appn2.data.remote.RetrofitClient
import com.outracoisa.avaliacao3appn2.data.repository.MovieRepository
import com.outracoisa.avaliacao3appn2.data.repository.UserRepository
import com.outracoisa.avaliacao3appn2.ui.navigation.AppNavigation
import com.outracoisa.avaliacao3appn2.ui.theme.Avaliacao3appn2Theme
import com.outracoisa.avaliacao3appn2.ui.viewmodel.AuthViewModel
import com.outracoisa.avaliacao3appn2.ui.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val userRepository by lazy { UserRepository(database.userDao()) }
    private val movieRepository by lazy { MovieRepository(database.movieDao(), RetrofitClient.apiService) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Avaliacao3appn2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    val authViewModel: AuthViewModel = viewModel(
                        factory = AuthViewModel.Factory(userRepository)
                    )
                    
                    val movieViewModel: MovieViewModel = viewModel(
                        factory = MovieViewModel.Factory(movieRepository)
                    )
                    
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        movieViewModel = movieViewModel
                    )
                }
            }
        }
    }
}