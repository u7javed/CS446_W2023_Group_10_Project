package cs446.group10.gen_s.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import cs446.group10.gen_s.R

class AutoGenerateActivity : AppCompatActivity() {

    private lateinit var notificationSwitch: Switch
    private lateinit var selectColorButton: Button
    private lateinit var selectedColorBox: View
    private lateinit var colorPicker: AlertDialog
    private var pickedColorHex: Int = R.color.blueAccentColor
    private var planColor: String = Integer.toHexString(pickedColorHex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_generate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Auto-Generate a Plan"

        colorPicker = ColorPickerDialogBuilder
            .with(this)
            .initialColor(R.color.blueAccentColor)
            .setTitle("Choose color for your plan!")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorChangedListener { colorInt ->
                pickedColorHex = colorInt
            }
            .setPositiveButton("Select") { _, selectedColor, _ ->
                planColor = Integer.toHexString(selectedColor)
                selectedColorBox.setBackgroundColor(selectedColor)
                println("COLOR SELECTED: $planColor")

            }.setNegativeButton("Cancel") { _, _ ->
            }
            .build()
        selectedColorBox = findViewById(R.id.colorSelectedView)
        notificationSwitch = findViewById(R.id.autoGenerateNotificationSwitch)
        selectColorButton = findViewById(R.id.selectColorButton)
        selectColorButton.setOnClickListener {
            colorPicker.show()
        }
    }

    override fun onStop() {
        colorPicker.dismiss()
        super.onStop()
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}