import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrokenBiometricsPresenter(
    private val biometricsStore: BiometricsStore,
    private val navigator: Navigator,
    private val biometricsKey: String,
) {
    fun present(events: Flow<Unit>): Flow<BiometricsUiModel> = flow {
        emit(BiometricsUiModel.Loading)
        emit(BiometricsUiModel.Done)

        val biometricsToken = biometricsStore.read(biometricsKey)


        if (biometricsToken != null) {
            navigator.goTo(PaymentScreen(biometricsToken))
        } else {
            navigator.goTo(HomeScreen)
        }
    }
}