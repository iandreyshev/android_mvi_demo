package ru.iandreyshev.jokesfeed.ui.feed

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import ru.iandreyshev.jokesfeed.R
import ru.iandreyshev.jokesfeed.databinding.FragmentFeedBinding
import ru.iandreyshev.jokesfeed.presentation.Action
import ru.iandreyshev.jokesfeed.presentation.FeedState
import ru.iandreyshev.jokesfeed.presentation.FilterState
import ru.iandreyshev.jokesfeed.presentation.MainStore
import ru.iandreyshev.jokesfeed.presentation.State
import ru.iandreyshev.jokesfeed.system.utils.onEachWithViewLifecycle
import ru.iandreyshev.jokesfeed.system.utils.uiLazy
import ru.iandreyshev.jokesfeed.system.utils.viewBindings

class FeedFragment : Fragment(R.layout.fragment_feed) {

    // Определяем свойства(поля) фрагмента
    private val mStore by activityViewModels<MainStore>()
    private val mBinding: FragmentFeedBinding by viewBindings(FragmentFeedBinding::bind)
    private val mAdapter by uiLazy { FeedAdapter() }
    private val mSearchQueryFlow = MutableStateFlow("")
    private lateinit var mSearchWatcher: TextWatcher

    // Вызывается при запуске фрагмента на экране
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeToStore()

        // Если фрагмент запускается первый раз, то вызываем инициализацию данных
        if (savedInstanceState == null) {
            mStore.accept(Action.Init)
        }
    }

    private fun initView() {
        mBinding.listView.adapter = mAdapter
        mSearchWatcher = mBinding.searchField.doOnTextChanged { text, _, _, _ ->
            mSearchQueryFlow.value = text.toString().trim()
        }
        mSearchQueryFlow.debounce(1000).onEachWithViewLifecycle(viewLifecycleOwner) {
            mStore.accept(Action.QueryChanged(it))
        }
        mBinding.filtersButton.setOnClickListener {
            mStore.accept(Action.OpenFilters)
        }
        mBinding.cancelButton.setOnClickListener {
            mStore.accept(Action.CancelRefresh)
        }
    }

    private fun subscribeToStore() {
        mStore.state.onEachWithViewLifecycle(viewLifecycleOwner, ::render)
    }

    private fun render(state: State) {
        renderFirstLoading(state.feedState)
        renderList(state.feedState)
        renderSearchField(state.feedState)
        renderFiltersButton(state.filterState)
        renderRefreshCard(state.feedState)
    }

    private fun renderFirstLoading(feedState: FeedState) {
        mBinding.firstLoadingView.isVisible = feedState.type == FeedState.Type.FIRST_LOADING
    }

    private fun renderList(feedState: FeedState) {
        val listItems = when {
            feedState.query.isBlank() -> feedState.feed
            else -> feedState.queriedFeed
        }
        mBinding.listView.isVisible = listItems.isNotEmpty()
        mBinding.emptyText.isVisible = listItems.isEmpty()

        if (mAdapter.highlightString != feedState.query) {
            mAdapter.highlightString = feedState.query
            mAdapter.submitList(listItems) {
                mAdapter.notifyDataSetChanged()
                mBinding.listView.smoothScrollToPosition(0)
            }
        } else {
            mAdapter.highlightString = feedState.query
            mAdapter.submitList(listItems)
        }
    }

    private fun renderSearchField(feedState: FeedState) {
        if (mBinding.searchField.text.toString() != feedState.query) {
            mBinding.searchField.removeTextChangedListener(mSearchWatcher)
            mBinding.searchField.setText(feedState.query)
            mBinding.searchField.addTextChangedListener(mSearchWatcher)
        }
    }

    private fun renderFiltersButton(filterState: FilterState) {
        mBinding.filtersBadge.isVisible = !filterState.draft.isEmpty
    }

    private fun renderRefreshCard(feedState: FeedState) {
        mBinding.refreshCard.isVisible = feedState.isRefreshing
    }

}