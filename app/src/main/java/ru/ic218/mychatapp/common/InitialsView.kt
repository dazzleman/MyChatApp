package ru.ic218.mychatapp.common

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ru.ic218.mychatapp.R

class InitialsView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private val colors = arrayOf(
            R.color.initials_bg_color_green,
            R.color.initials_bg_color_blue,
            R.color.initials_bg_color_red,
            R.color.initials_bg_color_light_blue,
            R.color.initials_bg_color_orange,
            R.color.initials_bg_color_pink
        )
    }

    fun populateInitials(name: String) {
        val items = name.split(" ")
        if (items.isNotEmpty() && items.size >= 2) text = "${items[0][0].toUpperCase()}${items[1][0].toUpperCase()}"
        else text = "${name[0].toUpperCase()}${name[1].toUpperCase()}"
        setColorsByName(name)
    }

    private fun setColorsByName(name: String) {
        val paintColor = ContextCompat.getColor(context, colors[getColorIndexByName(name)])
        val textColor = ContextCompat.getColor(context, R.color.white_100)

        val oval = ShapeDrawable(OvalShape()).apply {
            paint.color = paintColor
        }
        background = oval
        setTextColor(textColor)
    }

    private fun getColorIndexByName(name: String): Int {
        val div = Math.abs(name.hashCode() % colors.size)
        return if (div >= colors.size) colors.size - 1 else div
    }
}