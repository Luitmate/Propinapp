 package com.lukitmate.android.propinapp

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

 private lateinit var seekBarTip : SeekBar
 private lateinit var tvTipPercent : TextView
 private lateinit var etBase : EditText

 private lateinit var tvTipAmount : TextView
 private lateinit var tvTotalAmount: TextView
 private lateinit var tvTipDescription: TextView

 private const val TAG = "MainActivity"
 private const val INITIAL_TIP_PERCENT = 15

 class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip = findViewById<SeekBar>(R.id.seekBarTip)
        tvTipPercent = findViewById<TextView>(R.id.tvTipPercent)
        etBase = findViewById<EditText>(R.id.etBase)
        tvTipAmount = findViewById<TextView>(R.id.tvTipAmount)
        tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)
        tvTipDescription = findViewById<TextView>(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "OnProgressChanged $progress")
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBase.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}


        })

    }

     private fun updateTipDescription(tipPercent: Int) {
         val tipDescription : String = when (tipPercent) {
             in 0..9 -> "Pobre"
             in 10..14 -> "Aceptable"
             in 15..19 -> "Buena"
             in 20..24 -> "Muy buena"
             else -> "Excelente!"
         }
         tvTipDescription.text = tipDescription
         val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
             ContextCompat.getColor(this,R.color.colorWorstTip),
             ContextCompat.getColor(this,R.color.colorBestTip)
         ) as Int
         tvTipDescription.setTextColor(color)
     }

     private fun computeTipAndTotal() {
        if(etBase.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text= ""
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
     }
 }