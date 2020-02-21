package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel

//@file:JvmName("DawgTrie")
class DawgTrie: Trie {
    companion object {
        init {
            System.loadLibrary("dawg-jni")
        }
    }
    private external fun dawgBuilder(path: String): Long
    private external fun dawgBuilderInsert(dawgBuilderPtr: Long, word: String)
    private external fun dawgBuilderFinish(dawgBuilderPtr: Long)

    fun insert(s: String) {
        words.add(s)
    }

    private val words = sortedSetOf<String>()
    override suspend fun build(seed: WordSequence, progressListener: Channel<Int>?) {
        var count = 0
        var dawgBuilderPtr = dawgBuilder("trie.dawg")
        for (word in seed) {
            dawgBuilderInsert(dawgBuilderPtr, word)
        }
        dawgBuilderFinish(dawgBuilderPtr)
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

