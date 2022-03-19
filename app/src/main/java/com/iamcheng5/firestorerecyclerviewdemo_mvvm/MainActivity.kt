package com.iamcheng5.firestorerecyclerviewdemo_mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.utils.linear
import com.drake.brv.utils.setDifferModels
import com.drake.brv.utils.setup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val postViewModel: PostViewModel by viewModels()
    private val postRec: RecyclerView by lazy { findViewById(R.id.mainAct_rec_post) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerview()
        //loadPosts()
        getPosts()

        val newPostActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    addPosts(result.data!!.getParcelableExtra(NewPostActivity.EXTRA_REPLY)!!)
                } else {
                    Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG)
                        .show()
                }
            }

        val newPostBtn = findViewById<FloatingActionButton>(R.id.mainAct_btn_new_post)
        newPostBtn.setOnClickListener {
            newPostActivityLauncher.launch(Intent(this@MainActivity, NewPostActivity::class.java))
        }

        //get data once
        /*
        val refreshBtn = findViewById<FloatingActionButton>(R.id.mainAct_btn_refresh)

        refreshBtn.setOnClickListener {
            loadPosts()
        }
        */

    }
    //listen for realtime updates
    private fun getPosts(){
        postViewModel.getPosts().observe(this@MainActivity,{list->
            lifecycleScope.launch {
                withContext(Dispatchers.Default){
                    val newList = list.toList()
                    postRec.setDifferModels(newList)
                }
            }
        })
    }

    private fun addPosts(post: Post) {
        postViewModel.addPost(post).observe(this@MainActivity, { result ->
            if (result is Result.Success)
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(
                        if (result.data) R.string.post_added else R.string.post_not_added,
                        post.title
                    ), Toast.LENGTH_SHORT
                ).show()
        })
    }

    private fun removePosts(post: Post) {
        postViewModel.removePost(post).observe(this, { result ->
            if (result is Result.Success)
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(
                        if (result.data) R.string.post_removed else R.string.post_not_removed,
                        post.title
                    ),
                    Toast.LENGTH_SHORT
                ).show()
        })
    }
    //get data once
    /*
    private fun loadPosts() {
        postViewModel.getPosts().observe(this, { result ->
            if (result is Result.Success)
                postRec.setDifferModels(result.data)
        })
    }
    */

    private fun initRecyclerview() {
        postRec.linear().setup {
            addType<Post>(R.layout.recyclerview_post_item)
            itemDifferCallback = object : ItemDifferCallback {
                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                    return (oldItem as Post).id == (newItem as Post).id
                }

                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                    return oldItem == newItem
                }

                override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
                    return true
                }
            }
            R.id.postItemRec_btn_delete.onClick{
                removePosts(getModel())
            }
            onBind {
                findView<TextView>(R.id.postItemRec_tv_title).text = getModel<Post>().title
                findView<TextView>(R.id.postItemRec_tv_description).text = getModel<Post>().description
                findView<TextView>(R.id.postItemRec_tv_content).text = getModel<Post>().content
            }

        }
    }
}