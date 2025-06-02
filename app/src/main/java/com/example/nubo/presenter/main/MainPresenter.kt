package com.example.mongcare.presenter.main

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Presenter {

    override fun onSearchClicked() {
        view.goToSearchScreen()
    }

    override fun onSummarizeClicked() {
        view.goToSummarizeScreen()
    }

    override fun onCategorySelected(index: Int) {
        view.showToast("카테고리 ${index + 1}번 선택됨")
    }
}
