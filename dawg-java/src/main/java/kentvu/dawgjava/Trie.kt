package kentvu.dawgjava

import kotlinx.coroutines.channels.Channel

interface Trie {
    fun search(prefix: String): PrefixSearchResult
    fun contains(key: String): Boolean
}


typealias PrefixSearchResult = Map<String, Int>
