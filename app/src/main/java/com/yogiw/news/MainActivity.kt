package com.yogiw.news

import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.view.View
import android.widget.Toast
import com.yogiw.news.Adapter.NewsAdapter
import com.yogiw.news.Dao.NewsDao
import com.yogiw.news.Helper.InitRetrofit
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var snapHelper:SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvNews)

        fetchApi()
    }

    fun fetchApi(){
        val api =  InitRetrofit().getInitInstance()
        val call = api.requestNews("109cafd7e6a94b0cae047707712c8c25")
        call.enqueue(object : Callback<NewsDao> {
            override fun onFailure(call: Call<NewsDao>?, t: Throwable?) {
                container.visibility = View.GONE
                tvNotif.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<NewsDao>?, response: retrofit2.Response<NewsDao>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val data = response.body()?.articles
                        val adapter = NewsAdapter(this@MainActivity, data)
                        rvNews.adapter = adapter
                        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        rvNews.layoutManager = layoutManager
                        pageIndicator.attachTo(rvNews)
                    }
                }
            }

        })
    }


}
