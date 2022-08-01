import newturbine.Turbine

class FakeAppService : AppService {
    val validateTokenRequests = Turbine<String>()
    val validateTokenResponses = Turbine<Boolean>()

    override suspend fun validateToken(token: String): Boolean {
        validateTokenRequests.add(token)
        return validateTokenResponses.awaitItem()
    }
}