# Example of creating Debug DAO's

A debug DAO is a room DAO that is only available in the debug sourceset.

This allows you to separate database actions that are used for debugging & developer tools from database actions that are required for user
facing features.

Taking influence from the official example code:

- https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room#0
- https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app

And then augmenting with:

```
app/src/debug/java/com/blundell/tut/daoshop/DaoExtensions.kt
app/src/debug/java/com/blundell/tut/daoshop/DebugItemDao.kt
```

&

```
app/src/release/java/com/blundell/tut/daoshop/DaoExtensions.kt
```

![](/sourcesets.png)
