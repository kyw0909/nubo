// app/src/main/java/com/example/nubo/data/NewsArticle.kt
package com.example.nubo.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class NewsArticle(
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val pubDate: String = "", // 또는 Date 타입으로 변환하여 저장
    val category: String = "",
    val imageUrl: String = "", // 뉴스 썸네일 이미지 URL (필요 시)
    @ServerTimestamp // Firestore에서 자동으로 서버 시간 타임스탬프를 넣어줌
    val timestamp: Date? = null
)