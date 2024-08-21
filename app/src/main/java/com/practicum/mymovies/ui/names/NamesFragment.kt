package com.practicum.mymovies.ui.names

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.practicum.mymovies.databinding.FragmentNamesBinding
import com.practicum.mymovies.domain.models.Person
import com.practicum.mymovies.presentation.names.NamesState
import com.practicum.mymovies.presentation.names.NamesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NamesFragment : Fragment() {

    private val viewModel by viewModel<NamesViewModel>()
    private val adapter = PersonsAdapter()

    private lateinit var binding: FragmentNamesBinding
    private lateinit var textWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Список
        binding.personsList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.personsList.adapter = adapter

        //Подключаем слушатель ввода
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    searchText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        textWatcher.let {
            binding.queryInput.addTextChangedListener(it)
        }

        viewModel.observeState.observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

    }

    private fun render(state: NamesState) {
        when (state) {
            is NamesState.Content -> showContent(state.persons)
            is NamesState.Empty -> showEmpty(state.message)
            is NamesState.Error -> showError(state.message)
            NamesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {

        with(binding) {
            personsList.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }

    }

    private fun showError(errorMessage: String) {
        with(binding) {
            personsList.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            placeholderMessage.text = errorMessage
        }
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(persons: List<Person>) {

        with(binding) {
            personsList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
        }

        adapter.persons.clear()
        adapter.persons.addAll(persons)
        adapter.notifyDataSetChanged()

    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}