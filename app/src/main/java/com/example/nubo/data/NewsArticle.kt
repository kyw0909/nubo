// kyw0909/nubo/nubo-700f81f17325a8c78b9f20770f5aae2c7965c919/app/src/main/java/com/example/nubo/data/NewsArticle.kt
package com.example.nubo.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class NewsArticle(
    val title: String = "",       // Firestore 필드명과 일치해야 함 (Python에서 저장하는 이름 기준)
    val content: String = "",     // Naver API의 description, 요약 전 원문 (또는 요약된 내용)
    val summary: String = "",     // KoBART 요약 결과
    val link: String = "",        // 뉴스 원문 링크 (선택 사항, 필요 시 Python에서 추가 저장)
    val pubDate: String = "",     // 발행일 (선택 사항, 필요 시 Python에서 추가 저장)
    val category: String = "",    // 카테고리 (선택 사항, 필요 시 Python에서 추가 저장)
    val imageUrl: String? = null, // 뉴스 썸네일 이미지 URL (선택 사항, 필요 시 Naver API에서 추출하여 Python에서 추가 저장)
    @ServerTimestamp
    val timestamp: Date? = null   // Firestore에서 자동 생성
) {
    // Firestore에서 Deserialization 할 때 빈 생성자가 필요할 수 있습니다.
    constructor() : this("", "", "", "", "", "", null, null)
}