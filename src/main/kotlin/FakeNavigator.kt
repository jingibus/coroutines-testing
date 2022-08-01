class FakeNavigator : Navigator {
    var nextScreen : Any? = null
    override fun goTo(screen: Any) {
        nextScreen = screen
    }
}