package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel

interface Trie {
    /**
     * @param progressListener Listen to progress by bytes read.
     */
    suspend fun build(seed: Sequence<String>, progressListener: Channel<Int>? = null)
    fun search(prefix: String): PrefixSearchResult
    fun contains(key: String): Boolean
}


typealias PrefixSearchResult = Map<String, Int>
