package kentvu.dawgjava

import io.kotlintest.specs.StringSpec

class JniDawgTest: StringSpec() {
    init {
        "createADawg" {
            val dawg = DawgTrie()
            dawg.run {
            }
        }
    }
}
