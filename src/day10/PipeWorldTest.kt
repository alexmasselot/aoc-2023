package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PipeWorldTest{
    val map ="""
        -|L|F7
        7|S-7|
        L||7||
        -|L-J|
        L||-JF
    """.trimIndent().split("\n")

    @Test
    fun testParseInput(){
        val pw = PipeWorld.fromInput(map)

        assertEquals(listOf(1,2), pw.start)
    }

    @Test
    fun testPossibleStarts1(){
        val pw = PipeWorld.fromInput(map)
        val got = pw.possibleStarts()

        assertEquals(listOf(
            Mover(listOf(1,3), listOf(0,1)),
            Mover(listOf(2,2), listOf(1,0)),
        ), got)
    }

    @Test
    fun testPossibleStarts2(){
        val map ="""
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(map)
        val got = pw.possibleStarts()

        assertEquals(listOf(
            Mover(listOf(2,1), listOf(0,1)),
            Mover(listOf(3,0), listOf(1,0)),
        ), got)
    }

    @Test
    fun testFurthestPath1(){
        val given ="""
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.furthestPath()

        assertEquals(4, got)
    }

 @Test
    fun testFurthestPath2(){
        val given ="""
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.furthestPath()

        assertEquals(8, got)
    }
}