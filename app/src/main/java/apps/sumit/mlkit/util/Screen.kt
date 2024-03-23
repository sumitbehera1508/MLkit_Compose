package apps.sumit.mlkit.util

sealed class Screen(val route:String) {
    data object HomeScreen :Screen("HomeScreen")
}