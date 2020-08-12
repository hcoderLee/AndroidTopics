package com.lee.androidtopics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_topics.*

class TopicsFragment : Fragment() {
    private var topics: List<ExpandableItem>? = null
    private var scrollTo = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topic_list.run {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = ExpandableListAdapter(getTopics())
            scrollTo(0, scrollTo)
        }
    }

    private fun getTopics(): List<ExpandableItem> {
        if (topics == null) {
            topics = createTopics()
        }
        return topics!!
    }

    private fun createTopics(): List<ExpandableItem> {
        return arrayListOf(
            ExpandableItem(
                "Activity",
                arrayListOf()
            ),
            ExpandableItem(
                "Architecture Components",
                arrayListOf()
            ),
            ExpandableItem(
                "Navigation Components",
                arrayListOf(
                    ExpandableSubItem(
                        "基本使用",
                        gotoContent(R.id.action_topicsFragment_to_navigationBasicFragment)
                    )
                )
            )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        scrollTo = topic_list.scrollY
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun gotoContent(actionId: Int): View.OnClickListener {
        return View.OnClickListener {
            it.findNavController().navigate(actionId)
        }
    }
}