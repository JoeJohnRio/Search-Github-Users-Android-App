package com.example.githubsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var searchAdapter: SearchAdapter
    lateinit var searchView: SearchView
    lateinit var rvSearchResult: RecyclerView
    var searchKey: String = ""
    var listSearchItem = mutableListOf<SearchItem>()
    var pageNumber: Int = 1
    var lastPage: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchAdapter = SearchAdapter(this)
        searchView = findViewById(R.id.sv_search)
        searchView.queryHint = "Search Github Users"
        rvSearchResult = findViewById(R.id.rv_search_result)
        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.setHasFixedSize(true)

        rvSearchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (lastPage > pageNumber) {
                        pageNumber++
                        searchSomething(searchKey, pageNumber)
                    }
                }
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchKey = query
                pageNumber = 1
                lastPage = 1
                searchSomething(query, 1)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    fun searchSomething(query: String, pageNumber: Int){
        RetrofitClient.getInstance().api.searchUser(query, pageNumber, 100)
            .enqueue(object : Callback<SearchResult> {
                override fun onResponse(
                    call: Call<SearchResult>,
                    response: Response<SearchResult>
                ) {

                    if(response.message() == "OK"){
                        if(pageNumber == 1){
                            lastPage = response.body()?.totalCount ?: 1
                            listSearchItem = response.body()?.items ?: mutableListOf()
                            searchAdapter.submitList(response.body()?.items)
                            rvSearchResult.apply {
                                if (adapter == null) {
                                    adapter = searchAdapter
                                }
                                if (layoutManager == null) {
                                    layoutManager = LinearLayoutManager(context)
                                }
                                adapter?.notifyDataSetChanged()
                            }
                        }else{
                            listSearchItem.addAll(response.body()?.items ?: mutableListOf())
                            rvSearchResult.adapter?.notifyDataSetChanged()
                        }
                    }else if(response.message() == "Unprocessable Entity"){
                        Toast.makeText(
                            applicationContext,
                            "Only the first 1000 search results are available",
                            Toast.LENGTH_LONG
                        ).show()
                    }else if(response.message() == "rate limit exceeded"){
                        Toast.makeText(
                            applicationContext,
                            "You can only access 10 times per minute",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            applicationContext,
                            response.message(),
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }

                override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                    Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG).show()
                }

            })
    }
}
