package com.notes.ui.details

import android.os.Bundle
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.ui.RootActivity
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementation

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {
    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        viewBinding.toolbar.setNavigationOnClickListener {
            findImplementation<RootActivity>()?.onBackPressed()
        }
    }
}