import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class InjectedContextBiometricsPresenter(
    private val biometricsStore: BiometricsStore,
    private val navigator: Navigator,
    private val biometricsKey: String,
    private val appService: AppService,
    backgroundContext: CoroutineContext,
    outerScope: CoroutineScope,
) {
    private val tokenSubmissions = Channel<String?>(Channel.BUFFERED)
    init {
        outerScope.launch(backgroundContext) {
            for (biometricsToken in tokenSubmissions) {
                if (biometricsToken != null &&
                    appService.validateToken(biometricsToken)) {
                    navigator.goTo(PaymentScreen(biometricsToken))
                } else {
                    navigator.goTo(HomeScreen)
                }
            }
        }
    }

    fun present(events: Flow<Unit>): Flow<BiometricsUiModel> = flow {
        emit(BiometricsUiModel.Loading)
        val biometricsToken = biometricsStore.read(biometricsKey)
        emit(BiometricsUiModel.Done)
        tokenSubmissions.send(biometricsToken)
    }
}