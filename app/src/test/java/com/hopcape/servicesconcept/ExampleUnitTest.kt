package com.hopcape.servicesconcept

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `multiplication is correct`(){
        assertEquals(4,2*2)
    }

    @Test
    fun `subtraction is correct`(){
        assertEquals(0,2-2)
    }
}