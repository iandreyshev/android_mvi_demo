package ru.iandreyshev.jokesfeed.domain.feed

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.iandreyshev.jokesfeed.domain.Filter
import ru.iandreyshev.jokesfeed.domain.Joke
import ru.iandreyshev.jokesfeed.domain.Topic
import ru.iandreyshev.jokesfeed.domain.core.ErrorType
import ru.iandreyshev.jokesfeed.domain.core.UseCaseResult
import ru.iandreyshev.jokesfeed.system.utils.CoroutineContextProvider

private const val DELAY = 4000L

class GetFeedUseCase(
    private val gateway: Gateway,
    private val contextProvider: CoroutineContextProvider
) {

    suspend operator fun invoke(filter: Filter): UseCaseResult<List<Joke>> =
        withContext(contextProvider.io) {
            when {
                filter.isEmpty -> {
                    val feed = gateway.get(filter)
                    UseCaseResult.Success(feed)
                }

                else -> {
                    delay(DELAY)
                    val feed = gateway.get(filter)
                        .filter { it.topic in filter.topics }

                    if (filter.topics.contains(Topic.TEAPOT)) {
                        return@withContext UseCaseResult.Error(ErrorType.NoInternetConnection)
                    }

                    UseCaseResult.Success(feed)
                }
            }
        }

}
