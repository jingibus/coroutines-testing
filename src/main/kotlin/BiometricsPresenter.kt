import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BiometricsPresenter(
    private val biometricsStore: BiometricsStore,
    private val navigator: Navigator,
    private val biometricsKey: String,
) {
    fun present(events: Flow<Unit>): Flow<BiometricsUiModel> = flow {
        emit(BiometricsUiModel.Loading)

        val biometricsToken = biometricsStore.read(biometricsKey)

        emit(BiometricsUiModel.Done)

        if (biometricsToken != null) {
            navigator.goTo(PaymentScreen(biometricsToken))
        } else {
            navigator.goTo(HomeScreen)
        }
    }
}