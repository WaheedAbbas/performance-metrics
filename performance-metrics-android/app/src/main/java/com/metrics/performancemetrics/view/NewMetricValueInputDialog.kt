package com.metrics.performancemetrics.view

import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.metrics.performancemetrics.R

typealias OnAddNewMetricValue = (Double) -> Unit
class NewMetricValueInputDialog (private val activity: Activity) {

    private var metricValueEdtTxt  : TextInputLayout? = null
    private var addNewMetricValueBtn  : Button? = null
    private var metricNameTxt  : TextView? = null
    fun showDialog(metricName : String, onAddNewMetricValue : OnAddNewMetricValue)
    {
        val inflater = activity.layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.new_metric_value_dialog, null)
        metricValueEdtTxt = dialogLayout.findViewById(R.id.new_metric_value_edt)
        metricNameTxt = dialogLayout.findViewById(R.id.metric_name_txt)
        addNewMetricValueBtn = dialogLayout.findViewById(R.id.add_new_metric_value_btn)
        metricValueEdtTxt?.editText?.addTextChangedListener(metricValueTxtWatcher)
        metricNameTxt?.text = metricName

        val builder: AlertDialog = AlertDialog.Builder(activity).create()
        builder.setView(dialogLayout)
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.show()
        addNewMetricValueBtn?.setOnClickListener {
            onAddNewMetricValue(metricValueEdtTxt?.editText?.text.toString().toDoubleOrNull() ?: 0.0)
            builder.dismiss()
        }

    }
    private var metricValueTxtWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            addNewMetricValueBtn?.isEnabled = s.toString().isNotBlank()
            if(s.toString().isNotBlank()) {
                addNewMetricValueBtn?.alpha = 1.0f
            }else {
                addNewMetricValueBtn?.alpha = 0.6f
            }
        }

        override fun afterTextChanged(s: Editable) {
        }
    }

}