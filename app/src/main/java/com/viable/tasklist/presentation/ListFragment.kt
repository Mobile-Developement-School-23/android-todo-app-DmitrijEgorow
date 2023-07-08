package com.viable.tasklist.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.viable.tasklist.R
import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.data.Importance
import com.viable.tasklist.data.ObtainedData
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.data.TodoItemsRepository
import com.viable.tasklist.databinding.FragmentListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * A [Fragment] to show all tasks.
 */
class ListFragment : Fragment() {

    private val sharedPreferencesName = "revision"

    private val snackbarDelay = 2_000L

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val uiScope = CoroutineScope(Dispatchers.Main)
    @Inject lateinit var repository: TodoItemsRepository

    private val itemViewModel: ItemViewModel by activityViewModels {
        ViewModelFactory(
            repository,
        )
    }

    private lateinit var adapter: ItemAdapter

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
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

        //repository = (requireActivity().application as TodoItemsApplication).repository
        itemViewModel.obtainedData.observe(
            viewLifecycleOwner,
            object : Observer<ObtainedData> {
                override fun onChanged(data: ObtainedData) {
                    val response = when (data) {
                        is ObtainedData.Success -> {
                            showSnackbar(getString(R.string.sync_success))
                            itemViewModel.resetObtainedData()
                        }

                        is ObtainedData.Fail -> {
                            showSnackbar(getString(R.string.sync_error))
                            itemViewModel.resetObtainedData()
                        }

                        is ObtainedData.Empty -> {}
                    }
                }
            },
        )
        itemViewModel.setTodoItem(null)
        itemViewModel.loadItems()
        adapter = ItemAdapter(
            { position, _ ->
                itemViewModel.setSelectedPosition(position)
                itemViewModel.setTodoItem(adapter.tasks[position])
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            },
            { position, isCompleted ->
                itemViewModel.updateTodoItem(
                    position,
                    adapter.tasks[position].also { v ->
                        v.isCompleted = isCompleted
                    },
                )
            },
            DefaultItemFormatter(resources),
        )
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
                        is TaskUi.Empty -> adapter.tasks = mutableListOf(newItem)
                        else -> listOf(newItem)
                    }
                }
            }
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
                { id, position ->
                    val item = adapter.removeItem(position)
                    val snackbar = Snackbar.make(
                        binding.root,
                        getString(R.string.one_task_deleted),
                        Snackbar.LENGTH_LONG,
                    )
                        .setAction(getString(R.string.undo_delete)) {
                            adapter.addItem(position, item)
                        }
                    snackbar.show()
                    uiScope.launch {
                        delay(snackbarDelay)
                        if (snackbar.isShown) {
                            snackbar.dismiss()
                            itemViewModel.deleteTodoItem(id)
                        }
                    }
                },
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

    fun showSnackbar(supportedText: CharSequence) {
        Snackbar.make(binding.root, supportedText, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
