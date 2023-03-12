package cs446.group10.gen_s.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cs446.group10.gen_s.R

class AutoGenerateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_generate)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Auto-Generate a cs446.group10.gen_s.backend.dataClasses.Plan"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}