package ru.iandreyshev.jokesfeed.ui.filter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.iandreyshev.jokesfeed.R
import ru.iandreyshev.jokesfeed.databinding.FragmentFilterBinding
import ru.iandreyshev.jokesfeed.domain.Topic
import ru.iandreyshev.jokesfeed.presentation.Action
import ru.iandreyshev.jokesfeed.presentation.Event
import ru.iandreyshev.jokesfeed.presentation.MainStore
import ru.iandreyshev.jokesfeed.presentation.State
import ru.iandreyshev.jokesfeed.system.utils.onEachWithViewLifecycle
import ru.iandreyshev.jokesfeed.system.utils.viewBindings

class FilterFragment : Fragment(R.layout.fragment_filter) {

    // Определяем свойства(поля) фрагмента
    private val mBinding: FragmentFilterBinding by viewBindings(FragmentFilterBinding::bind)
    private val mStore by activityViewModels<MainStore>()

    // Вызывается при запуске фрагмента на экране
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeToStore()
    }

    private fun initView() {
        mBinding.toolbar.setNavigationOnClickListener { mStore.accept(Action.CloseFilters) }

        Topic.values().forEach {
            val checkbox = CheckBox(requireContext())
            checkbox.text = it.name()
            checkbox.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            checkbox.setOnCheckedChangeListener { _, _ ->
                mStore.accept(Action.SelectTopics(topics = mBinding.topicsList
                    .children
                    .mapIndexedNotNull { i, view ->
                        if ((view as CheckBox).isChecked) Topic.values()[i] else null
                    }
                    .toSet()))
            }
            mBinding.topicsList.addView(checkbox)
        }

        mBinding.submitButton.setOnClickListener { mStore.accept(Action.ApplyFilters) }
    }

    private fun subscribeToStore() {
        mStore.state.onEachWithViewLifecycle(viewLifecycleOwner, ::render)
        mStore.event.onEachWithViewLifecycle(viewLifecycleOwner, ::handleEvent)
    }

    private fun render(state: State) {
        mBinding.topicsList.forEachIndexed { i, view ->
            (view as CheckBox).isChecked = Topic.values()[i] in state.filterState.draft.topics
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.ShowMessage ->
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
        }
    }

}
