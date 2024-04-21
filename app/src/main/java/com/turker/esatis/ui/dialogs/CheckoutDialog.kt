package com.turker.esatis.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.turker.esatis.R
import com.turker.esatis.databinding.DialogCheckoutBinding
import java.text.DecimalFormat

class CheckoutDialog(context: Context) : Dialog(context, R.style.Theme_ESatis_Dialog) {

    lateinit var binding: DialogCheckoutBinding
    var total: Float = 0.0f

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCheckoutBinding.inflate(layoutInflater)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setContentView(binding.root)

        binding.tvPay.setOnClickListener { onClickListener?.onClick(it) }
    }

    override fun show() {
        super.show()
        binding.tvMessage.text = buildString {
            append("Toplam ")
            append("₺")
            append(DecimalFormat("0.00").format(total).toString())
        }
    }
}