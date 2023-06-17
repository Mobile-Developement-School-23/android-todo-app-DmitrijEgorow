package com.viable.tasklist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.viable.tasklist.R
import com.viable.tasklist.TodoItemsApplication
import com.viable.tasklist.data.Importance
import com.viable.tasklist.data.TodoItem
import com.viable.tasklist.databinding.FragmentEditBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.UUID

/**
 * A [Fragment] to edit / add a new task.
 */
class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null

    private val binding get() = _binding!!

    private val itemViewModel: ItemViewModel by activityViewModels()

    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitButton = view.findViewById(R.id.submit_item)
        val repository = (requireActivity().application as TodoItemsApplication).repository

        val types = resources.getStringArray(R.array.priorities)
        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = arrayAdapter
        binding.prioritySpinner.setSelection(1)

        binding.switchDeadline.setOnCheckedChangeListener { switch, isChecked ->
            binding.calendar.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        binding.deleteButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        view.findViewById<Button>(R.id.cancel_item).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            itemViewModel.setTodoItem(null)
        }

        var chosenDate: Long = binding.calendar.date

        binding.calendar.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, day ->
                val c: Calendar = Calendar.getInstance()
                c.set(year, month, day)
                chosenDate = c.getTimeInMillis()
            },
        )

        submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            itemViewModel.setTodoItem(null)
            val importance = when (binding.prioritySpinner.selectedItemId) {
                0L -> Importance.LOW
                1L -> Importance.NORMAL
                2L -> Importance.URGENT
                else -> Importance.NORMAL
            }
            var deadline: LocalDateTime? = null
            if (binding.switchDeadline.isChecked) {
                deadline = LocalDateTime.ofInstant(Instant.ofEpochMilli(chosenDate), ZoneId.systemDefault())
            }
            val todoItem = TodoItem(
                id = UUID.randomUUID().toString(),
                text = binding.taskDescription.text.toString(),
                importance = importance,
                deadline = deadline,
                isCompleted = false,
                createdAt = LocalDateTime.now(),
            )
            if (itemViewModel.selectedTodoItem.value != null) {
                repository.updateItem(itemViewModel.selectedPosition.value!!, todoItem)
            } else {
                repository.insertNewItem(
                    todoItem,
                )
            }
        }

        itemViewModel.selectedTodoItem.value?.let { item ->
            binding.taskDescription.setText(item.text)
            submitButton.isEnabled = true
            binding.prioritySpinner.setSelection(item.importance.ordinal)
            if (item.deadline != null) {
                binding.switchDeadline.isChecked = true
                val timestampInMillis = item.deadline.toInstant(ZoneOffset.UTC).toEpochMilli()
                binding.calendar.date = timestampInMillis
            } else {
                binding.switchDeadline.isChecked = false
            }
        }

        binding.taskDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                submitButton.isEnabled = !s.toString().isEmpty()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
