package com.example.mongcare.presenter.main

interface MainContract {
    interface View {
        fun goToSearchScreen()
        fun goToSummarizeScreen()
        fun showToast(msg: String)
    }

    interface Presenter {
        fun onSearchClicked()
        fun onSummarizeClicked()
        fun onCategorySelected(index: Int)
    }
}
