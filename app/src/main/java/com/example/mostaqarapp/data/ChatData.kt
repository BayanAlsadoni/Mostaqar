package com.example.mostaqarapp.data

import com.example.mostaqarapp.R


data class ChatData(var image: Int= R.drawable.ic_person_outline,
                    var personName:String="",
                    var message:String="",
                    var senderId : String = "",
                    var receiverId : String ="",
                    var timestamp: String="", )
