package kentvu.dawgjava.android.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import kentvu.dawgjava.TrieFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

// verify DawgTrie works on Android
@RunWith(AndroidJUnit4::class)
class DawgAndroidTest {
    @Test
    fun buildDawg() = runBlocking {
        val trie = TrieFactory.newTrie()
        trie.build("a\nb\nc".lineSequence())
        assertTrue(trie.contains("a"))
    }
}
