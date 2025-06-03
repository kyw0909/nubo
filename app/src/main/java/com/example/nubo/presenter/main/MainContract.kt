package com.example.nubo.presenter.main
import com.example.nubo.data.NewsArticle

interface MainContract {
    interface View {
        fun goToSearchScreen()
        fun goToSummarizeScreen()
        fun showToast(msg: String)
        fun showNewsArticles(articles: List<NewsArticle>) // 뉴스 목록을 View에 표시 (NewsListFragment에 전달)
        fun showLoadingIndicator() // 로딩 인디케이터 표시
        fun hideLoadingIndicator() // 로딩 인디케이터 숨기기
        fun showErrorMessage(message: String) // 오류 메시지 표시
        fun navigateToSummaryFragment(newsContent: String) // 요약 화면으로 이동
        fun setupTabLayoutWithViewPager(categories: List<String>) // 탭 레이아웃 설정
        fun showCategoryNews(category: String, articles: List<NewsArticle>)
    }

    interface Presenter {
        fun onSearchClicked()
        fun onSummarizeClicked()
        fun onCategorySelected(index: Int)
        fun attachView(view: View)
        fun detachView()
        fun loadNewsArticlesForCategory(category: String) // 특정 카테고리 뉴스 로드
        fun onTabSelected(category: String) // 탭 선택 시 호출
        fun onNewsItemClicked(article: NewsArticle) // 뉴스 아이템 클릭 시 호출 (웹뷰 등)
        fun onSummarizeButtonClicked() // 요약하기 버튼 클릭 시 호출
        fun onSearchButtonClicked() // 검색 버튼 클릭 시 호출
    }

    // Model 인터페이스: Presenter가 Model에게 요청할 수 있는 메서드들을 정의합니다.
    interface Model {
        // 콜백 리스너를 사용하여 데이터를 비동기적으로 전달
        fun getNewsArticles(category: String, callback: OnNewsLoadListener)

        interface OnNewsLoadListener {
            fun onSuccess(articles: List<NewsArticle>)
            fun onFailure(errorMessage: String)
        }
    }
}
