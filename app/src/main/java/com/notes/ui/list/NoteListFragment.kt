package com.notes.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.R
import com.notes.data.NoteDbo
import com.notes.databinding.*
import com.notes.di.DependencyManager
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.details.NoteDetailsFragment
import java.time.LocalDateTime

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    private val viewModel by lazy { DependencyManager.noteListViewModel() }

    private val recyclerViewAdapter = RecyclerViewAdapter(
        this::onDeleteClick,
        this::onLongClick,
        this::onNoteClick
    )

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )

        viewBinding.createNoteButton.setOnClickListener {
            onCreateNoteDialog()
        }

        viewModel.notes.observe(
            viewLifecycleOwner
        ) {
            if (it?.size != 0) {
                viewBinding.viewFlipper.displayedChild = 0
                recyclerViewAdapter.setItems(it)
            } else {
                viewBinding.viewFlipper.displayedChild = 1
            }
        }
    }

    private fun onNoteClick(note: NoteListItem) {
        findImplementationOrThrow<FragmentNavigator>().navigateTo(NoteDetailsFragment())
    }

    private fun onDeleteClick(note: NoteListItem) {
        onDeleteNoteDialog(note)
    }

    private fun onLongClick(note: NoteListItem) {
        onEditNoteDialog(note)
    }

    private fun onEditNoteDialog(note: NoteListItem) {
        val customDialogEditEmployeeBinding =
            CustomDialogEditNoteBinding.inflate(
                layoutInflater
            )
        customDialogEditEmployeeBinding.tvTitle.setText(R.string.note_editing)
        customDialogEditEmployeeBinding.tvNewNoteTitle.setText(R.string.note_title)
        customDialogEditEmployeeBinding.etNewNoteTitle.setText(note.title)
        customDialogEditEmployeeBinding.tvNoteContent.setText(R.string.note_content)
        customDialogEditEmployeeBinding.etNewNoteContent.setText(note.content)
        val customAlertBuilder = AlertDialog.Builder(requireContext())
            .setView(customDialogEditEmployeeBinding.root)
            .create()
        customDialogEditEmployeeBinding.btnEditCancel.setOnClickListener { customAlertBuilder.dismiss() }
        customDialogEditEmployeeBinding.btnEditOk.setOnClickListener {
            val newNoteTitle =
                customDialogEditEmployeeBinding.etNewNoteTitle.text.toString().trim()
            val newNoteContent =
                customDialogEditEmployeeBinding.etNewNoteContent.text.toString().trim()
            if ((newNoteTitle.isEmpty() || newNoteContent.isEmpty()) ||
                (newNoteTitle.matches(Regex("")) || newNoteContent.matches(Regex("")))
            ) {
                showMessage("Enter data for all fields!")
            } else {
                note.title = newNoteTitle
                note.content = newNoteContent
                viewModel.editNote(note)
                customAlertBuilder.dismiss()
            }
        }
        customAlertBuilder.show()
    }


    private fun onDeleteNoteDialog(note: NoteListItem) {
        val customDialogDeleteFromOurCompanyBinding =
            CustomDialogDeleteNoteBinding.inflate(
                layoutInflater
            )
        customDialogDeleteFromOurCompanyBinding.tvNoteTitle.setText(R.string.delete_note_title)
        customDialogDeleteFromOurCompanyBinding.tvNoteDescription.setText(R.string.delete_note_description)
        val customAlertBuilder = AlertDialog.Builder(requireContext())
            .setView(customDialogDeleteFromOurCompanyBinding.root)
            .create()
        customDialogDeleteFromOurCompanyBinding.btnDeleteCancel.setOnClickListener { customAlertBuilder.dismiss() }
        customDialogDeleteFromOurCompanyBinding.btnDeleteOk.setOnClickListener {
            note.id.let { it1 -> viewModel.deleteNote(it1) }
            customAlertBuilder.dismiss()
        }
        customAlertBuilder.show()
    }

    private fun onCreateNoteDialog() {
        val customDialogAddNoteBinding =
            CustomDialogAddNoteBinding.inflate(
                layoutInflater
            )
        customDialogAddNoteBinding.tvAddingNote.setText(R.string.adding_a_note)
        customDialogAddNoteBinding.tvNoteTitle.setText(R.string.note_title)
        customDialogAddNoteBinding.tvNoteContent.setText(R.string.note_content)
        val customAlertBuilder = AlertDialog.Builder(requireContext())
            .setView(customDialogAddNoteBinding.root)
            .create()
        customDialogAddNoteBinding.btnCancel.setOnClickListener { customAlertBuilder.dismiss() }
        customDialogAddNoteBinding.btnOk.setOnClickListener {
            val noteName =
                customDialogAddNoteBinding.etNoteTitle.text.toString().trim()
            val noteContent =
                customDialogAddNoteBinding.etNoteContent.text.toString().trim()
            if ((noteName.isEmpty() || noteContent.isEmpty()) ||
                (noteName.matches(Regex("")) || noteContent.matches(Regex("")))
            ) {
                showMessage("Enter data for all fields!")
            } else {
                val note =
                    NoteDbo(0, noteName, noteContent, LocalDateTime.now(), LocalDateTime.now())
                viewModel.insertNote(note)
                customAlertBuilder.dismiss()
            }
        }
        customAlertBuilder.show()
    }

    private fun showMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private class RecyclerViewAdapter(
        private val onDeleteClick: (NoteListItem) -> Unit,
        private val onLongClick: (NoteListItem) -> Unit,
        private val onNoteClick: (NoteListItem) -> Unit
    ) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position])
            val note = items[position]
            holder.btnDelete.setOnClickListener {
                onDeleteClick.invoke(note)
            }
            holder.itemView.setOnLongClickListener {
                onLongClick.invoke(note)
                true
            }
            holder.itemView.setOnClickListener {
                onNoteClick.invoke(note)
            }
        }

        override fun getItemCount() = items.size

        @SuppressLint("NotifyDataSetChanged")
        fun setItems(
            items: List<NoteListItem>?
        ) {
            this.items.clear()
            if (items != null) {
                this.items.addAll(items)
            }
            notifyDataSetChanged()
        }

        private class ViewHolder(
            private val binding: ListItemNoteBinding
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {
            val btnDelete: ImageButton = binding.ivDeleteNote
            fun bind(
                note: NoteListItem
            ) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content
            }

        }

    }

}