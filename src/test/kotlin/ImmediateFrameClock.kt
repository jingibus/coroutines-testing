import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.MonotonicFrameClock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


suspend fun withImmediateFrameClock(block: suspend ()->Unit) = coroutineScope {
    val ticks = Channel<Unit>(Channel.CONFLATED)
    val frameClock = BroadcastFrameClock()
    val job = launch { for (tick in ticks) frameClock.sendFrame(0) }
    val clock = object : MonotonicFrameClock {
        override suspend fun <R> withFrameNanos(onFrame: (frameTimeNanos: Long) -> R): R {
            ticks.trySend(Unit)
            return frameClock.withFrameNanos(onFrame)
        }
    }
    withContext(clock) { block() }
    job.cancel()
}
