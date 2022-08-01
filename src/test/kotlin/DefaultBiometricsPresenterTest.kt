import app.cash.turbine.test
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.assertEquals

class DefaultBiometricsPresenterTest {
    @Test
    fun works() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigatorWithTurbine()
        val fakeAppService = FakeAppService()
        val key = "key"
        val token = "token"

        val presenter = DefaultBiometricsPresenter(fakeBiometricsStore, fakeNavigator, key, fakeAppService)
        presenter.present(emptyFlow()).test {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            fakeBiometricsStore.readResults.add(token)
            fakeAppService.validateTokenResponses.add(true)

            assertEquals(PaymentScreen(token), fakeNavigator.screens.awaitItem())
            assertEquals(BiometricsUiModel.Done, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun worksWithInjection() = runTest {
        val fakeBiometricsStore = FakeBiometricsStoreWithTurbine()
        val fakeNavigator = FakeNavigatorWithTurbine()
        val fakeAppService = FakeAppService()
        val key = "key"
        val token = "token"

        runTest {
            val presenter = InjectedContextBiometricsPresenter(
                fakeBiometricsStore,
                fakeNavigator,
                key,
                fakeAppService,
                EmptyCoroutineContext,
                this,
            )

            val flow = presenter.present(emptyFlow())

            flow.test {
                assertEquals(BiometricsUiModel.Loading, awaitItem())
                fakeBiometricsStore.readResults.add(token)
                fakeAppService.validateTokenResponses.add(true)

                assertEquals(PaymentScreen(token), fakeNavigator.screens.awaitItem())
                assertEquals(BiometricsUiModel.Done, awaitItem())
                awaitComplete()
            }
        }
    }
}