package com.viable.tasklist.presentation

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.viable.tasklist.R
import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.data.Importance
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.databinding.FragmentListBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A [Fragment] to show all tasks.
 */
class ListFragment : Fragment() {

    private val sharedPreferencesName = "revision"

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    private lateinit var repository: TodoItemsRepository

    private val itemViewModel: ItemViewModel by activityViewModels { ViewModelFactory(repository) }

    // private var items: List<TodoItem> = emptyList()

    private lateinit var adapter: ItemAdapter

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = this.requireActivity()
            .getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)


        binding.toolbarLayout.setContentScrimColor(
            ResourcesCompat.getColor(
                resources,
                R.color.toolbarColor,
                context?.theme,
            ),
        )

        repository = (requireActivity().application as TodoItemsApplication).repository
        itemViewModel.setTodoItem(null)
        itemViewModel.loadItems()
        adapter = ItemAdapter({ position, _ ->
            itemViewModel.setSelectedPosition(position)
            itemViewModel.setTodoItem(adapter.tasks[position])
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }, DefaultItemFormatter(resources))
        // items = itemViewModel.tasks.value
        val newItem = TodoItem(
            id = UUID.randomUUID().toString(),
            text = getString(R.string.new_item_text),
            importance = Importance.LOW,
            isCompleted = false,
            createdAt = LocalDateTime.now(),
        )
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            itemViewModel.tasks.collect { uiState ->
                withContext(Dispatchers.Main) {
                    when (uiState) {
                        is TaskUi.Success ->
                            adapter.tasks = uiState.data.toMutableList().apply {
                                add(newItem)
                            }


                        is TaskUi.Error -> Any()
                        is TaskUi.Empty ->
                            adapter.tasks = mutableListOf(newItem)


                        else -> listOf(newItem)
                    }
                }
            }
        }

        println(adapter.tasks)
        /*adapter.tasks = items.toMutableList().apply {
            add(newItem)
        }*/

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

        binding.swipeRefresh.setOnRefreshListener {
            itemViewModel.loadItems(forceRefresh = true)
            binding.swipeRefresh.isRefreshing = false
        }

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