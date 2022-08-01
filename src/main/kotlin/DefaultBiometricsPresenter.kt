import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DefaultBiometricsPresenter(
    private val biometricsStore: BiometricsStore,
    private val navigator: Navigator,
    private val biometricsKey: String,
    private val appService: AppService,
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun present(events: Flow<Unit>): Flow<BiometricsUiModel> = flow {
        emit(BiometricsUiModel.Loading)

        val biometricsToken = biometricsStore.read(biometricsKey)

        emit(BiometricsUiModel.Done)

        scope.launch {
            if (biometricsToken != null &&
                appService.validateToken(biometricsToken)
            ) {
                navigator.goTo(PaymentScreen(biometricsToken))
            } else {
                navigator.goTo(HomeScreen)
            }
        }
    }
}