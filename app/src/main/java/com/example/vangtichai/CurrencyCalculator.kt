package com.example.vangtichai

object CurrencyCalculator {

    // The list of Taka notes as required by the assignment
    private val denominations = listOf(500, 100, 50, 20, 10, 5, 2, 1)

    /**
     * Takes an amount (e.g., 688) and returns a map of Note -> Count.
     * Example: {500=1, 100=1, 50=1, 20=1, 10=1, 5=1, 2=1, 1=1}
     */
    fun calculateBreakdown(amount: Int): Map<Int, Int> {
        var remainingAmount = amount
        val breakdown = mutableMapOf<Int, Int>()

        for (note in denominations) {
            // How many of this note fit in the remaining amount?
            val count = remainingAmount / note
            breakdown[note] = count

            // Subtract the value of these notes from the remainder
            remainingAmount %= note
        }

        return breakdown
    }
}