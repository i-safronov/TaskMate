package safronov.apps.taskmate.project.system_settings.ui.text_watcher

import android.text.Editable
import android.widget.EditText

interface TextWatcher {

    fun addTextWatcherToView(view: EditText, afterTextChanged: (String) -> Unit)

    class Base: TextWatcher {
        override fun addTextWatcherToView(view: EditText, afterTextChanged: (String) -> Unit) {
            view.addTextChangedListener(object: android.text.TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(p0: Editable?) {
                    afterTextChanged.invoke(p0.toString())
                }
            })
        }
    }

}