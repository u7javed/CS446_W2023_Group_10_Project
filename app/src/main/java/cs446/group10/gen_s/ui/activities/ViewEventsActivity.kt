package cs446.group10.gen_s.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cs446.group10.gen_s.R

class ViewEventsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "View Events"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}