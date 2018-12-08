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
import com.okatanaa.timemanager.utilities.RC_TEXT_EDITOR_ACTIVITY
import java.lang.Exception


/*
* This is a helper class, that starts TextEditorActivity in order to edit text.
* It does not implement OnClickListener interface, because the string that you
* need to change can be change during the time the application is running;
* in case you instantiate it the object would receive the only unmutable
* string; this problem cause by that variables in Java contain the links,
* not values. So if you want to use it, you need to call the onClick function.
*
 */
class TextClickedListener{
    companion object {
        fun onClick(context: Context, editedName: String, textToEdit: String) {
            val textEditorIntent = Intent(context, TextEditorActivity::class.java)
            textEditorIntent.putExtra(EXTRA_EDITED_NAME, editedName)
            textEditorIntent.putExtra(EXTRA_EDITED_VALUE, textToEdit)
            Toast.makeText(context, "I'm here!", Toast.LENGTH_SHORT).show()
            (context as Activity).startActivityForResult(textEditorIntent, RC_TEXT_EDITOR_ACTIVITY)
        }
    }
}