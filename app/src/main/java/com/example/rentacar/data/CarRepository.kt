package com.example.rentacar.data

import com.example.rentacar.R

/**
 * Simple in-memory repository.
 * - Holds the 5 cars.
 * - Tracks which ones are currently rented (so they disappear from "available").
 * - Stores favourite flags.
 */
object CarRepository {

    private val cars = mutableListOf(
        Car("c1", "Aurora",  "LX",     2021, 4.5f, 38200,  70,  R.drawable.car1),
        Car("c2", "Raven",   "Sport",  2023, 4.8f, 15400,  95,  R.drawable.car2),
        Car("c3", "Kestrel", "Eco",    2020, 4.0f, 60120,  55,  R.drawable.car3),
        Car("c4", "Delta",   "Tourer", 2019, 3.8f, 81210,  45,  R.drawable.car4),
        Car("c5", "Nimbus",  "GT",     2022, 4.9f,  9900, 120,  R.drawable.car5),
        Car("c6", "Aurora",  "LX-6",     2021, 4.5f, 38200,  70,  R.drawable.car1),
    )

    private val rentedIds = mutableSetOf<String>()

    /** Cars not currently rented. */
    fun available(): List<Car> = cars.filter { it.id !in rentedIds }

    /** Mark a car as rented so it no longer appears on the main screen. */
    fun markRented(car: Car) { rentedIds += car.id }

    /** Undo rental (used if you later support cancellations). */
    fun cancelRental(car: Car) { rentedIds -= car.id }

    /** Toggle favourite by id. */
    fun toggleFavourite(id: String) {
        cars.find { it.id == id }?.let { it.isFavourite = !it.isFavourite }
    }

    /** Current favourites list (for the horizontal row). */
    fun favourites(): List<Car> = cars.filter { it.isFavourite }
}
