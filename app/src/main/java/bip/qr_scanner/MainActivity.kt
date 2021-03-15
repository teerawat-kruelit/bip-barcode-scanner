package bip.qr_scanner

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity : AppCompatActivity() {
    var currScan: Int = 0
    var resultScanSlot: String = "";
    var resultScanTray: String = "";

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
            currScan = 0;
            if(resultScanSlot.equals("") || resultScanTray.equals("")){
                Toast.makeText(this, "Some value is null", Toast.LENGTH_SHORT).show();
            }else{
                if(resultScanSlot.equals(resultScanTray)){
                    txtExecute.setText("Unlock");
                    txtExecute.setTextColor(Color.GREEN)
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