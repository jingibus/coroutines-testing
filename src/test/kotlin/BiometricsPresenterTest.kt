import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BiometricsPresenterTest {
    @Test
    fun works() = runTest {
        val fakeBiometricsStore = FakeBiometricsStore()
        val fakeNavigator = FakeNavigator()
        val key = "key"
        val token = "token"
        fakeBiometricsStore.data[key] = token

        val presenter = BiometricsPresenter(fakeBiometricsStore, fakeNavigator, key)
        presenter.present(emptyFlow()).test {
            assertEquals(BiometricsUiModel.Loading, awaitItem())
            assertEquals(PaymentScreen(token), fakeNavigator.nextScreen)

            assertEquals(BiometricsUiModel.Done, awaitItem())
            awaitComplete()
        }
    }
}