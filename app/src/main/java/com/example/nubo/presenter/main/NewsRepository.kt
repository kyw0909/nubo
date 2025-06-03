package com.example.nubo.presenter.main

import com.example.nubo.data.NewsArticle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.util.Log

class NewsRepository : MainContract.Model {

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "NewsRepository"

    // Firestore에서 뉴스 기사를 가져오는 메서드
    override fun getNewsArticles(category: String, callback: MainContract.Model.OnNewsLoadListener) {
        var query: Query = db.collection("articles") // "articles" 컬렉션에서 가져옴
            .orderBy("timestamp", Query.Direction.DESCENDING) // 최신순으로 정렬

        if (category != "전체") {
            query = query.whereEqualTo("category", category) // "전체"가 아니면 해당 카테고리로 필터링
        }

        query.get()
            .addOnSuccessListener { result ->
                val articles = mutableListOf<NewsArticle>()
                for (document in result) {
                    val article = document.toObject(NewsArticle::class.java)
                    articles.add(article)
                }
                callback.onSuccess(articles) // 성공 시 콜백 호출
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
                callback.onFailure("뉴스 데이터를 가져오는데 실패했습니다: ${exception.message}") // 실패 시 콜백 호출
            }
    }
}