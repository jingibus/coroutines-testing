import newturbine.Turbine

class FakeBiometricsStoreWithTurbine : BiometricsStore {
    val readResults = Turbine<String>()

    override suspend fun read(key: String): String? {
        return readResults.awaitItem()
    }
}