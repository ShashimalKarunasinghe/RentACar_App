package com.example.rentacar.ui.main

import androidx.lifecycle.ViewModel
import com.example.rentacar.data.Car
import com.example.rentacar.data.CarRepository

class MainViewModel : ViewModel() {
    private var source: List<Car> = emptyList()
    private var index = 0

    fun refresh() { source = CarRepository.available(); index = 0 }
    fun current(): Car? = source.getOrNull(index)
    fun next(): Car? {
        if (source.isEmpty()) return null
        index = (index + 1) % source.size
        return current()
    }

    fun applySearch(query: String) {
        val q = query.trim().lowercase()
        source = CarRepository.available().filter {
            it.name.lowercase().contains(q) || it.model.lowercase().contains(q)
        }
        index = 0
    }

    fun sortByRating() { source = source.sortedByDescending { it.rating }; index = 0 }
    fun sortByYear()   { source = source.sortedByDescending { it.year };   index = 0 }
    fun sortByCost()   { source = source.sortedBy { it.dailyCost };        index = 0 }

    fun favourites() = CarRepository.favourites()
    fun toggleFavourite(id: String) = CarRepository.toggleFavourite(id)

    /** Select a car by id; returns the selected car or null if not available (e.g., rented). */
    fun select(id: String): Car? {
        var i = source.indexOfFirst { it.id == id }
        if (i >= 0) { index = i; return current() }

        // If not in the current filtered list, fall back to the full available list
        val full = CarRepository.available()
        i = full.indexOfFirst { it.id == id }
        if (i >= 0) { source = full; index = i; return current() }

        return null
    }
}
