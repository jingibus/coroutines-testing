import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.FlowTurbine
import app.cash.turbine.test

suspend fun <T> testMoleculePresenter(molecule: @Composable () -> T, body: suspend FlowTurbine<T>.() -> Unit) {
    withImmediateFrameClock {
        moleculeFlow(RecompositionClock.ContextClock, molecule).test(body)
    }
}
