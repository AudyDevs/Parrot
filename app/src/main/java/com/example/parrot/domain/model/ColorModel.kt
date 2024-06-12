package com.example.parrot.domain.model

import com.example.parrot.R

const val INT_NULL = -1

sealed class ColorModel(val color: Int) {
    data object Blank : ColorModel(INT_NULL)
    data object Red : ColorModel(R.color.backcolorRed)
    data object Orange : ColorModel(R.color.backcolorOrange)
    data object Yellow : ColorModel(R.color.backcolorYellow)
    data object Green : ColorModel(R.color.backcolorGreen)
    data object Cyan : ColorModel(R.color.backcolorCyan)
    data object LightBlue : ColorModel(R.color.backcolorLightBlue)
    data object Blue : ColorModel(R.color.backcolorBlue)
    data object Purple : ColorModel(R.color.backcolorPurple)
    data object Rose : ColorModel(R.color.backcolorRose)
    data object Brown : ColorModel(R.color.backcolorBrown)
    data object Grey : ColorModel(R.color.backcolorGrey)
}