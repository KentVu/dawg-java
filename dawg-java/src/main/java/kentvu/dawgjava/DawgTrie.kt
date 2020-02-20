package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel
import java.io.File

class DawgTrie: Trie {
    companion object {
        init {
            System.loadLibrary("dawg-jni")
        }
    }
    external fun saveDawg(inFile: String)

    fun insert(s: String) {
        words.add(s)
    }

    fun save() {
        System.out.println(words)
        val filename = "TestDawg.txt"
        File(filename).bufferedWriter().use { out ->
            words.forEach {
                out.write(it)
                out.newLine()
            }
        }
        saveDawg(filename)
    }

    private val words = sortedSetOf<String>()
    override suspend fun build(seed: WordSequence, progressListener: Channel<Int>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun search(prefix: String): Map<String, Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(key: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

object TrieFactory {
    fun newTrie(): Trie {
        return DawgTrie()
    }
}

