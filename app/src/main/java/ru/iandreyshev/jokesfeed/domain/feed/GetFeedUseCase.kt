package ru.iandreyshev.jokesfeed.domain.feed

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.iandreyshev.jokesfeed.domain.Filter
import ru.iandreyshev.jokesfeed.domain.Joke
import ru.iandreyshev.jokesfeed.domain.core.UseCaseResult
import ru.iandreyshev.jokesfeed.system.utils.CoroutineContextProvider

class GetFeedUseCase(
    private val gateway: FeedGateway,
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
                    delay(5000)
                    val feed = gateway.get(filter)
                        .filter { it.topic in filter.topics }
                    UseCaseResult.Success(feed)
                }
            }
        }

}
