package com.turker.esatis.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.turker.esatis.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context) {

    lateinit var binding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setContentView(binding.root)
        setCancelable(false)
    }
}