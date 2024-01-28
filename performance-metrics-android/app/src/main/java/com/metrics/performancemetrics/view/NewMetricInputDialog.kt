package com.metrics.performancemetrics.view

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.metrics.performancemetrics.R


typealias OnAddNewMetric = (String) -> Unit
class NewMetricInputDialog (private val activity: Activity)
{
    private var metricEdtTxt  : TextInputLayout? = null
    private var addNewMetricBtn  : Button? = null
    fun showDialog(onAddNewMetric : OnAddNewMetric) {
        val inflater = activity.layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.new_metric_dialog, null)
        metricEdtTxt = dialogLayout.findViewById(R.id.new_metric_name_edt)
        addNewMetricBtn = dialogLayout.findViewById(R.id.add_new_metric_btn)
        metricEdtTxt?.editText?.addTextChangedListener(textWatcher)
        val builder: AlertDialog = AlertDialog.Builder(activity).create()
        builder.setView(dialogLayout)
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.show()
        addNewMetricBtn?.setOnClickListener {
            onAddNewMetric(metricEdtTxt?.editText?.text.toString())
            builder.dismiss()
        }

    }

    private val textWatcher : TextWatcher = object : TextWatcher
    {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            addNewMetricBtn?.isEnabled = s.toString().isNotBlank()
            if (s.toString().isNotBlank()) {
                addNewMetricBtn?.alpha = 1.0f
            } else {
                addNewMetricBtn?.alpha = 0.6f
            }
         }

        override fun afterTextChanged(s: Editable) {

         }

    }

}