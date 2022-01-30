package com.notes.ui.details

import android.os.Bundle
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.di.DependencyManager
import com.notes.ui.RootActivity
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementation

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteDetailsViewModel() }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        val noteId = arguments?.getLong("noteId")
        noteId?.let { viewModel.getNoteDetails(it) }
        viewBinding.toolbar.setNavigationOnClickListener {
            findImplementation<RootActivity>()?.onBackPressed()
        }
        viewModel.note.observe(viewLifecycleOwner) {
            viewBinding.tvNameTitleNote.text = viewModel.note.value?.title
            viewBinding.tvNoteContent.text = viewModel.note.value?.content
        }

    }

}