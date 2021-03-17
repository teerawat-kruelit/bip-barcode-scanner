package bip.qr_scanner

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.renderscript.ScriptIntrinsicResize
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.*
import java.io.IOException
import okhttp3.OkHttpClient





class MainActivity : AppCompatActivity() {
    var currScan: Int = 0
    var resultScanSlot: String = "";
    var resultScanTray: String = "";
    var clientOK = OkHttpClient();
    var gsonPretty = GsonBuilder().setPrettyPrinting().create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scanner = IntentIntegrator(this);

        val txtScan = findViewById<TextView>(R.id.txtScanSlot);
        val txtExecute = findViewById<TextView>(R.id.txtExecute);

        val btnScanSlot = findViewById<Button>(R.id.btnScanSlot);
        val btnScanTray = findViewById<Button>(R.id.btnScanTray);
        val btnExecute = findViewById<Button>(R.id.btnExecute);
        val btnReset = findViewById<Button>(R.id.btnReset);

        btnScanSlot.setOnClickListener{
            scanner.initiateScan();
            currScan = 1
            Toast.makeText(this, "Scanning Slot", Toast.LENGTH_SHORT).show()
        }

        btnScanTray.setOnClickListener{
            scanner.initiateScan();
            currScan = 2;
            Toast.makeText(this, "Scanning Tray", Toast.LENGTH_SHORT).show()
        }

        btnExecute.setOnClickListener{
            var mapResult: Map<String, Any> = HashMap()
            var bodyMap: Map<String, Any> = mapOf(
                    "slot_id" to "$resultScanSlot",
                    "slot_lock_status" to 1
            )
            var json : String = gsonPretty.toJson(bodyMap)
            var body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            var url = "http://172.29.60.56/pharmacy/pharmacy/unlock"; //SET URL HERE
            var request = Request.Builder().method("POST", body).url(url).build();

            currScan = 0;
            if(resultScanSlot.equals("") || resultScanTray.equals("")){
                Toast.makeText(this, "Some value is null", Toast.LENGTH_SHORT).show();
            }else{
                if(resultScanSlot.equals(resultScanTray)){
                    clientOK.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            txtExecute.setText("Error");
                            txtExecute.setTextColor(Color.RED)
                            println(e.printStackTrace())
                        }
                        override fun onResponse(call: Call, response: Response) {
                            var respJson = response.body().string();
                            mapResult = Gson().fromJson(respJson, mapResult.javaClass)
                            txtExecute.setText("Unlock");
                            txtExecute.setTextColor(Color.GREEN)
                        }
                    })
                }else{
                    txtExecute.setText("Error");
                    txtExecute.setTextColor(Color.RED)
                }
            }
        }

        btnReset.setOnClickListener{
            currScan = 0;
            resultScanSlot = "";
            resultScanTray = "";

            txtScan.setText("-");
            txtExecute.setText("Scan");
            txtExecute.setTextColor(Color.BLACK)
            Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    currScan = 0;
                    Toast.makeText(this, "Result is null", Toast.LENGTH_SHORT).show();
                } else {
                    if(currScan == 1){
                        val txtScanSlot = findViewById<TextView>(R.id.txtScanSlot);
                        txtScanSlot.setText(result.contents)
                        resultScanSlot = result.contents;
                    }else{
                        resultScanTray = result.contents;
                    }
                    Toast.makeText(this, "Result : " + result.contents, Toast.LENGTH_SHORT).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}