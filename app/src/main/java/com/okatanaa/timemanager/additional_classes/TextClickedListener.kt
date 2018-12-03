package com.okatanaa.timemanager.additional_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.okatanaa.timemanager.controller.TextEditorActivity
import com.okatanaa.timemanager.utilities.EXTRA_EDITED_NAME
import com.okatanaa.timemanager.utilities.EXTRA_EDITED_VALUE
import java.lang.Exception

class TextClickedListener{
    companion object {
        fun onClick(context: Context, editedName: String, textToEdit: String) {
            val textEditorIntent = Intent(context, TextEditorActivity::class.java)
            textEditorIntent.putExtra(EXTRA_EDITED_NAME, editedName)
            textEditorIntent.putExtra(EXTRA_EDITED_VALUE, textToEdit)
            Toast.makeText(context, "I'm here!", Toast.LENGTH_SHORT).show()
            (context as Activity).startActivityForResult(textEditorIntent, 0)
        }
    }
}