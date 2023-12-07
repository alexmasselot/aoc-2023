package day07

data class Card(val hand: String, val bid: Int) {


    /**
     * counts the number of letters in the hand
     */
    fun handType(): String {
        return hand.groupBy { it }.mapValues { it.value.size }
            .values.sortedDescending().joinToString("")
    }

    fun compareTo(other: Card): Int {
        val ht = handType()
        val oht = other.handType()
        if(ht != oht) {
            return ht.compareTo(oht)
        }
        return hand.compareTo(other.hand)
    }
    companion object {
        val cOrder = "AKQJT98765432".zip((0..12).map { 'Z' - it }.joinToString("")).toMap()
        fun fromString(s: String): Card {
            val x = s.split(" ")
            return Card(x[0].map { cOrder[it]!! }.joinToString(""), x[1].toInt())
        }

        /**
         * orders the cards based on the compareTo method
         */
        fun sortList(cards:List<Card>):List<Card> {
            return cards.sortedWith(Comparator { o1, o2 -> o1.compareTo(o2) })
        }
    }

}