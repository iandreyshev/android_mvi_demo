package ru.iandreyshev.jokesfeed.domain.feed

import ru.iandreyshev.jokesfeed.domain.Joke

class FilterJokesByQueryUseCase {

    operator fun invoke(jokes: List<Joke>, query: String): List<Joke> =
        jokes.filter { it.text.contains(query, ignoreCase = true) }

}
