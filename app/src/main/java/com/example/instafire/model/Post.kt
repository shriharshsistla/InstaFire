package com.example.instafire.model

import com.google.firebase.firestore.PropertyName

data class Post (
    var description:String="",
    @get:PropertyName("image_url")@set:PropertyName("image_url")var imageUrl:String="",
    @get:PropertyName("creation time") @set:PropertyName("creation time")var creationTime:Long=0,
    var user:User?=null

        )