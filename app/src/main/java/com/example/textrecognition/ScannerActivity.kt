package com.example.textrecognition

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
/*import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText*/
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.android.synthetic.main.activity_scanner.*
import java.util.jar.Manifest

class ScannerActivity : AppCompatActivity() {
    private lateinit var imageBitmap : Bitmap
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        detectBtn!!.setOnClickListener {
            detectText()
        }
        snapBtn!!.setOnClickListener {
            if (checkPermissions()) {
                captureImage()
            } else {
                requestPermission()
            }
            //dispatchTakePictureIntent()

        }
        
    }
    private fun detectText() {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var image = InputImage.fromBitmap(imageBitmap, 0)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                detectedTextTV.text = visionText.text
                //processText(visionText)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this, "Fail to detect the text from image", Toast.LENGTH_SHORT).show()
            }



        /*var image = InputImage.fromBitmap(imageBitmap!!, 0)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener(OnSuccessListener<FirebaseVisionText?> {
                firebaseVisionText -> processText(firebaseVisionText)
            }).addOnFailureListener(OnFailureListener {
                OnFailureListener {
                    Toast.makeText(
                        this, "Fail to detect the text from image", Toast.LENGTH_SHORT).show()
                }
            })*/
    }

    override fun onResume() {
        super.onResume()
    }
    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 200)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200){
            Toast.makeText(this, "Permission 12356 Denied", Toast.LENGTH_SHORT).show()

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                captureImage()
            } else {
                requestPermission()
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun captureImage() {
        var takePic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePic.resolveActivity(packageManager) != null) {
            startActivityForResult(takePic, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == REQUEST_IMAGE_CAPTURE){
            if(requestCode == RESULT_OK){
                imageBitmap = data!!.extras!!.get("data") as Bitmap
                capturedImageIV!!.setImageBitmap(imageBitmap)
            }
        }
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    private fun getTextRecognizer(): TextRecognizer {
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }
}