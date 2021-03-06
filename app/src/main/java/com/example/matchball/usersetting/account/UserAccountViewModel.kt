package com.example.matchball.usersetting.account

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.AuthConnection.authUser
import com.example.matchball.firebaseconnection.StorageConnection
import com.example.matchball.usersetting.userinfo.UserInfoViewModel
import java.io.File

class UserAccountViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
    val loadData = MutableLiveData<UserData>()
    val verifyEmail = MutableLiveData<VerifyEmail>()
    val saveUserData = MutableLiveData<SaveUserData>()

    private var imgUri: Uri? = null

    fun setUri(uri: Uri) {
        imgUri = uri
    }

    sealed class SaveUserData {
        class SaveOk(val message: String) : SaveUserData()
        class SaveFail(val message: String) : SaveUserData()
    }

    sealed class UserData {
        class LoadAvatarSuccess(val image: Bitmap) : UserData()
        class LoadPhoneSuccess(val phone : String) : UserData()
        object LoadDataFail : UserData()
    }

    sealed class VerifyEmail {
        class EmailVerifySuccess(val successMessage : String) : VerifyEmail()
        class EmailVerifyFail(val errorMessage : String) : VerifyEmail()
    }

    fun handleLoadAvatar() {
        val email = authUser?.phoneNumber
        loadData.postValue(email?.let
        { UserData.LoadPhoneSuccess(it) })

        val localFile = File.createTempFile("tempImage", "jpg")
        StorageConnection.handleAvatar(uid = uid, localFile =  localFile, onSuccess = {
            loadData.postValue(UserData.LoadAvatarSuccess(it))
        }, onFail = {
            loadData.postValue(UserData.LoadDataFail)
        })
    }

    fun handleSaveAvatar() {
        imgUri?.let { it ->
            StorageConnection.storageReference.getReference("Users/" + AuthConnection.auth.currentUser?.uid)
                .putFile(it).addOnSuccessListener {
                    saveUserData.postValue(SaveUserData.SaveOk("Save Profile Success"))
                }.addOnFailureListener {
                    saveUserData.postValue(SaveUserData.SaveFail("Failed to Save Profile"))
                }
        }
    }

    fun handleVerifyEmail() {
        authUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                verifyEmail.postValue(VerifyEmail.EmailVerifySuccess("Send Verify Email Success"))
            } else {
                verifyEmail.postValue(VerifyEmail.EmailVerifyFail("Send Verify Email Fail"))
            }
        }
    }
}