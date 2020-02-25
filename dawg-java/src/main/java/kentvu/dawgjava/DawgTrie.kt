package kentvu.dawgjava

import dawgswig.*
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
    private val dic = dawgswig.Dictionary()

    override suspend fun build(seed: Sequence<String>, progressListener: Channel<Int>?) {
        var count = 0

        val dawgBuilder = dawgswig.DawgBuilder()
        seed.forEach {
            dawgBuilder.Insert(it)
            count += it.size + ESTIMATED_LINEBREAK_SIZE /*the line ending*/
            progressListener?.send(count)
        }
        // Finishes building a simple dawg.
        val dawg = Dawg()
        dawgBuilder.Finish(dawg)

        // Builds a dictionary from a simple dawg.
        DictionaryBuilder.Build(dawg, dic);
        progressListener?.close()
    }

    override fun contains(key: String): Boolean {
        return dic.Contains(key)
    }

    override fun search(prefix: String): PrefixSearchResult {
        val index = dic.root()
        dic.Follow(prefix, prefix.length, index)
        return SwigPrefixSearchResult(swigSearch)
    }
}

object TrieFactory {
    fun newTrie(): Trie {
        return DawgTrie()
    }
}

