package com.metrics.performancemetrics.average

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.metrics.performancemetrics.R
import com.metrics.performancemetrics.util.DefaultNumberFormatter
import com.metrics.performancemetrics.util.NumberFormatter
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

class MetricAverageTextView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var averageTextValue : TextView? = null
    private var averageTextLabel : TextView? = null
    private var numberFormatter: NumberFormatter = DefaultNumberFormatter()

    init {
        val inflate = LayoutInflater.from(context).inflate(R.layout.metric_average_view, this, true)
        averageTextValue = inflate.findViewById(R.id.metric_average_value_txt_view)
        averageTextLabel = inflate.findViewById(R.id.metric_average_label_txt_view)
        initAttributes(attrs)
    }

    private fun initAttributes(attrs: AttributeSet?)
    {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MetricAverageTextView)
        val bgColor = typedArray.getColor(R.styleable.MetricAverageTextView_backgroundColor, -1)
        val labelText = typedArray.getString(R.styleable.MetricAverageTextView_label)
        typedArray.recycle()
        if (bgColor != -1) {
            applyRoundedBackground(bgColor)
        }
        averageTextLabel?.text = labelText
    }
    //For custom number formatter
    fun setNumbersFormatter(formatter: NumberFormatter)
    {
        this.numberFormatter = formatter
    }
    fun setValue(value: BigDecimal) {
        averageTextValue?.text =  numberFormatter.formatNumber(value)
    }
    private fun applyRoundedBackground(bgColor : Int) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 12.0f
        shape.setColor(bgColor)
        averageTextValue?.background = shape
    }
}