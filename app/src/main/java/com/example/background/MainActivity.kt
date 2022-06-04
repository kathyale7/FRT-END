package com.example.background

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.background.Constant.Status
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    var task: MyAsyncTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startService(view: View) {
        if (task?.status == Status.RUNNING){
            task?.cancel(true)
        }
        task = MyAsyncTask(this)
        task?.execute(10)
    }

    class MyAsyncTask(private var activity: MainActivity?) : CoroutinesAsyncTask<Int, Int, String>("MysAsyncTask") {
        private var apiUrl: String = "http://192.168.0.13:8081/NewBackEnd/NewServlet"
        private var apiUrl2: String = "https://jsonplaceholder.typicode.com/users/1"
        override fun doInBackground(vararg params: Int?): String {
            var current = ""
            val jsonObject = JSONObject()
            try {
                jsonObject.put("id", "110470816")
                jsonObject.put("nombre", "JuanDeDios-MurilloMorera")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
           // apiUrl+="?opc=1&id=110470816";
            apiUrl += "?profe=$jsonObject"
            try {
                val url: URL
                var urlConnection: HttpURLConnection? = null
                try {
                    url = URL(apiUrl2)
                    urlConnection = url
                        .openConnection() as HttpURLConnection
                    val `in` = urlConnection.inputStream
                    val isw = InputStreamReader(`in`)
                    var data = isw.read()
                    while (data != -1) {
                        current += data.toChar()
                        data = isw.read()
                        print(current)
                    }
                    // return the data to onPostExecute method
                    return current
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return "Exception: " + e.message
            }
            Log.d("Current", current)
            return current
        }

        override fun onPostExecute(result: String?) {
            activity?.progressBar?.visibility = View.GONE
            activity?.output?.text = result
        }

        override fun onPreExecute() {
            activity?.output?.text = "Tast starting.."
            activity?.progressBar?.visibility = View.VISIBLE
            activity?.progressBar?.max = 10
            activity?.progressBar?.progress = 0

        }

        override fun onProgressUpdate(vararg values: Int?) {

        }
    }
}
