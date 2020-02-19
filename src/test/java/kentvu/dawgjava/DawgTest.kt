package kentvu.dawgjava

import org.junit.Test

class DawgTest {
    @Test
    fun createADawg() {
        val dawg: Dawg = JniDawg()
        dawg.run {
            insert("Vietnam")
            insert("Cambodia")
            insert("Thailand")
            insert("Laos")
            insert("countries")
            insert("Venezuela")
            save()
        }
    }
}
