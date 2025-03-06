package com.example.mostaqarapp.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.HashMap

open class FCMMessage {

    companion object{
         fun sendFCMMessage(token: String,text:String) {
            val serverKey = "AAAAK2tNqk4:APA91bEV34KVrxcWo6o1KCgd1apJxV_t2823vjCdyXxAf9G8m_0_yuL4bWJHDQ5hV7U498RqmeR6U0vX0PkomZmTe6TILoTEso6-ESAs4YAmvVBNP3aPsGQTVb5zjGRA4EH7ZhuWU9dz"
            val url = URL("https://fcm.googleapis.com/fcm/send")
            GlobalScope.launch {
                val connection = url.openConnection() as HttpURLConnection
                Log.e("byn","in blobal scope")

                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.doInput = true
                connection.addRequestProperty("Content-Type", "application/json")
                connection.addRequestProperty("Authorization", "key=$serverKey")

                val message = JSONObject()
                val data = JSONObject()
                data.put("message", text)
                message.put("to", token)
                message.put("data", data)

                val messageBytes = message.toString().toByteArray(Charsets.UTF_8)
                connection.setFixedLengthStreamingMode(messageBytes.size)

                connection.connect()

                val outputStream: OutputStream = BufferedOutputStream(connection.outputStream)
                outputStream.write(messageBytes)
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Message sent successfully
                    Log.e("byn","resopns is successfully ok")
                } else {
                    // Handle failure
                    Log.e("byn","respons failed")
                }
                connection.disconnect()
            }
        }

         fun deleteTokenFromHome(topicId:String, token: String){
            Firebase.firestore.collection("homes").get().addOnSuccessListener {
                for (document in it){
                    if (document.id == topicId){
                        Firebase.firestore.collection("homes").document(document.id).collection("tokens").get()
                            .addOnSuccessListener {
                                for (d in it){
                                    if(d.data["token"].toString() == token){
                                        Firebase.firestore.collection("homes").document(document.id).collection("tokens")
                                            .document(d.id).delete()
                                    }
                                }
                            }
                    }else{
                        Log.e("byn","document not equls $document")
                    }
                }
            }

        }

         fun addTokenToHome(topicId:String, map: HashMap<String, Any>, token: String){
            Firebase.firestore.collection("homes").get().addOnSuccessListener {
                for (document in it){
                    if (document.id == topicId){
                        var topic = Firebase.firestore.collection("homes").document(document.id).collection("tokens")

//                    topic.get().addOnSuccessListener {
//                        for(doc in it){
//                            if (doc.data["token"] == token){
//                                deleteTokenFromTopic(topicId,token)
//                            }else{
                        topic.add(map)
                            .addOnSuccessListener {
//                                        Toast.makeText(this, "token added success", Toast.LENGTH_SHORT).show()
                                Log.e("byn","token added success")
                            }.addOnFailureListener {
                                Log.e("byn","faild to add token in topic $it")
                            }
//                            }
//                        }
//                    }
                    }else{
                        Log.e("byn","document not equls $document")
                    }
                }
            }

        }

    }


}