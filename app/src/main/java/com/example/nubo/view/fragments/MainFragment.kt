// kyw0909/nubo/nubo-700f81f17325a8c78b9f20770f5aae2c7965c919/app/src/main/java/com/example/nubo/view/fragments/MainFragment.kt
package com.example.nubo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nubo.R
import com.example.nubo.data.NewsArticle
import com.example.nubo.databinding.FragmentMainBinding
import com.example.nubo.presenter.main.MainContract
import com.example.nubo.presenter.main.MainPresenter
import com.example.nubo.presenter.main.NewsRepository
import com.example.nubo.view.recycleview.NewsAdapter
import com.google.android.material.tabs.TabLayout

class MainFragment : Fragment(), MainContract.View {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: MainContract.Presenter
    private lateinit var newsAdapter: NewsAdapter
    // private var fragmentChangeListener: FragmentChange? = null // MainActivity가 구현

    // companion object에서 newInstance 제거하고 일반 생성 또는 MainActivity에서 주입
    // MainActivity 로부터 FragmentChange 인터페이스를 받기 위한 코드
    // override fun onAttach(context: Context) {
    //     super.onAttach(context)
    //     if (context is FragmentChange) {
    //         fragmentChangeListener = context
    //     } else {
    //         // throw RuntimeException("$context must implement FragmentChange")
    //         // MainActivity가 FragmentChange를 구현하므로 이 부분은 현재 MainActivity 구조에서는 필요 없을 수 있음
    //     }
    // }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Model(Repository) 인스턴스 생성
        val newsRepository = NewsRepository()
        // Presenter 인스턴스 생성 및 View 연결
        presenter = MainPresenter(newsRepository) // MainPresenter의 생성자에서 abstract 제거 필요
        presenter.attachView(this)

        setupRecyclerView()
        setupListeners()

        // 초기 데이터 로드 (Presenter의 attachView에서 처리)
        // presenter.loadNews("전체") // Presenter의 attachView에서 초기 카테고리 로드하도록 변경
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            presenter.onNewsArticleClicked(article)
        }
        binding.rvNewsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun setupListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    presenter.onCategorySelected(it.text.toString())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 필요시 재선택 로직 구현 (예: 목록 맨 위로 스크롤)
                tab?.let {
                    presenter.onCategorySelected(it.text.toString()) // 또는 loadNews 재호출
                }
            }
        })

        binding.btnSearch.setOnClickListener {
            presenter.onSearchButtonClicked()
        }

        binding.fabSummarize.setOnClickListener {
            // 현재 선택된 기사가 있다면 해당 기사를, 없다면 목록의 첫번째 기사를 전달
            // 이 로직은 Presenter가 담당하는 것이 더 적절할 수 있음
            // 여기서는 간단히 Presenter에게 "요약 버튼 눌림"만 알리고,
            // Presenter가 현재 상태(예: 마지막으로 클릭된 기사, 또는 현재 카테고리의 첫 기사)를 기반으로 동작
            val currentVisibleArticle: NewsArticle? = null // TODO: 현재 화면에 보이는 주요 기사 식별 로직 (선택적)
            presenter.onSummarizeFabClicked(currentVisibleArticle)
        }
    }

    // --- MainContract.View 구현 ---
    override fun showLoadingIndicator() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.GONE
        binding.rvNewsList.visibility = View.GONE
    }

    override fun hideLoadingIndicator() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showNewsArticles(articles: List<NewsArticle>) {
        binding.rvNewsList.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.GONE
        newsAdapter.submitList(articles)
    }

    override fun showErrorMessage(message: String) {
        binding.rvNewsList.visibility = View.GONE // 목록 숨기기
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.tvErrorMessage.text = message
        // Toast.makeText(context, message, Toast.LENGTH_LONG).show() // 또는 토스트로도 표시
    }

    override fun navigateToSummaryScreen(article: NewsArticle) {
        // MainActivity를 통해 SummaryFragment로 전환
        // (activity as? FragmentChange)?.setFrag(PageName.SUMMARY.ordinal, article) // SUMMARY PageName 추가 필요
        // 또는 FragmentManager 직접 사용
        val summaryFragment = SummaryFragment.newInstance(article) // SummaryFragment에 article 전달하는 newInstance 필요

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentFrame, summaryFragment) // MainActivity의 FrameLayout ID
            .addToBackStack(null) // 뒤로가기 스택에 추가
            .commit()
    }


    override fun setupTabs(categories: List<String>) {
        binding.tabLayout.removeAllTabs() // 기존 탭 제거
        categories.forEach { categoryName ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(categoryName))
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun goToSearchScreen() {
        // TODO: 검색 화면으로 이동하는 로직 (새로운 Fragment 또는 Activity)
        showToast("검색 화면으로 이동 (미구현)")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView() // Presenter와 View 연결 해제
        _binding = null
    }

    companion object {
        fun newInstance(): MainFragment { // param, Fragmentch 제거
            return MainFragment()
        }
    }
}