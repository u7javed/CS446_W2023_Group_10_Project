package cs446.group10.gen_s
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import cs446.group10.gen_s.ui.activities.GeneratePlanActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerClickListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

data class NotificationDetail(
    var notificationOn: Boolean,
    var notificationTime: String,
    var notificationUnitPosition: Int,
)
data class PlanBasicInfoDetail(
    val valid: Boolean,
    val planName: String,
    var startDate: DateVal?,
    var endDate: DateVal?,
    var planColor: String,
    var notification: NotificationDetail,
)


/**
 * A simple [Fragment] subclass.
 * Use the [PlanBasicInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlanBasicInfoFragment(
    private val initialPlanBasicInfo: PlanBasicInfoDetail,
): Fragment(R.layout.fragment_plan_basic_info) {

    private var startDate: DateVal? = null;
    private var endDate: DateVal? = null;
    private var pickedColorHex: Int = 0x0;
    private var planColor: String = "#ff000000";
    private var notificationOn: Boolean = false;
    private var notificationTime = "";
    private var notificationUnitPosition = 0;
    private lateinit var nameTextField: EditText;
    private lateinit var selectedColorBox: View;
    private lateinit var colorPicker: AlertDialog;
    private lateinit var notificationSwitch: Switch;
    private lateinit var notificationTimeEditText: EditText;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startDate = initialPlanBasicInfo.startDate;
        endDate = initialPlanBasicInfo.endDate;
        nameTextField = view.findViewById(R.id.PlanNameTextField);
        nameTextField.setText(initialPlanBasicInfo.planName);
        planColor = initialPlanBasicInfo.planColor;
        selectedColorBox = view.findViewById(R.id.colorSelectedView);
        selectedColorBox.setBackgroundColor(Color.parseColor(planColor));
        notificationOn = initialPlanBasicInfo.notification.notificationOn;
        notificationTimeEditText = view.findViewById(R.id.NotificationTime);
        if (notificationOn) {
            notificationUnitPosition = initialPlanBasicInfo.notification.notificationUnitPosition;
            notificationTime = initialPlanBasicInfo.notification.notificationTime;
        }

        val chosenStartDate = view.findViewById<TextView>(R.id.chosenPlanStartDate);
        if (startDate != null) {
            chosenStartDate.text = DatePickerFragment.convertDateToString(startDate!!);
        }

        val chosenEndDate = view.findViewById<TextView>(R.id.chosenPlanEndDate);
        if (endDate != null) {
            chosenEndDate.text = DatePickerFragment.convertDateToString(endDate!!);
        }

        // start date

        val startDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.PlanStartDatePickerFragment, startDateCalendarFragment);
            commit();
        }

        fun setStartDate(dateVal: DateVal) {
            val chosenStartDate = view.findViewById<TextView>(R.id.chosenPlanStartDate);
            startDate = dateVal;
            chosenStartDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

        val selectStartDateButton = view.findViewById<MaterialButton>(R.id.selectPlanStartDate);
        selectStartDateButton.setOnClickListener {
            startDateCalendarFragment.showDatePicker(
                DatePickerInitialVal(::setStartDate, startDate)
            );
        }

        // end date

        val endDateCalendarFragment = DatePickerFragment();
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.PlanEndDatePickerFragment, endDateCalendarFragment);
            commit();
        }

        fun setEndDate(dateVal: DateVal) {
            val chosenEndDate = view.findViewById<TextView>(R.id.chosenPlanEndDate);
            endDate = dateVal;
            chosenEndDate.text = DatePickerFragment.convertDateToString(dateVal);
        }

        val selectEndDateButton = view.findViewById<MaterialButton>(R.id.selectPlanEndDate);
        selectEndDateButton.setOnClickListener {
            endDateCalendarFragment.showDatePicker(
                DatePickerInitialVal(::setEndDate, endDate)
            );
        }

        // notification
        notificationSwitch = view.findViewById(R.id.NotificationSwitch);
        notificationSwitch.isChecked = notificationOn;

        val notificationDetails = view.findViewById<LinearLayout>(R.id.NotificationDetails);
            notificationDetails.visibility = if (notificationOn) View.VISIBLE else View.GONE;

        notificationSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            notificationOn = isChecked;
            notificationDetails.visibility = if (notificationOn) View.VISIBLE else View.GONE;
        })

        notificationTimeEditText.setText(notificationTime);

        val timeUnits = resources.getStringArray(R.array.UnitsOfTime);
        val timeUnitsSpinner = view.findViewById<Spinner>(R.id.NotificationUnit)
        if (timeUnitsSpinner != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    timeUnits
                )
            }
            if (adapter != null) {
                timeUnitsSpinner.setSelection(notificationUnitPosition);
            };
            timeUnitsSpinner.adapter = adapter;
            timeUnitsSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?, position: Int, id: Long) {
                        notificationUnitPosition = position;
                        }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        notificationUnitPosition = -1;
                    }
                }
        }

        // colour picker
        colorPicker = ColorPickerDialogBuilder
            .with(context)
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

        selectedColorBox.setOnClickListener {
            colorPicker.show()
        }
    }

    public fun getPlanBasicInfo(): PlanBasicInfoDetail {
        return PlanBasicInfoFragment.formPlanBasicInfo(
            nameTextField,
            startDate,
            endDate,
            planColor,
            NotificationDetail(
                notificationSwitch.isChecked,
                notificationTimeEditText.text.toString(),
                notificationUnitPosition,
            ),
        );
    }

    companion object {
        fun getMultiplierFromUnitPosition(position: Int): Long {
            val notificationMultiplier = when(position) {
                0 -> 60L
                1 -> 3600L
                2 -> 86400L
                3 -> 604800L
                4 -> 2628000L
                else -> 0L
            };
            return notificationMultiplier;
        }

        fun validateNotification(notification: NotificationDetail, startDate: DateVal?): Boolean {
            if (startDate == null) {
                return false;
            }
            var valid = true;
            if (notification.notificationOn) {
                if (notification.notificationTime.isEmpty()) {
                    valid = false;
                } else {
                    val notificationMultiplier = getMultiplierFromUnitPosition(notification.notificationUnitPosition);
                    val startDateLong = LocalDateTime.of(
                        startDate.year,
                        startDate.month + 1,
                        startDate.day,
                        0,
                        0,
                    ).toEpochSecond(ZoneOffset.UTC);
                    if (startDateLong - (notification.notificationTime.toLong() * notificationMultiplier) <=
                        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) {
                        valid = false;
                    }
                }
            }
            return valid;
        }
        fun formPlanBasicInfo(planName: String, startDate: DateVal?, endDate: DateVal?, planColor: String, notification: NotificationDetail): PlanBasicInfoDetail {
            var valid = planName != "" && startDate != null && endDate != null && planColor != "";
            valid = valid && validateNotification(notification, startDate);
            return PlanBasicInfoDetail(
                valid,
                planName,
                startDate,
                endDate,
                planColor,
                notification,
            )
        }
        fun formPlanBasicInfo(nameTextField: EditText, startDate: DateVal?, endDate: DateVal?, planColor: String, notification: NotificationDetail): PlanBasicInfoDetail {
            var valid = nameTextField != null && startDate != null && endDate != null && planColor != "";
            valid = valid && nameTextField!!.text.toString() != "";
            valid = valid && validateNotification(notification, startDate);
            return PlanBasicInfoDetail(
                valid,
                nameTextField!!.text.toString(),
                startDate,
                endDate,
                planColor,
                notification
            )
        }
    }
}