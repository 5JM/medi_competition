package com.example.donkin

import android.R
import android.app.Activity
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*


class SaturdayDec : DayViewDecorator {
    val calendar = Calendar.getInstance()
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
//        view?.setSelectionDrawable(drawable!!)
    }
   fun setText(text:String){
//       textView?.text=text
   }
}