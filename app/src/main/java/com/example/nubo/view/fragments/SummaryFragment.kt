// kyw0909/nubo/nubo-700f81f17325a8c78b9f20770f5aae2c7965c919/app/src/main/java/com/example/nubo/view/fragments/SummaryFragment.kt
package com.example.nubo.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nubo.data.NewsArticle
import com.example.nubo.databinding.FragmentSummaryBinding

class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private var newsArticle: NewsArticle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Android Studio 최신 버전에서는 getParcelable deprecated 경고가 나올 수 있음
            // Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU 사용 분기 필요
            newsArticle = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_NEWS_ARTICLE, NewsArticle::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_NEWS_ARTICLE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        newsArticle?.let { article ->
            // 초기에는 로딩 메시지를 보여줄 수 있음 (이미지 참고)
            // binding.pbSummaryLoading.visibility = View.VISIBLE
            // binding.tvSummaryContent.visibility = View.GONE
            // binding.loadingMessageTextView.visibility = View.VISIBLE // fragment_summary.xml에 있는 TextView (현재는 없음)

            // 제목 설정
            binding.tvSummaryTitle.text = article.title.ifEmpty { getString(com.example.nubo.R.string.summary_title_default) }

            // 요약 내용 설정 (Python 스크립트의 summary 필드)
            if (article.summary.isNotEmpty()) {
                binding.tvSummaryContent.text = article.summary
                binding.pbSummaryLoading.visibility = View.GONE // 로딩 완료
                binding.tvSummaryContent.visibility = View.VISIBLE
            } else if (article.content.isNotEmpty()) {
                // summary가 없고 content만 있다면, content를 보여주거나, 여기서 요약 API를 호출해야 함.
                // 현재는 Python에서 요약된 것을 받아온다고 가정.
                // 만약 Android에서 요약해야 한다면, 여기서 Presenter를 통해 Model에 요약 요청.
                binding.tvSummaryContent.text = "요약된 내용이 없습니다. 원본 내용을 표시합니다:\n\n${article.content}"
                binding.pbSummaryLoading.visibility = View.GONE
                binding.tvSummaryContent.visibility = View.VISIBLE
            } else {
                binding.tvSummaryContent.text = "표시할 내용이 없습니다."
                binding.pbSummaryLoading.visibility = View.GONE
                binding.tvSummaryContent.visibility = View.VISIBLE
            }


            if (article.link.isNotEmpty()) {
                binding.btnViewOriginal.visibility = View.VISIBLE
                binding.btnViewOriginal.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.link))
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "원본 기사 링크를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                binding.btnViewOriginal.visibility = View.GONE
            }
        } ?: run {
            binding.tvSummaryTitle.text = getString(com.example.nubo.R.string.summary_title_default)
            binding.tvSummaryContent.text = "뉴스를 불러오지 못했습니다."
            binding.btnViewOriginal.visibility = View.GONE
            binding.pbSummaryLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NEWS_ARTICLE = "news_article"

        // NewsArticle 객체를 Parcelable로 만들거나, 필요한 필드만 String으로 전달해야 함.
        // NewsArticle을 Parcelable로 만들려면 @Parcelize 어노테이션과 kotlinx.parcelize 플러그인 필요.
        // 여기서는 NewsArticle이 Firestore에서 오므로, 필드들이 Serializable 하다고 가정.
        // 또는 필요한 정보(title, summary, link)만 String으로 전달.
        // 가장 간단한 방법은 NewsArticle 클래스를 Parcelable로 만드는 것.
        // NewsArticle.kt 에 @Parcelize 추가하고 import kotlinx.parcelize.Parcelize
        // 그리고 data class NewsArticle(...) : Parcelable
        fun newInstance(article: NewsArticle): SummaryFragment {
            val fragment = SummaryFragment()
            val args = Bundle()
            args.putParcelable(ARG_NEWS_ARTICLE, args) // NewsArticle이 Parcelable이어야 함
            fragment.arguments = args
            return fragment
        }
    }
}
