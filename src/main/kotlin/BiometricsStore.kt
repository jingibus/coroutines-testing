interface BiometricsStore {
    suspend fun read(key: String): String?
}