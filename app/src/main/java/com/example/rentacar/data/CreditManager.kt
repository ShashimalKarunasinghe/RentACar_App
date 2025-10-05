package com.example.rentacar.data

/**
 * Centralised credit logic:
 * - Combined total spend must not exceed 500.
 * - A single rental must not exceed 400.
 */
object CreditManager {
    private const val MAX_TOTAL = 500
    private const val MAX_PER_RENT = 400

    private var spent = 0

    val balance: Int
        get() = MAX_TOTAL - spent

    /** Check if a booking is allowed under both limits. */
    fun canAfford(days: Int, dailyCost: Int): Boolean {
        val cost = days * dailyCost
        if (cost > MAX_PER_RENT) return false
        return spent + cost <= MAX_TOTAL
    }

    /** Apply a successful booking. */
    fun charge(days: Int, dailyCost: Int) {
        spent += days * dailyCost
    }

    /** Refund a booking (if you support cancel-after-save later). */
    fun refund(days: Int, dailyCost: Int) {
        spent -= days * dailyCost
    }
}
