import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BrokenMoleculePresenterTest {
    @Test
    fun worksWithTurbine() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigatorWithTurbine()
        val key = "key"
        val token = "token"

        val presenter = BrokenMoleculeBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        testMoleculePresenter({ presenter.present(emptyFlow()) }) {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            runCurrent() // :(
            expectNoEvents()
            fakeBiometricsStore.readResults.add(token)

            assertEquals(PaymentScreen(token), fakeNavigator.screens.awaitItem())
            assertEquals(BiometricsUiModel.Done, awaitItem())
        }
    }

}