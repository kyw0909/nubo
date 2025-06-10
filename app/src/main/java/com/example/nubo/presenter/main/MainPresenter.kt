// kyw0909/nubo/nubo-700f81f17325a8c78b9f20770f5aae2c7965c919/app/src/main/java/com/example/nubo/presenter/main/MainPresenter.kt
package com.example.nubo.presenter.main

import com.example.nubo.data.NewsArticle

// MainPresenter는 MainContract.Presenter를 구현해야 합니다.
// 생성자에서 Model을 주입받는 것은 좋은 패턴입니다.
// abstract class 제거하고 일반 클래스로 변경 (필요시 유지)
class MainPresenter(private val model: MainContract.Model) : MainContract.Presenter {

    private var view: MainContract.View? = null
    private val categories = listOf("전체", "정치", "스포츠", "경제", "국제", "사회", "IT 과학") // 기존 유지
    private var currentCategory: String = categories.first() // 초기값 "전체"
    private var currentArticles: List<NewsArticle> = emptyList()


    override fun attachView(view: MainContract.View) {
        this.view = view
        this.view?.setupTabs(categories)
        loadNews(currentCategory) // 초기 "전체" 뉴스 로드
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadNews(category: String) {
        view?.showLoadingIndicator()
        currentCategory = category
        // Model에게 특정 카테고리 뉴스 요청 (콜백은 Presenter가 직접 처리하도록 변경)
        model.getNewsArticles(category, object : MainContract.Model.OnNewsLoadListener {
            override fun onSuccess(articles: List<NewsArticle>) {
                currentArticles = articles // 현재 기사 목록 업데이트
                view?.hideLoadingIndicator()
                if (articles.isEmpty() && category != "전체") {
                    view?.showErrorMessage("$category 카테고리에 뉴스가 없습니다.")
                } else if (articles.isEmpty()) {
                    view?.showErrorMessage("뉴스를 불러올 수 없습니다.")
                }
                view?.showNewsArticles(articles)
            }

            override fun onFailure(errorMessage: String) {
                view?.hideLoadingIndicator()
                view?.showErrorMessage(errorMessage)
                currentArticles = emptyList() // 실패 시 목록 비우기
                view?.showNewsArticles(emptyList()) // 빈 목록 보여주기
            }
        })
    }

    override fun onCategorySelected(category: String) {
        if (this.currentCategory != category) {
            loadNews(category)
        }
    }

    override fun onNewsArticleClicked(article: NewsArticle) {
        // View에게 요약 화면으로 이동하라고 알림
        view?.navigateToSummaryScreen(article)
    }

    override fun onSummarizeFabClicked(currentArticle: NewsArticle?) {
        // 현재 보고 있는 주요 기사가 있다면 해당 기사를 요약 화면으로 전달
        // 없다면, 현재 카테고리의 첫 번째 기사를 전달하거나, 사용자에게 선택을 유도할 수 있음
        val articleToSummarize = currentArticle ?: currentArticles.firstOrNull()
        if (articleToSummarize != null) {
            view?.navigateToSummaryScreen(articleToSummarize)
        } else {
            view?.showToast("요약할 뉴스가 없습니다.")
        }
    }

    override fun onSearchButtonClicked() {
        view?.goToSearchScreen() // View에 검색 화면 이동 요청 (미구현 상태)
        view?.showToast("검색 기능은 아직 준비중입니다.") // 임시 메시지
    }
}