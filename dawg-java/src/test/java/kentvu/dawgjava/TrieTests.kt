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
    private lateinit var trie: Trie

    init {
        "build".config(timeout = Duration.ofMillis(100)) {
            val channel = Channel<Int>()
            val job = GlobalScope.async {
                val progress = channel.toList()
                assertSoftly {
                    progress[0] shouldBe 2
                    progress[1] shouldBe 4
                    progress[2] shouldBe 6
                }
            }
            trie.build(content.lineSequence(), channel)
            withTimeout(100) {
                job.await()
            }
        }

        "contains" {
            // sorted
            trie.build(content_countries.lineSequence())
            trie.contains("Vietnam") shouldBe true
            trie.contains("Cambodia") shouldBe true
            trie.contains("Thailand") shouldBe true
            trie.contains("England") shouldBe false
        }

        "find" {
            trie.build(content.lineSequence())
            val shouldBe0: (Map.Entry<String, Int>) -> Unit = {
                it.value shouldBe 0
            }
            trie.search("a").let {
                it.entries.let {
                    it.forAll(shouldBe0)
                }
                it.shouldContainKey("a")
            }
            for(prefix in arrayOf("a", "b", "c")) {
                trie.search(prefix).let {
                    it.shouldContainKey(prefix)
                    it.entries.forAll(shouldBe0)
                }
            }
        }

        "find2" {
            trie.build(content_countries.lineSequence())
            val shouldBe0: (Map.Entry<String, Int>) -> Unit = {
                it.value shouldBe 0
            }
            trie.search("V").let {
                it.shouldContainKey("Vietnam")
                it.entries.forAll(shouldBe0)
            }
            trie.search("c").entries.forAll(shouldBe0)
        }
    }

    override fun beforeTest(testCase: TestCase) = runBlocking {
        trie = TrieFactory.newTrie()
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
                countries
                """.trimIndent()
    }
}

private fun String.wordSequence(): WordSequence {
    return WordSequence.new(this)
}
