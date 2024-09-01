package com.example.batteryalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.batteryalarm.ui.theme.BatteryAlarmTheme
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BatteryStatus(Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun BatteryStatus(modifier: Modifier=Modifier) {
    val context = LocalContext.current
    val batteryLevel = remember { mutableIntStateOf(0) }
    val batteryStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                batteryLevel.intValue = (level / scale.toFloat() * 100).toInt()
            }
        }
    }
    DisposableEffect(Unit) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryStatusReceiver, filter)
        onDispose {
            context.unregisterReceiver(batteryStatusReceiver)
        }
    }
    val batteryStatus = if (batteryLevel.intValue < 20) R.drawable.batterylowlow else R.drawable.batteryfull
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painter = painterResource(id = batteryStatus), contentDescription ="Battery Status" )
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BatteryPreview() {

    BatteryStatus()

}