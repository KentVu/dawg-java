package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel

interface Trie {
    /**
     * @param progressListener Listen to progress by bytes read.
     */
    suspend fun build(seed: WordSequence, progressListener: Channel<Int>? = null)
    fun search(prefix: String): Map<String, Int>
    fun contains(key: String): Boolean
}