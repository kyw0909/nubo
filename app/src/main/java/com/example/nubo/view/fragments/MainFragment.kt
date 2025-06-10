// app/src/main/java/com/example/nubo/view/fragments/MainFragment.kt
package com.example.nubo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nubo.Interfaces.FragmentChange
import com.example.nubo.Interfaces.PageName
import com.example.nubo.data.NewsArticle
import com.example.nubo.databinding.FragmentMainBinding
import com.example.nubo.presenter.main.MainContract
import com.example.nubo.presenter.main.MainPresenter
import com.example.nubo.presenter.main.NewsRepository
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment(), MainContract.View {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    // Activity에서 프래그먼트 전환을 처리한다고 가정
    lateinit var fragmentChange: FragmentChange
    // Presenter 인스턴스
    lateinit var presenter: MainContract.Presenter // lateinit으로 선언

    // ViewPager2 어댑터
    private inner class NewsPagerAdapter(fragment: Fragment, private val categories: List<String>) : FragmentStateAdapter(fragment) {
        private val fragments = mutableMapOf<String, NewsListFragment>()

        override fun getItemCount(): Int = categories.size

        override fun createFragment(position: Int): Fragment {
            val category = categories[position]
            // 이미 생성된 프래그먼트가 있다면 재사용
            return fragments[category] ?: NewsListFragment.newInstance(category).also {
                fragments[category] = it
            }
        }

        fun getFragment(category: String): NewsListFragment? {
            return fragments[category]
        }
    }

    private lateinit var newsPagerAdapter: NewsPagerAdapter
    private val categories = listOf("전체", "정치", "스포츠", "경제", "국제", "사회", "IT 과학")

    // newInstance 메서드를 통해 FragmentChange 인터페이스를 주입받도록 함
    companion object {
        fun newInstance(Fragmentch: FragmentChange): MainFragment {
            val fragment = MainFragment()
            fragment.fragmentChange = Fragmentch
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Presenter 초기화 (Model 주입)
        presenter = MainPresenter(NewsRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this) // Presenter에 View 연결

        // 검색 버튼 클릭 리스너
        binding.btnSearch.setOnClickListener {
            presenter.onSearchButtonClicked()
        }

        // 요약하기 FloatingActionButton 클릭 리스너
        binding.fabSummarize.setOnClickListener {
            presenter.onSummarizeButtonClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView() // Presenter에서 View 연결 해제
        _binding = null
    }

    override fun goToSearchScreen() {
        TODO("Not yet implemented")
    }

    override fun goToSummarizeScreen() {
        TODO("Not yet implemented")
    }

    override fun showToast(msg: String) {
        TODO("Not yet implemented")
    }

    // --- MainContract.View 인터페이스 구현 ---

    override fun showNewsArticles(articles: List<NewsArticle>) {
        // 이 메서드는 NewsListFragment에서 직접 데이터를 표시할 것이므로, 여기서는 사용하지 않습니다.
        // 각 NewsListFragment에 데이터가 전달될 때 showCategoryNews를 사용합니다.
    }

    override fun showCategoryNews(category: String, articles: List<NewsArticle>) {
        // 현재 ViewPager2에 표시된 또는 해당 카테고리의 NewsListFragment에 데이터를 전달
        val currentFragment = newsPagerAdapter.getFragment(category)
        currentFragment?.updateNews(articles)
    }

    override fun showLoadingIndicator() {
        // 로딩 스피너 등을 표시하는 로직
        // 예: binding.progressBar.visibility = View.VISIBLE (progressBar를 XML에 추가해야 함)
        // binding.progressBar?.visibility = View.VISIBLE // null-safe 호출
        // 또는, 각 NewsListFragment 내부에 로딩 인디케이터를 둘 수도 있습니다.
    }

    override fun hideLoadingIndicator() {
        // 로딩 스피너 등을 숨기는 로직
        // 예: binding.progressBar.visibility = View.GONE
        // binding.progressBar?.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun navigateToSummaryFragment(newsContent: String) {
        // FragmentChange 인터페이스를 사용하여 SummaryFragment로 전환
        if (::fragmentChange.isInitialized) {
            // PageName enum에 SUMMARY_SCREEN이 있는지 확인 필요
            fragmentChange.setFrag(PageName.SUMMARY_SCREEN.ordinal, newsContent)
        } else {
            Toast.makeText(context, "FragmentChange 인터페이스가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setupTabLayoutWithViewPager(categories: List<String>) {
        newsPagerAdapter = NewsPagerAdapter(this, categories)
        binding.viewPager.adapter = newsPagerAdapter // viewPager는 fragment_main.xml에 추가해야 함

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()

        // 탭 선택 리스너 (Presenter에 탭 선택 이벤트 전달)
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                tab?.text?.toString()?.let { category ->
                    presenter.onTabSelected(category)
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }
}