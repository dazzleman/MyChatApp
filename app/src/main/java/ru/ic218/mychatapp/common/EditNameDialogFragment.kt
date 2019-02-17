package ru.ic218.mychatapp.common

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.view_edit_name.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast

class EditNameDialogFragment : DialogFragment(), TextView.OnEditorActionListener {

    companion object {
        private const val TEXT = "text"

        fun newInstance(text: String = ""): EditNameDialogFragment {
            return EditNameDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(TEXT, text)
                    setStyle(STYLE_NO_TITLE, 0)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.ic218.mychatapp.R.layout.view_edit_name, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountEditOk.setOnClickListener {
            handleOk()
        }

        accountNameEdit.setText(arguments?.getString(TEXT))
        accountNameEdit.requestFocus()
        accountNameEdit.setOnEditorActionListener(this)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    override fun onResume() {
        val params = dialog?.window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        dialog?.window?.attributes = params as android.view.WindowManager.LayoutParams

        super.onResume()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            handleOk()
        }
        Toast.makeText(context, "Ты не прав!", Toast.LENGTH_LONG).show()
        return false
    }

    private fun handleOk(): Boolean {
        if (!accountNameEdit.text.isEmpty()) {
            val listener = activity as EditNameDialogListener?
            listener?.onFinishEditDialog(accountNameEdit.text.toString())
            dismiss()
            return true
        }
        return false
    }

    interface EditNameDialogListener {
        fun onFinishEditDialog(inputText: String)
    }
}