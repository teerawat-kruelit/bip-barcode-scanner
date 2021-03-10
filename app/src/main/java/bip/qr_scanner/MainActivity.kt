package bip.qr_scanner

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator


class MainActivity : AppCompatActivity() {
    var currScan: Int = 0
    var resultScan1: String = "";
    var resultScan2: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scanner = IntentIntegrator(this);

        val txtScan1 = findViewById<TextView>(R.id.txtResultScan1);
        val txtScan2 = findViewById<TextView>(R.id.txtResultScan2);
        val txtExecute = findViewById<TextView>(R.id.txtResultExecute);

        val btnScan1 = findViewById<Button>(R.id.btnScan1);
        val btnScan2 = findViewById<Button>(R.id.btnScan2);
        val btnExecute = findViewById<Button>(R.id.btnExecute);
        val btnReset = findViewById<Button>(R.id.btnReset);

        btnScan1.setOnClickListener{
            scanner.initiateScan();
            currScan = 1
            Toast.makeText(this, "Scanning on 1", Toast.LENGTH_SHORT).show()
        }

        btnScan2.setOnClickListener{
            scanner.initiateScan();
            currScan = 2;
            Toast.makeText(this, "Scanning on 2", Toast.LENGTH_SHORT).show()
        }

        btnExecute.setOnClickListener{
            currScan = 0;
            if(resultScan1.equals("") || resultScan2.equals("")){
                txtExecute.setText("Some value is null");
                Toast.makeText(this, "Some value is null", Toast.LENGTH_SHORT).show()
            }else{
                if(resultScan1.equals(resultScan2)){
                    txtExecute.setText("Match !");
                    Toast.makeText(this, "Match !", Toast.LENGTH_SHORT).show()
                }else{
                    txtExecute.setText("Not match !");
                    Toast.makeText(this, "Not match !", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnReset.setOnClickListener{
            currScan = 0;
            resultScan1 = "";
            resultScan2 = "";
            txtScan1.setText("ResultScan1");
            txtScan2.setText("ResultScan2");
            txtExecute.setText("ResultExecute");
            Toast.makeText(this, "Reset Value", Toast.LENGTH_SHORT).show()
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
                        val txtScan1 = findViewById<TextView>(R.id.txtResultScan1);
                        txtScan1.setText(result.contents)
                        resultScan1 = result.contents;
                    }else{
                        val txtScan2 = findViewById<TextView>(R.id.txtResultScan2);
                        txtScan2.setText(result.contents)
                        resultScan2 = result.contents;
                    }
                    Toast.makeText(this, "Result : " + result.contents, Toast.LENGTH_SHORT).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}