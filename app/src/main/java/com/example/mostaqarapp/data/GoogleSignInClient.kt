package com.example.mostaqarapp.data

import android.provider.Settings.Global.getString
//import androidx.credentials.CustomCredential
//import androidx.credentials.GetCredentialRequest
//import androidx.credentials.GetCredentialResponse
import com.example.mostaqarapp.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions


class GoogleSignInClient {
//    suspend fun signIn():Boolean{
//
//    }
//    private suspend fun handleSignIn(result: GetCredentialResponse):Boolean{
//        val credential = result.credential
//        if(credential is CustomCredential
////            &&
////            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
//        ){
//
////            val signInRequest = BeginSignInRequest.builder()
////                .setGoogleIdTokenRequestOptions(
////                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
////                        .setSupported(true)
////                        // Your server's client ID, not your Android client ID.
////                        .setServerClientId(getString(R.string.your_web_client_id))
////                        // Only show accounts previously used to sign in.
////                        .setFilterByAuthorizedAccounts(true)
////                        .build())
////                .build()
//
//
//
//
////            try {
////                val tokenCredential = GoogleIdTokenCredential.createForm(credential.data)
////
////
////            }catch (e:GoogleIdTokenParsingException){
////
////
////            }
//            return true
//        }
//    return true
//    }


}