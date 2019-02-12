package com.okatanaa.timemanager.utils.TextEditorActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.okatanaa.timemanager.R
import com.okatanaa.timemanager.utils.EXTRA_EDITED_NAME
import com.okatanaa.timemanager.utils.EXTRA_EDITED_VALUE
import com.okatanaa.timemanager.utils.RC_TEXT_EDITOR_ACTIVITY


/**
 * Helper activity which always starts for result. Made in order
 * to get tool for simple text editing.
 */
class TextEditorActivity : AppCompatActivity() {

    lateinit var editedName: TextView
    lateinit var textToEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_editor)

        editedName = findViewById<TextView>(R.id.editedNameTxt)
        textToEdit = findViewById<EditText>(R.id.editedValueTxt)

        editedName.text = intent.getStringExtra(EXTRA_EDITED_NAME)
        textToEdit.setText(intent.getStringExtra(EXTRA_EDITED_VALUE))
    }

    fun onClickedCancel(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun onClickedDone(view: View) {
        val resultIntent = Intent()
        println(textToEdit.text.toString())
        resultIntent.putExtra(EXTRA_EDITED_NAME, editedName.text.toString())
        resultIntent.putExtra(EXTRA_EDITED_VALUE, textToEdit.text.toString())
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        fun launchTextEditor(context: Context, editedName: String, textToEdit: String) {
            val textEditorIntent = Intent(context, TextEditorActivity::class.java)
            textEditorIntent.putExtra(EXTRA_EDITED_NAME, editedName)
            textEditorIntent.putExtra(EXTRA_EDITED_VALUE, textToEdit)
            (context as Activity).startActivityForResult(
                textEditorIntent,
                RC_TEXT_EDITOR_ACTIVITY
            )
        }
    }
}
