// kyw0909/nubo/nubo-700f81f17325a8c78b9f20770f5aae2c7965c919/app/src/main/java/com/example/nubo/presenter/main/NewsRepository.kt
package com.example.nubo.presenter.main

import android.util.Log
import com.example.nubo.data.NewsArticle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NewsRepository : MainContract.Model {

    private val db = FirebaseFirestore.getInstance()
    private val newsCollection = db.collection("news_articles") // Python에서 저장하는 컬렉션 이름과 일치 필요
    private val TAG = "NewsRepository"

    override fun getNewsArticles(category: String, callback: MainContract.Model.OnNewsLoadListener) {
        Log.d(TAG, "Fetching news for category: $category")
        var query: Query = newsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING) // 최신순 정렬

        if (category.isNotEmpty() && category != "전체") { // "전체" 또는 빈 카테고리면 필터링 안 함
            query = query.whereEqualTo("category", category) // category 필드가 Firestore에 있어야 함
        }

        query.limit(20) // 너무 많은 데이터를 가져오지 않도록 제한 (예시)
            .get()
            .addOnSuccessListener { result ->
                val articles = mutableListOf<NewsArticle>()
                for (document in result) {
                    try {
                        val article = document.toObject(NewsArticle::class.java)
                        // Firestore 문서 ID를 article 객체 내에 저장하고 싶다면, 여기서 처리 가능
                        // article.id = document.id (NewsArticle에 id 필드 추가 시)
                        articles.add(article)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error converting document: ${document.id}", e)
                        // 특정 문서 변환 실패 시 로그 남기고 계속 진행하거나, 전체 실패 처리 가능
                    }
                }
                Log.d(TAG, "Successfully fetched ${articles.size} articles for category: $category")
                callback.onSuccess(articles)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting news articles for category $category: ", exception)
                callback.onFailure("뉴스 데이터를 가져오는데 실패했습니다: ${exception.message}")
            }
    }
}