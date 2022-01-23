package com.notes.ui.details

import android.os.Bundle
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.list.NoteListFragment
import com.notes.ui.list.NoteListItem

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
), IShowNoteDetails {
    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        viewBinding.toolbar.setNavigationOnClickListener {
            findImplementationOrThrow<FragmentNavigator>().navigateTo(NoteListFragment())
        }
    }

    override fun showNoteDetails(note: NoteListItem) {
        viewBinding?.tvNameTitleNote?.text = note.title
        viewBinding?.tvNoteContent?.text = note.content
    }
}