package com.laznaslmi.siamil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(username: String, password: String) {
        val url = "http://103.179.216.69/apicoba/absen/slogin.php?username=$username&password=$password"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    runOnUiThread {
                        try {
                            val json = JSONObject(responseData)
                            if (json.has("response_status") && json.has("response_message")) {
                                val responseStatus = json.getString("response_status")
                                val responseMessage = json.getString("response_message")
                                Toast.makeText(this@MainActivity, responseMessage, Toast.LENGTH_SHORT).show()
                                if (responseStatus == "OK") {
                                    val dataArray = json.getJSONArray("data")
                                    if (dataArray.length() > 0) {
                                        val userData = dataArray.getJSONObject(0)
                                        val nip = userData.getString("nip")
                                        val nama = userData.getString("nama")
                                        val kotaLayanan = userData.getString("kota_layanan")
                                        val dep = userData.getString("dep")

                                        // Handle successful login here
                                        Toast.makeText(this@MainActivity, "Welcome $nama", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this@MainActivity, "Unexpected response format", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "Error parsing response: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
