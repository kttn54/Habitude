package com.example.habitude.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.habitude.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Creates and displays a BottomSheetDialog for resetting/changing the password.
 * The dialog contains an EditText for email input and two buttons for 'send' and 'cancel'.
 * The 'send' button's click behavior is defined by the passed lambda function 'onSendClick'.
 * The 'cancel' button dismisses the dialog.
 */

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.et_reset_password)
    val btnSend = view.findViewById<Button>(R.id.btn_send)
    val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

    btnSend?.setOnClickListener {
        val email = etEmail?.text?.toString()?.trim()
        if (!email.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onSendClick(email)
            dialog.dismiss()
        } else {
            etEmail?.error = "Enter a valid email"
        }
    }

    btnCancel?.setOnClickListener {
        dialog.dismiss()
    }
}