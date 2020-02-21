package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel

private val String.size: Int
    get() = toByteArray().size

// TODO: Stop estimating and add a parameter (May Android won't change the line ending convention :) )
const val ESTIMATED_LINEBREAK_SIZE = 1 // assume linux/mac line endings. (\r, \n only)

//@file:JvmName("DawgTrie")
class DawgTrie: Trie {
    companion object {
        init {
            System.loadLibrary("dawg-jni")
        }
    }

    fun insert(s: String) {
        words.add(s)
    }

    private val words = sortedSetOf<String>()
    override suspend fun build(seed: Sequence<String>, progressListener: Channel<Int>?) {
        val dawgSwig = dawgswig.DawgSwig("test.dawg")
        var count = 0
        seed.forEach {
            dawgSwig.Insert(it)
            count += it.size + ESTIMATED_LINEBREAK_SIZE /*the line ending*/
            progressListener?.send(count)
        }
        dawgSwig.Finish()
        progressListener?.close()
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

