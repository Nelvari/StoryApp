package com.nelvari.storyapp.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.nelvari.storyapp.R

class EditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    val textError = resources.getString(R.string.message_password)
                    setError(textError, null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

    }

}