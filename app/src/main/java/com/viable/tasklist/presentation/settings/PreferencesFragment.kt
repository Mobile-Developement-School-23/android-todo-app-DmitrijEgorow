package com.viable.tasklist.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.viable.tasklist.R
import com.viable.tasklist.data.prefs.PreferencesRepository
import com.viable.tasklist.databinding.FragmentPreferencesBinding
import com.viable.tasklist.presentation.MainActivity
import com.viable.tasklist.presentation.TaskViewModelFactory
import com.viable.tasklist.utils.ThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * A [Fragment] to set app preferences.
 */
class PreferencesFragment : Fragment() {

    private var _binding: FragmentPreferencesBinding? = null
    private val binding get() = _binding!!
    private val uiScope = CoroutineScope(Dispatchers.Main)

    @Inject
    lateinit var repositoryPrefs: PreferencesRepository

    private val preferencesViewModel: PreferencesViewModel by activityViewModels {
        TaskViewModelFactory.PreferencesViewModelFactory(
            repositoryPrefs,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as MainActivity).activityComponent.inject(this)
        _binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val types = resources.getStringArray(R.array.theme)
        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_spinner_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.themeSpinner.adapter = arrayAdapter
        preferencesViewModel.themeLiveData.observe(viewLifecycleOwner) { theme ->
            binding.themeSpinner.setSelection(theme.ordinal)
        }
        binding.themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> preferencesViewModel.onThemeUpdate(ThemePreferences.LIGHT)
                    1 -> preferencesViewModel.onThemeUpdate(ThemePreferences.DARK)
                    2 -> preferencesViewModel.onThemeUpdate(ThemePreferences.SYSTEM)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        /*binding..setOnClickListener { v ->
            v.isActivated = !(v.isActivated)
            *//*AppCompatDelegate.setDefaultNightMode(
                if (v.isActivated) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )*//*
            preferencesViewModel.onThemeUpdate(ThemePreferences.LIGHT)
            preferencesViewModel.themeLiveData.observe(viewLifecycleOwner) {

            }
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
