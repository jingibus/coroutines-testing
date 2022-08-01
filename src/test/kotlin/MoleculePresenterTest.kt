import androidx.compose.runtime.Composable
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Test
import kotlin.test.assertEquals

class MoleculePresenterTest {
    @Test
    fun works() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigator()
        val key = "key"
        val token = "token"

        val presenter = MoleculeBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        testMoleculePresenter({ presenter.present(emptyFlow()) }) {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            expectNoEvents()
            fakeBiometricsStore.readResults.add(token)

            assertEquals(PaymentScreen(token), fakeNavigator.nextScreen)
            assertEquals(BiometricsUiModel.Done, awaitItem())
        }
    }

    @Test
    fun worksWithTurbine() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigatorWithTurbine()
        val key = "key"
        val token = "token"

        val presenter = MoleculeBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        testMoleculePresenter({ presenter.present(emptyFlow()) }) {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            expectNoEvents()
            fakeBiometricsStore.readResults.add(token)

            assertEquals(PaymentScreen(token), fakeNavigator.screens.awaitItem())
            assertEquals(BiometricsUiModel.Done, awaitItem())
        }
    }

}