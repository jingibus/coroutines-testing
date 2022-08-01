interface AppService {
    suspend fun validateToken(token: String): Boolean
}