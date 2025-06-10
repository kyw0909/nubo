// app/src/main/java/com/example/nubo/view/adapters/NewsAdapter.kt (새로운 패키지 및 파일)
package com.example.nubo.view.recycleview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nubo.R
import com.example.nubo.data.NewsArticle
import com.example.nubo.databinding.ItemNewsArticleBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(private val onItemClick: (NewsArticle) -> Unit) :
    ListAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsViewHolder(
        private val binding: ItemNewsArticleBinding,
        private val onItemClick: (NewsArticle) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Firestore timestamp를 Date 객체로 변환 후 포맷팅 (예시, 실제 pubDate 필드 타입에 따라 조정)

        fun bind(article: NewsArticle) {
            binding.tvNewsTitle.text = article.title
            // content는 보통 너무 길어서 description(summary)을 보여주는 것이 일반적
            binding.tvNewsDescription.text = article.summary.ifEmpty { article.content }


            // Firestore의 timestamp를 사용하거나, Python에서 저장한 pubDate 문자열을 사용
            if (article.timestamp != null) {
                binding.tvNewsDate.text = outputDateFormat.format(article.timestamp)
            } else if (article.pubDate.isNotEmpty()) {
                // pubDate 문자열이 특정 형식이면 파싱 후 포맷팅 필요
                binding.tvNewsDate.text = article.pubDate // 예시: "Mon, 02 Jun 2025 10:00:00 +0900"
            } else {
                binding.tvNewsDate.text = "" // 날짜 정보 없을 시
            }

            Glide.with(binding.ivNewsThumbnail.context)
                .load(article.imageUrl) // imageUrl이 Firestore에 저장되어 있어야 함
                .placeholder(R.drawable.ic_launcher_background) // 로딩 중 이미지
                .error(R.drawable.ic_launcher_foreground) // 에러 시 이미지
                .into(binding.ivNewsThumbnail)

            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            // Firestore 문서 ID가 있다면 그것으로 비교하는 것이 가장 좋음
            // return oldItem.id == newItem.id (NewsArticle에 id 필드가 있다면)
            return oldItem.title == newItem.title && oldItem.timestamp == newItem.timestamp // 임시 비교
        }

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem == newItem
        }
    }
}