package day07

data class Card2(val hand: String, val bid: Int) {


    /**
     * counts the number of letters in the hand
     */
    fun handType(): String {
        val j = 'Z' - 12
        val js = hand.count { it == j }
        if(js == 5){
            return "5"
        }
        val ns = hand.replace(j.toString(), "").groupBy { it }.mapValues { it.value.size }
            .values.sortedDescending()
        return listOf(ns.first() + js).plus(ns.drop(1)).joinToString("")
    }

    fun compareTo(other: Card2): Int {
        val ht = handType()
        val oht = other.handType()
        if (ht != oht) {
            return ht.compareTo(oht)
        }
        return hand.compareTo(other.hand)
    }

    companion object {
        val cOrder = "AKQT98765432J".zip((0..12).map { 'Z' - it }.joinToString("")).toMap()
        fun fromString(s: String): Card2 {
            val x = s.split(" ")
            return Card2(x[0].map { cOrder[it]!! }.joinToString(""), x[1].toInt())
        }

        /**
         * orders the cards based on the compareTo method
         */
        fun sortList(cards: List<Card2>): List<Card2> {
            return cards.sortedWith(Comparator { o1, o2 -> o1.compareTo(o2) })
        }
    }

}