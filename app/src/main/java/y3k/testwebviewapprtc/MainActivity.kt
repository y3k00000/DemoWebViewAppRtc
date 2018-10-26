package y3k.testwebviewapprtc

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_go.setOnClickListener {
            startActivity(Intent(this@MainActivity,TestWebViewAppRTCActivity::class.java).apply {
                this.putExtra("url", when {
                    radio_btn_apprtc.isChecked -> "https://appr.tc/r/" + edittext_apprtc.text
                    radio_btn_cmore_webrtc.isChecked -> "https://api.cmoremap.com.tw:8050/"
                    radio_btn_cmore_webrtc_custom.isChecked -> "https://api.cmoremap.com.tw:8050/call/" + edittext_cmore_webrtc_custom_call.text + "/from/" + edittext_cmore_webrtc_custom_from.text
                    else -> "https://appr.tc"
                })
            })
            finish()
        }
    }
}
