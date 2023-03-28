package cs446.group10.gen_s.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import cs446.group10.gen_s.*
import cs446.group10.gen_s.backend.dataClasses.Plan
import cs446.group10.gen_s.backend.view_model.ViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

class EditPlanActivity : AppCompatActivity() {
    private var planId: String = "";
    private var planName: String = "";
    private var startDate: DateVal? = null;
    private var endDate: DateVal? = null;
    private var pickedColorHex: Int = 0x0
    private var planColor: String = "#ff000000"
    private lateinit var selectColorButton: MaterialButton;
    private lateinit var selectedColorBox: View;
    private lateinit var colorPicker: AlertDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_plan)
        supportActionBar?.title = "Edit Plan";

        val extras = intent.extras

        fun redirectToHome() {
            val calendarIntent = Intent(this, CalendarActivity::class.java)
            calendarIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(calendarIntent)
        }

        if (extras == null) {
            redirectToHome();
        }
        planId = extras!!.getString("planId")!!
        if (planId == null) {
            redirectToHome();
        }

        val plan: Plan? = ViewModel.getPlanById(planId);
        if (plan == null) {
            redirectToHome();
        }

        // Get plan information
        planName = plan!!.name;
        val nameTextField = findViewById<EditText>(R.id.PlanNameTextField);
        nameTextField.setText(planName);

        val startDateTime = LocalDateTime.ofEpochSecond(plan.start, 0, ZoneOffset.UTC)
        startDate = DateVal(
            startDateTime.year,
            startDateTime.monthValue - 1,
            startDateTime.dayOfMonth,
        )
        val chosenStartDate = findViewById<TextView>(R.id.chosenPlanStartDate);
        if (startDate != null) {
            chosenStartDate.text = DatePickerFragment.convertDateToString(startDate!!);
        }

        val endDateTime = LocalDateTime.ofEpochSecond(plan.end, 0, ZoneOffset.UTC)
        endDate = DateVal(
            endDateTime.year,
            endDateTime.monthValue - 1,
            endDateTime.dayOfMonth,
        )
        val chosenEndDate = findViewById<TextView>(R.id.chosenPlanEndDate);
        if (endDate != null) {
            chosenEndDate.text = DatePickerFragment.convertDateToString(endDate!!);
        }

        planColor = plan.color!!;
        selectedColorBox = findViewById<View>(R.id.colorSelectedView);
        selectedColorBox.setBackgroundColor(Color.parseColor(planColor));

        colorPicker = ColorPickerDialogBuilder
            .with(this)
            .setTitle("Choose color for your plan!")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(12)
            .setOnColorChangedListener { colorInt ->
                pickedColorHex = colorInt
            }
            .setPositiveButton("Select") { _, selectedColor, _ ->
                planColor = "#${Integer.toHexString(selectedColor)}"
                selectedColorBox.setBackgroundColor(selectedColor)

            }.setNegativeButton("Cancel") { _, _ ->
            }
            .build()


        selectColorButton = findViewById<MaterialButton>(R.id.selectColorButton)

        selectColorButton.setOnClickListener {
            colorPicker.show()
        }

        //notifications
        val notificationDetails = findViewById<LinearLayout>(R.id.NotificationDetails)
        val notificationSwitch = findViewById<Switch>(R.id.NotificationSwitch)
        notificationSwitch.isChecked = false
        notificationDetails.visibility = View.GONE

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                notificationDetails.visibility = View.VISIBLE
            } else {
                notificationDetails.visibility = View.GONE
            }
        }

        val removePlanButton = findViewById<ImageButton>(R.id.RemovePlanButton);
        removePlanButton.setOnClickListener {
            ViewModel.removePlanById(planId);
        }

        val editPlanButton = findViewById<Button>(R.id.EditPlanButton);
        editPlanButton.setOnClickListener {
            planName = nameTextField.text.toString();
            ViewModel.editPlanNameById(planId, planName);
            ViewModel.editPlanColorById(planId, planColor);

            val viewPlanIntent = Intent(this, ViewPlansActivity::class.java)
            viewPlanIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(viewPlanIntent)
        }
    }
}