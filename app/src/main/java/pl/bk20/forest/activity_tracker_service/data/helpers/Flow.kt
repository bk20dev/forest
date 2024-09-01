package pl.bk20.forest.activity_tracker_service.data.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.runningFold

data class StateTransition<T>(val previous: T?, val current: T)

/**
 * Transforms the incoming flow, by pairing each element with its previous value.
 *
 * The returned flow emits a [StateTransition] for each item in the original flow, where:
 * - `previous` is the previously emitted value or `null` if it's the first emission.
 * - `current` is the current value being emitted in the flow.
 */
fun <T> Flow<T>.withPreviousValue(): Flow<StateTransition<T>> {
    return runningFold(
        initial = null as StateTransition<T>?
    ) { accumulator, value ->
        StateTransition(
            previous = accumulator?.current,
            current = value,
        )
    }.filterNotNull()
}