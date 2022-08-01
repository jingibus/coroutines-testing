import newturbine.Turbine

class FakeNavigatorWithTurbine : Navigator {
    val screens = Turbine<Any>()

    override fun goTo(screen: Any) {
        screens.add(screen)
    }
}