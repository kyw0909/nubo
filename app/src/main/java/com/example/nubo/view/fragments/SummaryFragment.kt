package com.example.nubo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment // Fragment 클래스를 임포트합니다.
import com.example.nubo.databinding.FragmentSummaryBinding

// 1. Fragment를 상속받도록 수정합니다.
class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null
    // ViewBinding의 안전한 접근을 위한 프로퍼티
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 3. layoutInflater 대신 매개변수로 받은 inflater를 사용합니다.
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // param을 사용하지 않는다면 제거하는 것이 좋습니다.
        // 만약 특정 기능을 위한 param이라면, 주석으로 설명해주는 것이 좋습니다.
        fun newInstance(param: Int): SummaryFragment {
            val fragment = SummaryFragment()
            val args = Bundle()
            args.putInt("param", param) // Bundle에 인자를 추가
            // 4. Fragment를 상속받았으므로 arguments를 사용할 수 있습니다.
            fragment.arguments = args
            return fragment
        }

         fun newInstance(newsContent: String): SummaryFragment {
             val fragment = SummaryFragment()
             val args = Bundle()
             args.putString(arguments, newsContent)
             fragment.arguments = args
            return fragment
         }
    }
    // 2. 불필요한 닫는 중괄호 제거.
}