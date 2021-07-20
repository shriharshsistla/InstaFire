package com.example.instafire

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instafire.model.Post
import com.example.instafire.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_posts.*


private const val TAG = "PostsActivity"
const val EXTRA_USERNAME = "EXTRA_USERNAME"
open class PostsActivity : AppCompatActivity() {

    private var signedInUser: User?=null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)


        posts= mutableListOf()

        adapter = PostsAdapter(this,posts)

        rvposts.adapter=adapter
        rvposts.layoutManager = LinearLayoutManager(this)
        firestoreDb= FirebaseFirestore.getInstance()

        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
             .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG,"singed in user: $signedInUser")

            }
            .addOnFailureListener { exception->
                Log.i(TAG,"Fetching Failure user",exception)
            }


        firestoreDb = FirebaseFirestore.getInstance()
        var postsReference = firestoreDb
            .collection("posts")
            .limit(20)
            .orderBy("creation time",Query.Direction.DESCENDING)

        val userName = intent.getStringExtra(EXTRA_USERNAME)
        if(userName !=null){

            supportActionBar?.title = userName


           postsReference = postsReference.whereEqualTo("user.username",userName)

        }

        postsReference.addSnapshotListener { snapshot , exception ->
            if(exception!=null||snapshot==null){
                Log.e(TAG,"Exception in query posts",exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for(post in postList){
                Log.i(TAG,"Post ${post}")
            }
        }

        fabCreate.setOnClickListener{
            val intent = Intent(this,CreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menuProfile){
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME,signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}