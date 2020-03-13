package kentvu.dawgjava

import io.kotlintest.TestCase
import io.kotlintest.assertSoftly
import io.kotlintest.inspectors.forAll
import io.kotlintest.matchers.maps.shouldContainKey
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import java.time.Duration

//@UseExperimental(ObsoleteCoroutinesApi::class)
@ObsoleteCoroutinesApi
class TrieTests: StringSpec() {

    init {
        "build".config(timeout = Duration.ofMillis(1000)) {
            val channel = Channel<Int>()
            val job = async {
                val progress = channel.toList()
                assertSoftly {
                    progress[0] shouldBe 2
                    progress[1] shouldBe 4
                    progress[2] shouldBe 6
                }
            }
            val trie = TrieFactory.newTrie("a\nb\nc".lineSequence(), channel)
            withTimeout(100) {
                job.await()
            }
        }

        "load".config(timeout = Duration.ofMillis(100)) {
            val filePath = "test.dawg"
            val trie1 = TrieFactory.newTrie("a\nb\nc".lineSequence(), filePath = filePath)
            val trie2 = TrieFactory.newTrieFromFile(filePath)
            for (c in arrayOf("a","b","c")) {
                trie1.contains(c) shouldBe trie2.contains(c)
            }
        }

        "contains" {
            // sorted
            val trie = TrieFactory.newTrie(content_countries.lineSequence())
            trie.contains("Vietnam") shouldBe true
            trie.contains("Cambodia") shouldBe true
            trie.contains("Thailand") shouldBe true
            trie.contains("England") shouldBe false
        }

        "find" {
            val trie = TrieFactory.newTrie(content.lineSequence())
            for(prefix in arrayOf("a", "b", "c")) {
                trie.search(prefix).let {
                    it.shouldContainKey(prefix)
                    it.entries.forAll(mapEntryValueShouldBe0)
                }
            }
        }

        "find2" {
            val trie = TrieFactory.newTrie(content_countries.lineSequence())
            trie.search("V").let {
                it.shouldContainKey("Vietnam")
                it.entries.forAll(mapEntryValueShouldBe0)
            }
            trie.search("c").entries.forAll(mapEntryValueShouldBe0)
        }
    }

    override fun beforeTest(testCase: TestCase) {
        //trie = TrieFactory.newTrie("a\nb\nc".lineSequence(), channel)
    }

    companion object {
        private const val content = "a\nb\nc"
        // sorted!
        val content_countries = """
                Cambodia
                Laos
                Thailand
                Venezuela
                Vietnam
                Việt
                countries
                """.trimIndent()
        val mapEntryValueShouldBe0: (Map.Entry<*, Int>) -> Unit = {
            it.value shouldBe 0
        }
    }
}
