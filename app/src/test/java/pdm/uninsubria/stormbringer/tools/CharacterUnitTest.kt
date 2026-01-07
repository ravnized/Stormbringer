package pdm.uninsubria.stormbringer.tools

import org.junit.Assert.*
import org.junit.Test

class CharacterUnitTest {

    @Test
    fun isDead() {
        val c = Character(isAlive = false)
        assertTrue(c.isDead())
    }

    @Test
    fun levelUp() {
        val c = Character(level = 1)
        c.levelUp()
        assertEquals(2, c.level)
    }

    @Test
    fun levelDown() {
        val c = Character(level = 1)
        c.levelDown()
        assertEquals(1, c.level)
    }

    @Test
    fun increaseXP() {
        val c = Character(xp = 10)
        c.increaseXp(5)
        assertEquals(15, c.xp)
        c.decreaseXp(20)
        assertEquals(0, c.xp)
    }

    @Test
    fun resetXP() {
        val c = Character(xp = 50)
        c.resetXp()
        assertEquals(0, c.xp)
    }
}

