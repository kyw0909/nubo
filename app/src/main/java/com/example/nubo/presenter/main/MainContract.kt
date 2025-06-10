// kyw0909/nubo/nubo-700f81f17325a8c78b9f20770f5aae2c7965c919/app/src/main/java/com/example/nubo/presenter/main/MainContract.kt
package com.example.nubo.presenter.main

import com.example.nubo.data.NewsArticle

interface MainContract {
    interface View {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showNewsArticles(articles: List<NewsArticle>)
        fun showErrorMessage(message: String)
        fun navigateToSummaryScreen(article: NewsArticle) // 요약 화면으로 NewsArticle 객체 전달
        fun setupTabs(categories: List<String>) // 탭 설정 (기존 setupTabLayoutWithViewPager 와 유사)
        // fun showCategoryNews(category: String, articles: List<NewsArticle>) // showNewsArticles로 통합 가능
        fun showToast(msg: String) // 기존 유지
        fun goToSearchScreen() // 기존 유지 (구현 시 필요)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadNews(category: String) // 특정 카테고리 뉴스 로드
        fun onCategorySelected(category: String) // 탭 선택 시 호출
        fun onNewsArticleClicked(article: NewsArticle) // 뉴스 아이템 클릭 시 (요약 화면으로 이동 트리거)
        fun onSummarizeFabClicked(currentArticle: NewsArticle?) // FAB 클릭 시, 현재 보고있는 주요 기사 전달 (선택)
        fun onSearchButtonClicked() // 검색 버튼 클릭 시
    }

    interface Model {
        fun getNewsArticles(category: String, callback: OnNewsLoadListener)
        // fun getNewsArticleById(id: String, callback: OnNewsArticleLoadListener) // 특정 기사만 가져올 때 (선택)

        interface OnNewsLoadListener {
            fun onSuccess(articles: List<NewsArticle>)
            fun onFailure(errorMessage: String)
        }
        // interface OnNewsArticleLoadListener {
        //     fun onSuccess(article: NewsArticle)
        //     fun onFailure(errorMessage: String)
        // }
    }
}
