import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun asyncGetHttpRequest(
    endpoint: String,
    onSuccess: (ApiResponse<AmiiboResponse>) -> Unit,
    onError: (Exception) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(endpoint)
        val openedConnection = url.openConnection() as HttpURLConnection
        openedConnection.requestMethod = "GET"

        val responseCode = openedConnection.responseCode
        try {
            val reader = BufferedReader(InputStreamReader(openedConnection.inputStream))
            val response = reader.readText()
            val apiResponse = ApiResponse(
                responseCode,
                parseJson<AmiiboResponse>(response)
            )
            print(response)
            reader.close()
            // Call the success callback on the main thread
            launch(Dispatchers.Main) {
                onSuccess(apiResponse)
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
            // Handle error cases and call the error callback on the main thread
            launch(Dispatchers.Main) {
                onError(Exception("HTTP Request failed with response code $responseCode"))
            }
        } finally {

        }
    }
}

private inline fun <reified T>parseJson(text: String): T =
    Gson().fromJson(text, T::class.java)

data class ApiResponse<T>(
    val responseCode: Int,
    val response: T
)

data class AmiiboResponse(
    val amiibo: List<AmiiboItem>
)

data class AmiiboItem(
    val amiiboSeries: String,
    val character: String,
    val gameSeries: String,
    val head: String,
    val image: String,
    val name: String,
    val tail: String,
    val type: String
)