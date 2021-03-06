package com.example.matchball.usersetting.userinfo

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matchball.firebaseconnection.AuthConnection
import com.example.matchball.firebaseconnection.DatabaseConnection
import com.example.matchball.firebaseconnection.StorageConnection
import com.example.matchball.model.User
import java.io.File

class UserInfoViewModel : ViewModel() {

    private val uid = AuthConnection.auth.currentUser!!.uid
//    private var imgUri: Uri? = null
//
//    fun setUri(uri: Uri) {
//        imgUri = uri
//    }

    val saveUserData = MutableLiveData<SaveUserData>()
    val loadUserData = MutableLiveData<LoadUserData>()

    sealed class LoadUserData {
        class LoadUserInfoSuccess(
            val name: String,
            val bio: String,
            val birthday: String,
            val email: String
        ) : LoadUserData()

        object LoadUserInfoFail : LoadUserData()
//        class LoadUserAvatarOk(val avatar: Bitmap) : LoadUserData()
//        object LoadUserAvatarFail : LoadUserData()
    }

    sealed class SaveUserData {
        class SaveOk(val message: String) : SaveUserData()
        class SaveFail(val message: String) : SaveUserData()
    }

    fun handleLoadUserData() {
//        val localFile = File.createTempFile("tempImage", "jpg")
//        StorageConnection.handleAvatar(uid = uid, localFile = localFile, onSuccess = {
//            loadUserData.postValue(LoadUserData.LoadUserAvatarOk(it))
//        }, onFail = {
//            loadUserData.postValue(LoadUserData.LoadUserAvatarFail)
//        })

        DatabaseConnection.databaseReference.getReference("Users").child(uid).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val name = it.child("teamName").value.toString()
                    val bio = it.child("teamBio").value.toString()
                    val birthday = it.child("birthday").value.toString()
                    val email = it.child("email").value.toString()

                    loadUserData.postValue(
                        LoadUserData.LoadUserInfoSuccess(
                            name,
                            bio,
                            birthday,
                            email
                        )
                    )
                } else {
                    loadUserData.postValue(LoadUserData.LoadUserInfoFail)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun handleSaveUserData(
        teamName: String,
        teamBio: String,
        teamBirthday: String,
        teamEmail: String
    ) {
        val user = User(teamName, teamBio, teamBirthday, teamEmail)
        DatabaseConnection.databaseReference.getReference("Users").child(uid).setValue(user)
            .addOnCompleteListener {
                saveUserData.postValue(SaveUserData.SaveOk("Save Profile Success"))
            }.addOnFailureListener {
                saveUserData.postValue(SaveUserData.SaveFail("Failed to Save Profile"))
            }
    }
}
