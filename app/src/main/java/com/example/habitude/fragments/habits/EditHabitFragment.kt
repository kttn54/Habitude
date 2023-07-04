package com.example.habitude.fragments.habits

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.habitude.R
import com.example.habitude.activities.HabitActivity
import com.example.habitude.data.Habit
import com.example.habitude.databinding.FragmentEditHabitBinding
import com.example.habitude.utils.Constants.HABIT_DELETED
import com.example.habitude.utils.Constants.HABIT_OBJECT
import com.example.habitude.utils.CustomDateDecorator
import com.example.habitude.utils.Resource
import com.example.habitude.viewmodel.HabitViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditHabitFragment : Fragment() {

    private lateinit var binding: FragmentEditHabitBinding
    private val viewModel by viewModels<HabitViewModel>()
    private var habit: Habit? = null
    private var updatedHabit: Habit? = null
    private var selectedDates = mutableListOf<CalendarDay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditHabitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()

        habit = arguments?.getParcelable(HABIT_OBJECT)

        binding.etEditHabitName.setText(habit!!.name)

        val calendar = binding.calendarView
        val activity = activity as HabitActivity

        selectedDates = habit!!.selectedDates.map { convertToCalendarDay(it) }.toMutableList()

        calendar.addDecorators(CustomDateDecorator(activity, selectedDates))

        calendar.setOnDateChangedListener { _, date, selected ->
            if (selected && !selectedDates.contains(date)) {
                selectedDates.add(date)
            } else {
                selectedDates.remove(date)
            }
            Log.e("test","selecteddates is $selectedDates")
            calendar.invalidateDecorators() // Refresh the decorators
        }

        binding.btnSaveHabit.setOnClickListener {
            val editedName = binding.etEditHabitName.text.toString()
            updatedHabit = habit!!.copy(
                name = editedName,
                isDayOneComplete = habit!!.isDayOneComplete,
                isDayTwoComplete = habit!!.isDayTwoComplete,
                isDayThreeComplete = habit!!.isDayThreeComplete,
                isDayFourComplete = habit!!.isDayFourComplete,
                isDayFiveComplete = habit!!.isDayFiveComplete,
                isDaySixComplete = habit!!.isDaySixComplete,
                isDaySevenComplete = habit!!.isDaySevenComplete,
                selectedDates = selectedDates.map { convertToTimestamp(it) }.toMutableList()
            )
            viewModel.updateHabit(updatedHabit!!)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnSaveHabit.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnSaveHabit.revertAnimation()

                        findNavController().navigate(R.id.action_editHabitFragment_to_homeFragment)
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.btnDeleteHabit.setOnClickListener {
            viewModel.deleteHabit(habit!!)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteHabit.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnDeleteHabit.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnDeleteHabit.revertAnimation()

                        findNavController().previousBackStackEntry?.savedStateHandle?.set(HABIT_DELETED, habit)
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireActivity(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbarEditHabitActivity
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Edit Habit"
            toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarEditHabitActivity.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun convertToCalendarDay(timestamp: Timestamp): CalendarDay {
        val date = timestamp.toDate()
        val calendar = Calendar.getInstance()
        calendar.time = date
        return CalendarDay.from(calendar)
    }

    private fun convertToTimestamp(calendarDay: CalendarDay): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(calendarDay.year, calendarDay.month - 1, calendarDay.day)
        return Timestamp(calendar.time)
    }

}