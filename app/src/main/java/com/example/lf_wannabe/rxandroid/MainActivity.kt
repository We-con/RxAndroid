package com.example.lf_wannabe.rxandroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.lf_wannabe.rxandroid.recyclerview.PostAdapter
import com.example.lf_wannabe.rxandroid.model.Post
import com.example.lf_wannabe.rxandroid.recyclerview.ListAdapterWithHeader
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var selectedID: Int = -1
    var postAdapter: PostAdapter = PostAdapter(this, false, Post(-1))
    private lateinit var fabs: java.util.ArrayList<FloatingActionButton>
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var vlist = ArrayList<Post>()

        postAdapter.setData(vlist)
        postAdapter.setOnItemClickListener(object : ListAdapterWithHeader.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                Toast.makeText(applicationContext, position.toString() + " selected", Toast.LENGTH_SHORT).show()
                selectedID = position
            }
        })

        with(list){
            setHasFixedSize(false)
            adapter = postAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        setFabActions()
    }

    fun setFabActions() {
        fabs = ArrayList<FloatingActionButton>().apply {
            add(fabActionC)
            add(fabActionR)
            add(fabActionD)
        }
        fab.setOnClickListener(fabListener(-1))
        for((index, fab) in fabs.withIndex()) fab.setOnClickListener(fabListener(index))
    }

    private fun fabListener(type: Int) = View.OnClickListener {
        when(type){
            0 -> create()
            1 -> read()
            2 -> delete()
            else -> null
        }
        animateFab()
    }

    private fun animateFab(){
        when(isFabOpen){
            true -> closeFab()
            false -> openFab()
        }
        isFabOpen = !isFabOpen
    }

    private fun openFab() {
        AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_forward)?.let {
            fab.startAnimation(it)
        }

        AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)?.let {
            for(fab in fabs) fab.startAnimation(it)
        }
        for(fab in fabs) fab.isClickable = true
    }

    private fun closeFab() {
        AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_backword)?.let {
            fab.startAnimation(it)
        }

        AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)?.let {
            for(fab in fabs) fab.startAnimation(it)
        }
        for(fab in fabs) fab.isClickable = false
    }
    private fun create(){
        startActivity(Intent(this, CrtPostActivity::class.java))
    }

    private fun read(){
        getPosts()
    }

    private fun delete(){
        if(selectedID == -1)
            Toast.makeText(applicationContext,
                    "select a post to delete", Toast.LENGTH_SHORT).show()
        else {
            deletePost()
            selectedID = -1
        }
    }

    fun getPosts() {
        var call: Observable<List<Post>> = BaseApplication.customService.getPosts()
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { postList -> postAdapter.setData(postList as ArrayList<Post>) }
//        call.enqueue(object: Callback<List<Post>>{
//            override fun onResponse(call: Call<List<Post>>?, response: Response<List<Post>>?) {
//                var list = response!!.body() as ArrayList<Post>
//                postAdapter.setData(list)
//            }
//
//            override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {
//                Log.d("MIM", "호출 실패!")
//            }
//        })
    }

    fun deletePost() {
        var call: Observable<List<Post>> = BaseApplication.customService.deletePost(Post(selectedID))

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { postList -> postAdapter.setData(postList as ArrayList<Post>) }

//        call.enqueue(object : Callback<Post>{
//            override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
//                getPosts()
//            }
//            override fun onFailure(call: Call<Post>?, t: Throwable?) {
//                Log.d("MIM_REST_D", "delete fail")
//            }
//        })
    }
    override fun onResume() {
        super.onResume()

        //흐음.. 마음에 안드는데.. 포스트를 다른 Activity에서 생성하는 구조에서는 어쩔 수 없는건가..
        getPosts()
    }
}
