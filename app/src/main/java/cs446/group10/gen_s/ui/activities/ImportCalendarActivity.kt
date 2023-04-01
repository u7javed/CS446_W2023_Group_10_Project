package cs446.group10.gen_s.ui.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R
import cs446.group10.gen_s.backend.view_model.ViewModel.icsToEvents


class ImportCalendarActivity : AppCompatActivity() {

    private lateinit var btnImport: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_calendar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Import Calendar"
        btnImport = findViewById<Button>(R.id.btnImportCalendar)
        btnImport.setOnClickListener {
            openDocumentPicker()
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val duration = Toast.LENGTH_SHORT

        if (uri != null) {
            var errorCodeImportCal = icsToEvents(uri)
            if (errorCodeImportCal == 1) {
                val toast = Toast.makeText(applicationContext, "Unable to import calendar due to file type or conflicts", duration)
                toast.show()
            } else if (errorCodeImportCal == 0) {
                val toast = Toast.makeText(applicationContext, "Import calendar successfully", duration)
                toast.show()
            }
        } else {
            val toast = Toast.makeText(applicationContext, "URI not selected", duration)
            toast.show()
        }
    }

    private fun openDocumentPicker() {
        getContent.launch("text/calendar")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}