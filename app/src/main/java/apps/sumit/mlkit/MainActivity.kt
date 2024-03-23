package apps.sumit.mlkit

import android.Manifest
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import apps.sumit.mlkit.ui.theme.MLKitTheme
import apps.sumit.mlkit.util.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitTheme {
                // A surface container using the 'background' color from the theme

                val permissionState = rememberPermissionState(
                    permission = Manifest.permission.CAMERA
                )


                val context = LocalContext.current

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                permissionState.launchPermissionRequest()
                            }
                        }

                        lifecycleOwner.lifecycle.addObserver(observer = observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer = observer)
                        }
                    }
                )

                if(permissionState.status.isGranted){
                    Show()
                }else{
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        val scale = remember {
                            Animatable(0f)
                        }

                        LaunchedEffect(key1 = true) {
                            scale.animateTo(
                                targetValue = .5f,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = {
                                        OvershootInterpolator(5f).getInterpolation(it)
                                    }
                                )
                            )
                            delay(1000L)
                            Toast.makeText(context,"Camera Permission is Required",Toast.LENGTH_SHORT).show()
                        }

                        Image(
                            painter = painterResource(id = R.drawable.sadjapanesepatient),
                            contentDescription = "Camera not permitted",
                            modifier = Modifier.scale(scale.value)
                        )
                        Text(
                            text = "Camera Permission \n\n is Required",
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .scale(scale.value)
                                .padding(vertical = (scale.value).dp)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun Show() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =Screen.HomeScreen.route ){

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MLKitTheme {
        Show()
    }
}