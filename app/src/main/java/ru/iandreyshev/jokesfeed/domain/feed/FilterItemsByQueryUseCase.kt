package ru.iandreyshev.jokesfeed.domain.feed

import ru.iandreyshev.jokesfeed.domain.Joke

class FilterItemsByQueryUseCase {

    operator fun invoke(items: List<Joke>, query: String): List<Joke> =
        items.filter { it.text.contains(query, ignoreCase = true) }

}
