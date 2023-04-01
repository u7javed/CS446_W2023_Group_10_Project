package cs446.group10.gen_s.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cs446.group10.gen_s.R


class ImportCalendarActivity : AppCompatActivity() {
    val PICKFILE_RESULT_CODE = 2
    private lateinit var btnImport: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_calendar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Import cs446.group10.gen_s.backend.dataClasses.Calendar"
        btnImport = findViewById<Button>(R.id.btnImportCalendar)
        btnImport.setOnClickListener {
            openDocumentPicker()
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val text = "Hello toast!"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, uri.toString(), duration)
        toast.show()
    }

    private fun openDocumentPicker() {
        getContent.launch("*/*")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}