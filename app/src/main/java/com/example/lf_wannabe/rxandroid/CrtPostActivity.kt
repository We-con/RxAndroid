package com.example.lf_wannabe.rxandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.lf_wannabe.rxandroid.model.Post
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_crt_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by lf_wannabe on 06/09/2017.
 */
class CrtPostActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crt_post)

        sendBtn.setOnClickListener {
            // write api 호출할 때 id가 애매하네, 받을때는 필요한데
            writePost(Post(-1,"${titleInput.text}", "${authorInput.text}"))
            finish()
        }

    }

    fun writePost(post: Post){
        var call: Observable<List<Post>> = BaseApplication.customService.writePost(post)

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { post -> Log.d("MIM_POST", "onResponse") }
    }
}