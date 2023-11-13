package ru.iandreyshev.jokesfeed.domain

data class Filter(
    val topics: Set<Topic>
) {

    val isEmpty: Boolean
        get() = topics.isEmpty()

    companion object {
        fun empty() = Filter(
            topics = setOf()
        )
    }

}
