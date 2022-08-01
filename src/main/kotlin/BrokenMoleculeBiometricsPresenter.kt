import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.Flow

class BrokenMoleculeBiometricsPresenter(
    private val biometricsStore: BiometricsStore,
    private val navigator: Navigator,
    private val biometricsKey: String,
) {
    @Composable
    fun present(events: Flow<Unit>): BiometricsUiModel {
        var completed by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            completed = true
        }

        LaunchedEffect(navigator) {
            val biometricsToken = biometricsStore.read(biometricsKey)
            if (biometricsToken != null) {
                navigator.goTo(PaymentScreen(biometricsToken))
            } else {
                navigator.goTo(HomeScreen)
            }
        }

        return if (completed) BiometricsUiModel.Done
        else BiometricsUiModel.Loading
    }
}