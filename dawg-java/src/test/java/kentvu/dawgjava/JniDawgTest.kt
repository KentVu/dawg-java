package kentvu.dawgjava

import io.kotlintest.specs.StringSpec

class JniDawgTest: StringSpec() {
    init {
        "createADawg" {
            val dawg = DawgTrie()
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
}
