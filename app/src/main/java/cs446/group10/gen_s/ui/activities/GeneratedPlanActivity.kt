package cs446.group10.gen_s.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cs446.group10.gen_s.R

class GeneratedPlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generated_plan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Your New Plan"
    }

    fun cancelBtnClickHandler(view: View) {
        Toast.makeText(this, "Cancel Button Clicked", Toast.LENGTH_SHORT).show()
    }

    fun confirmBtnClickHandler(view: View) {
        Toast.makeText(this, "Confirm Button Clicked", Toast.LENGTH_SHORT).show()
    }
}