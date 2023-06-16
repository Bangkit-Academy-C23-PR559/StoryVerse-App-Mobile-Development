package com.c32pr559.storyverse.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.c32pr559.storyverse.local.entity.RecommendedTitle
import com.c32pr559.storyverse.util.RecommendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecommendRepository = RecommendRepository(application)
    val recommendedTitles: LiveData<List<RecommendedTitle>> = repository.getRecommendedTitles()

    fun fetchDataFromServer(categories: List<String>, navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchDataFromServer(categories)
            withContext(Dispatchers.Main) {
                navController.navigate("home")
            }
        }
    }
}
