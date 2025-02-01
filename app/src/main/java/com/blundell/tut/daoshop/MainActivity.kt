package com.blundell.tut.daoshop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blundell.tut.daoshop.ui.theme.DaoShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    // Just an example, use the repository pattern etc in a full app
    private lateinit var itemDao: ItemDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemDao = ShopDatabase.getDatabase(applicationContext).itemDao()
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            DaoShopTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val items: List<Item> by itemDao.streamAll()
                        .collectAsStateWithLifecycle(emptyList())
                    MainScreen(
                        modifier = Modifier
                            .padding(paddingValues = innerPadding),
                        items = items.map { "${it.id} - ${it.name} - ${"$%.2f".format(it.price)}" }
                    ) {
                        // Not how to do this properly, just for the brevity in this tut
                        scope.launch(context = Dispatchers.IO) {
                            Log.d("TUT", "Adding a cat to the database.")
                            itemDao
                                .insert(
                                    Item(
                                        id = 0,
                                        name = "Cat${Random.nextInt()}",
                                        price = 1.0,
                                    )
                                )
                            Log.d("TUT", "Added a cat to the database.")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun MainScreen(
        modifier: Modifier = Modifier,
        items: List<String>,
        onInsertClick: () -> Unit,
    ) {
        Column(
            modifier = modifier
                .padding(24.dp)
        ) {
            Text(
                text = "Android Main Activity",
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "Here you can access the ItemDao only. Visit the DebugMenu to access the DebugItemDao.",
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = onInsertClick,
            ) {
                Text("Add a cat!")
            }
            HiddenDebugMenuOption()
            Spacer(modifier = Modifier.padding(16.dp))
            DatabaseView(items)
        }
    }

    /**
     * This shows a button, only when a debug build type is active (i.e. dev builds)
     */
    @Composable
    private fun HiddenDebugMenuOption() {
        if (BuildConfig.DEBUG) {
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    // Use a classname string, so that when the code is compile for release
                    // we do not have an issue with missing class (because debug sourceset won't be available)
                    startActivity(
                        Intent()
                            .setClassName(
                                applicationContext.packageName,
                                "com.blundell.tut.daoshop.DebugActivity"
                            )
                    )
                },
            ) {
                Text("Debug Menu")
            }
        }
    }

    @Composable
    private fun DatabaseView(items: List<String>) {
        LazyColumn {
            items(items) {
                Text(it)
            }
            if (items.isEmpty()) {
                item {
                    Text("Database empty. Try adding a cat!")
                }
            }
        }
    }
}
