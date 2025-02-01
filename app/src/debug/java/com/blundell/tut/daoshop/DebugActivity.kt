package com.blundell.tut.daoshop

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.blundell.tut.daoshop.ui.theme.DaoShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DebugActivity : ComponentActivity() {

    // Just an example, use the repository pattern etc in a full app
    private lateinit var debugItemDao: DebugItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        debugItemDao = ShopDatabase.getDatabase(applicationContext).debugItemDao()
        enableEdgeToEdge()
        setContent {
            DaoShopTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DebugScreen(
                        modifier = Modifier
                            .padding(paddingValues = innerPadding)
                    ) {
                        // Not how to do this properly, just for this brevity in this tut
                        scope.launch(context = Dispatchers.IO) {
                            Log.d("TUT", "Deleting all items in the database.")
                            debugItemDao.deleteAll()
                            Log.d("TUT", "Deleted all items in the database.")
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DebugScreen(
    modifier: Modifier = Modifier,
    onDeleteAllClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(24.dp)
    ) {
        Text(
            text = "Android Debug Activity",
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "This activity is only available in the debug sourceset, and will not be packaged in the release apk.",
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Here you can access the DebugItemDao (and the ItemDao).",
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = onDeleteAllClick,
        ) {
            Text("*Warning* Delete all items in the database.")
        }
    }
}
