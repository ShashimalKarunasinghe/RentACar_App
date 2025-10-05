package com.example.rentacar.data

import com.example.rentacar.R


object CarRepository {

    private val cars = mutableListOf(
        Car("c1", "BMW",  "4 Series M Sport",     2023, 4.8f, 9900,  95,  R.drawable.car1),
        Car("c2", "Mercedes Benz",   "SL500",  2022, 2.7f, 15400,  65,  R.drawable.car2),
        Car("c3", "Mercedes Benz", "Maybach S 580",    2020, 3.5f, 60120,  110,  R.drawable.car3),
        Car("c4", "Ford",   "Mustang GT2025", 2025, 4.5f, 6100,  60,  R.drawable.car4),
        Car("c5", "Toyota",  "GR Supra 2024",     2024, 4.0f,  13100, 85,  R.drawable.car5),
    )

    private val rentedIds = mutableSetOf<String>()


    fun available(): List<Car> = cars.filter { it.id !in rentedIds }


    fun markRented(car: Car) { rentedIds += car.id }


    fun cancelRental(car: Car) { rentedIds -= car.id }


    fun toggleFavourite(id: String) {
        cars.find { it.id == id }?.let { it.isFavourite = !it.isFavourite }
    }

    fun favourites(): List<Car> = cars.filter { it.isFavourite }
}
