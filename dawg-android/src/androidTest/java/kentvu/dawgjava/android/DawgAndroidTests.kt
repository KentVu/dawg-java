package kentvu.dawgjava.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kentvu.dawgjava.TrieFactory
import org.junit.runner.RunWith

// verify DawgTrie works on Android
@RunWith(AndroidJUnit4::class)
class DawgAndroidTest: StringSpec({
    "buildDawg" {
        val trie = TrieFactory.newTrie()
        trie.build("a\nb\nc".lineSequence())
        (trie.contains("a")) shouldBe true
    }
})