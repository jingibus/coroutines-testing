import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.Flow

class MoleculeBiometricsPresenter(
    private val biometricsStore: BiometricsStore,
    private val navigator: Navigator,
    private val biometricsKey: String,
) {
    data class BiometricsResult(val token: String?)

    @Composable
    fun present(events: Flow<Unit>): BiometricsUiModel {
        var biometricsResult by remember { mutableStateOf<BiometricsResult?>(null) }

        LaunchedEffect(biometricsStore) {
            val biometricsToken = biometricsStore.read(biometricsKey)
            biometricsResult = BiometricsResult(biometricsToken)
        }
        if (biometricsResult != null) {
            LaunchedEffect(biometricsResult) {
                val biometricsToken = biometricsResult?.token
                if (biometricsToken != null) {
                    navigator.goTo(PaymentScreen(biometricsToken))
                } else {
                    navigator.goTo(HomeScreen)
                }
            }
        }

        return if (biometricsResult != null) BiometricsUiModel.Done
        else BiometricsUiModel.Loading
    }
}