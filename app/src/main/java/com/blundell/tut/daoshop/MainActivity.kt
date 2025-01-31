package com.blundell.tut.daoshop

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blundell.tut.daoshop.ui.theme.DaoShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaoShopTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier
                            .padding(paddingValues = innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    private fun MainScreen(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .padding(24.dp)
        ) {
            Text(
                text = "Android Main Activity",
            )
            HiddenDebugMenuOption {
                // Use a classname string, so that when the code is compile for release
                // we do not have an issue with missing class (because debug sourceset won't be available)
                startActivity(
                    Intent()
                        .setClassName(
                            applicationContext.packageName,
                            "com.blundell.tut.daoshop.DebugActivity"
                        )
                )
            }
        }
    }
}

/**
 * This shows a button, only when a debug build type is active (i.e. dev builds)
 */
@Composable
private fun HiddenDebugMenuOption(onClick: () -> Unit) {
    if (BuildConfig.DEBUG) {
        Button(
            onClick = onClick
        ) {
            Text("Debug Menu")
        }
    }
}
