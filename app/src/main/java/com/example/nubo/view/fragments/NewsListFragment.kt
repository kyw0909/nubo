// app/src/main/java/com/example/nubo/view/fragments/NewsListFragment.kt
package com.example.nubo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nubo.data.NewsArticle
import com.example.nubo.databinding.FragmentNewsListBinding // 이 바인딩은 별도로 생성해야 합니다.
import com.example.nubo.R // 예시를 위해 임시로 사용
// import com.bumptech.glide.Glide // 이미지 로딩을 위한 Glide 라이브러리 (필요 시 주석 해제)
// 뉴스 아이템을 위한 어댑터 (NewsListFragment 내부에 중첩 클래스로 정의하거나 별도 파일로 분리 가능)
class NewsListAdapter(private var articles: List<NewsArticle>, private val onItemClick: (NewsArticle) -> Unit) :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TODO: news_item_layout.xml에 정의된 뷰들을 바인딩해야 합니다.
        val tvTitle: TextView = view.findViewById(R.id.tv_news_title) // 예시 ID
        val tvDescription: TextView = view.findViewById(R.id.tv_news_description) // 예시 ID
        val tvPubDate: TextView = view.findViewById(R.id.tv_news_pubdate) // 예시 ID
        // val ivThumbnail: ImageView = view.findViewById(R.id.iv_news_thumbnail) // 썸네일 이미지뷰
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // TODO: 뉴스 아이템 레이아웃 (예: news_item_layout.xml)을 인플레이트해야 합니다.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.tvTitle.text = article.title
        holder.tvDescription.text = article.description
        holder.tvPubDate.text = article.pubDate

        // TODO: Glide 등을 사용하여 이미지 로드
        // if (article.imageUrl.isNotEmpty()) {
        //     Glide.with(holder.itemView.context)
        //         .load(article.imageUrl)
        //         .into(holder.ivThumbnail)
        // }

        holder.itemView.setOnClickListener { onItemClick(article) }
    }

    override fun getItemCount(): Int = articles.size

    // 어댑터 데이터 업데이트 메서드
    fun updateArticles(newArticles: List<NewsArticle>) {
        this.articles = newArticles
        notifyDataSetChanged()
    }
}


class NewsListFragment : Fragment() {
    private var _binding: FragmentNewsListBinding? = null // NewsListFragment 전용 바인딩
    private val binding get() = _binding!!

    private lateinit var category: String
    private lateinit var newsAdapter: NewsListAdapter
    // MainFragment의 Presenter로부터 뉴스 데이터를 받을 콜백
    var onArticlesLoaded: ((List<NewsArticle>) -> Unit)? = null

    companion object {
        private const val ARG_CATEGORY = "category"
        fun newInstance(category: String): NewsListFragment {
            val fragment = NewsListFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY) ?: "전체"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // NewsListFragment를 위한 별도의 레이아웃 (fragment_news_list.xml)을 인플레이트
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsListAdapter(emptyList()) { article ->
            // TODO: 뉴스 아이템 클릭 처리 로직을 MainFragment로 전달
            (parentFragment as? MainFragment)?.presenter?.onNewsItemClicked(article)
        }
        binding.recyclerView.apply { // recyclerView는 fragment_news_list.xml에 있어야 함
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        // MainFragment에서 데이터가 로드되면 이 콜백을 통해 업데이트
        onArticlesLoaded = { articles ->
            newsAdapter.updateArticles(articles)
        }

        // 초기 로딩 표시 (선택 사항)
        // binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // MainFragment로부터 뉴스 데이터를 받을 메서드
    fun updateNews(articles: List<NewsArticle>) {
        newsAdapter.updateArticles(articles)
        // binding.progressBar.visibility = View.GONE // 로딩 숨기기
    }
}