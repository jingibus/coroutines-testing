class FakeBiometricsStore : BiometricsStore {
    val data = mutableMapOf<String,String>()

    override suspend fun read(key: String): String? {
        return data[key]
    }
}