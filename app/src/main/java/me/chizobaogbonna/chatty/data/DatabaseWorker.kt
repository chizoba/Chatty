package me.chizobaogbonna.chatty.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope
import me.chizobaogbonna.chatty.domain.models.Data

const val DATA_FILENAME = "data.json"

class DatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val dataType = object : TypeToken<Data>() {}.type
                    val data: Data = Gson().fromJson(jsonReader, dataType)

                    val database = AppDatabase.getInstance(applicationContext)

                    database.messageDao().insertAll(data.messages)
                    database.userDao().insertAll(data.users)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            // if any data (messages or users  data) fail to be added, throw an error
            Log.e(TAG, "Error adding data to database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = DatabaseWorker::class.java.simpleName
    }
}
