import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BrokenBiometricsPresenterTest {

    @Test
    fun works() = runTest {
        val fakeBiometricsStore = FakeBiometricsStore()
        val fakeNavigator = FakeNavigator()
        val key = "key"
        val token = "token"

        fakeBiometricsStore.data[key] = token

        val presenter = BrokenBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        presenter.present(emptyFlow()).test {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            assertEquals(PaymentScreen(token), fakeNavigator.nextScreen)

            assertEquals(BiometricsUiModel.Done, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun waitForResponseEmitDoneV2() = runTest {
        val fakeBiometricsStore = FakeBiometricsStore()
        val fakeNavigator = FakeNavigator()
        val key = "key"
        val token = "token"

        fakeBiometricsStore.data[key] = token

        val presenter = BrokenBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        presenter.present(emptyFlow()).test {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun waitForResponseEmitDoneWithTurbine() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigator()
        val key = "key"
        val token = "token"

        val presenter = BrokenBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        presenter.present(emptyFlow()).test {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun worksWithTurbine() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigator()
        val key = "key"
        val token = "token"

        val presenter = BiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        presenter.present(emptyFlow()).test {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            expectNoEvents()
            fakeBiometricsStore.readResults.add(token)

            assertEquals(BiometricsUiModel.Done, awaitItem())
            assertEquals(PaymentScreen(token), fakeNavigator.nextScreen)

            awaitComplete()
        }
    }

}