package com.example.nubo.presenter.main

import com.example.nubo.data.NewsArticle

abstract class MainPresenter(private val model: MainContract.Model) : MainContract.Presenter, MainContract.Model.OnNewsLoadListener {

    private var view: MainContract.View? = null
    // 탭 레이아웃에 표시할 카테고리 목록
    private val categories = listOf("전체", "정치", "스포츠", "경제", "국제", "사회", "IT 과학")
    private var currentCategory: String = "전체" // 현재 선택된 카테고리

    override fun attachView(view: MainContract.View) {
        this.view = view
        view.setupTabLayoutWithViewPager(categories) // View에 탭 레이아웃 설정을 지시
        loadNewsArticlesForCategory(currentCategory) // 초기 로드: "전체" 뉴스 로드
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadNewsArticlesForCategory(category: String) {
        view?.showLoadingIndicator() // View에 로딩 인디케이터 표시 요청
        currentCategory = category
        model.getNewsArticles(category, this) // Model에 특정 카테고리 뉴스 요청
    }

    // 탭 선택 시 호출될 Presenter 메서드 (ViewPager2와 연동될 때 주로 사용)
    override fun onTabSelected(category: String) {
        loadNewsArticlesForCategory(category) // 선택된 탭의 뉴스 로드
    }

    // 뉴스 아이템 클릭 시 호출될 Presenter 메서드
    override fun onNewsItemClicked(article: NewsArticle) {
        // TODO: 뉴스 아이템 클릭 시 상세 화면 (웹뷰)으로 이동하는 로직 구현
        // View에서 FragmentChange 인터페이스를 통해 이동을 처리할 수 있습니다.
        view?.showErrorMessage("뉴스 링크: ${article.link} (클릭 로직 미구현)") // 임시 메시지
    }

    // '요약하기' 버튼 클릭 시 호출될 Presenter 메서드
    override fun onSummarizeButtonClicked() {
        // TODO: 현재 보이는 탭의 첫 번째 뉴스나 선택된 뉴스 기사 내용을 요약 화면으로 전달
        // 여기서는 예시로 샘플 텍스트를 전달합니다.
        val sampleNewsContent = "최신 인공지능 기술이 빠르게 발전하며 일상생활에 큰 변화를 가져오고 있습니다. 특히, 자연어 처리 분야에서는 LLM(Large Language Model)이 등장하여 텍스트 생성, 번역, 요약 등 다양한 작업을 수행할 수 있게 되었습니다. 이는 교육, 의료, 금융 등 여러 산업 분야에 혁신적인 영향을 미치고 있으며, 미래 사회의 모습을 재정의할 것으로 기대됩니다. 하지만 기술의 발전과 함께 윤리적 문제와 사회적 영향에 대한 논의도 활발하게 이루어지고 있습니다."
        view?.navigateToSummaryFragment(sampleNewsContent)
    }

    // 검색 버튼 클릭 시 호출될 Presenter 메서드
    override fun onSearchButtonClicked() {
        // TODO: 검색 화면으로 이동하는 로직 구현
        view?.showErrorMessage("검색 기능은 아직 구현되지 않았습니다.")
    }

    // Model로부터 데이터 로드 성공 콜백
    override fun onSuccess(articles: List<NewsArticle>) {
        view?.hideLoadingIndicator() // View에 로딩 인디케이터 숨김 요청
        view?.showCategoryNews(currentCategory, articles) // View에 해당 카테고리 뉴스 표시 요청
    }

    // Model로부터 데이터 로드 실패 콜백
    override fun onFailure(errorMessage: String) {
        view?.hideLoadingIndicator() // View에 로딩 인디케이터 숨김 요청
        view?.showErrorMessage(errorMessage) // View에 오류 메시지 표시 요청
    }
}