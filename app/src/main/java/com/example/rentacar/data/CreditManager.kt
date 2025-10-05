package com.example.rentacar.data


object CreditManager {
    private const val MAX_TOTAL = 500
    private const val MAX_PER_RENT = 400

    private var spent = 0

    val balance: Int
        get() = MAX_TOTAL - spent


    fun canAfford(days: Int, dailyCost: Int): Boolean {
        val cost = days * dailyCost
        if (cost > MAX_PER_RENT) return false
        return spent + cost <= MAX_TOTAL
    }


    fun charge(days: Int, dailyCost: Int) {
        spent += days * dailyCost
    }


    fun refund(days: Int, dailyCost: Int) {
        spent -= days * dailyCost
    }
}
