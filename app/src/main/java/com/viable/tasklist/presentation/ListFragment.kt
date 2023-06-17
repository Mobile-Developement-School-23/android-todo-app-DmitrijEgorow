package com.viable.tasklist.presentation

import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.viable.tasklist.R
import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.data.Importance
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.databinding.FragmentListBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID

/**
 * A [Fragment] to show all tasks.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    private val itemViewModel: ItemViewModel by activityViewModels()

    private var items: List<TodoItem> = emptyList()

    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.setContentScrimColor(
            ResourcesCompat.getColor(
                resources,
                R.color.toolbarColor,
                context?.theme,
            ),
        )

        val repository = (requireActivity().application as TodoItemsApplication).repository
        itemViewModel.setTodoItem(null)
        itemViewModel.loadItems(repository)
        adapter = ItemAdapter({ position ->
            itemViewModel.setSelectedPosition(position)
            itemViewModel.setTodoItem(items[position])
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }, DefaultItemFormatter(resources))
        items = itemViewModel.items.value
        val newItem = TodoItem(
            id = UUID.randomUUID().toString(),
            text = getString(R.string.new_item_text),
            importance = Importance.LOW,
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        )
        adapter.tasks = items.toMutableList().apply {
            add(newItem)
        }

        ResourcesCompat.getDrawable(resources, R.drawable.divider, requireContext().theme)?.let {
            binding.recyclerList.addItemDecoration(
                HorizontalDividerItemDecoration(
                    it,
                ),
            )
        }
        binding.recyclerList.layoutManager = LinearLayoutManager(context)
        binding.recyclerList.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(
                adapter,
                ResourcesCompat.getDrawable(resources, R.drawable.delete, context?.theme)!!,
                ResourcesCompat.getDrawable(resources, R.drawable.check, context?.theme)!!,
            ),
        )

        itemTouchHelper.attachToRecyclerView(binding.recyclerList)

        binding.completedDealsText.text = getString(R.string.completed_with_num, 0)

        binding.visibility.setOnClickListener { v ->
            v.isActivated = !(v.isActivated)
        }

        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DefaultItemFormatter(private val resources: Resources) : ItemFormatter {
        override fun formatDate(date: String?): CharSequence? {
            if (date != null) {
                val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = dateParser.parse(date.substring(0, 19))
                val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
                if (date != null) {
                    return dateFormatter.format(date)
                }
            }
            return null
        }

        override fun wrapWithTemplate(
            text: CharSequence?,
            stringId: Int,
            shouldStrikethrough: Boolean,
        ): CharSequence {
            if (text != null) {
                val content1 = resources.getString(stringId, text)
                val spannableString1 = SpannableString(content1)
                if (shouldStrikethrough) {
                    spannableString1.setSpan(StrikethroughSpan(), 0, spannableString1.length, 0)
                }
                return spannableString1
            }

            return ""
        }
    }
}
