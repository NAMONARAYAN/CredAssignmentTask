package com.app.credassignmenttask.fragments

import android.content.Intent
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.PreviewConfig.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.app.credassignmenttask.R
import com.app.credassignmenttask.utils.LuminosityAnalyzer
import com.app.credassignmenttask.viewmodels.HomeScreenViewModel
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.frag_upload_screen.*
import java.io.File
import java.util.concurrent.Executors

class UplaodImageFragment : MasterFragment(), LifecycleOwner, View.OnClickListener {

    var mMainActivityViewModel: HomeScreenViewModel? = null

    private val executor = Executors.newSingleThreadExecutor()
    private var lensFacing = CameraX.LensFacing.BACK
    private lateinit var container: ConstraintLayout
    private lateinit var mCropImageUri: Uri

    private var left: Int = 0
    private var right: Int = 0
    private var top: Int = 0
    private var bottom: Int = 0

    companion object {
        fun getNewInstance(): UplaodImageFragment {
            val fragment = UplaodImageFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_upload_screen, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            mMainActivityViewModel = ViewModelProviders.of(it).get(HomeScreenViewModel::class.java)
        }
        container = view as ConstraintLayout

        view_finder.post { startCamera() }
        iv_back.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_back -> {
                popBackStack(activity!!.supportFragmentManager)
            }
        }
    }

    private fun startCamera() {
        CameraX.unbind()
        val previewConfig = Builder().apply {
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
            setLensFacing(lensFacing)
        }.build()

        // Build the viewfinder use case
        val preview = Preview(previewConfig)
        val myZoom = Rect(0, 0, 0, 0)
        preview.zoom(myZoom)
        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = view_finder.parent as ViewGroup
            parent.removeView(view_finder)
            parent.addView(view_finder, 0)

            view_finder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }
        // Remove previous UI if any
        container.findViewById<ConstraintLayout>(R.id.camera_ui_container)?.let {
            container.removeView(it)
        }
        // Inflate a new view containing all UI for controlling the camera
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setLensFacing(lensFacing)
            .setFlashMode(FlashMode.ON)
            .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            .build()

        // Build the image capture use case and attach button click listener
        val imageCapture = ImageCapture(imageCaptureConfig)
        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

//        controls.findViewById<ImageButton>(R.id.camera_zoom_in_button).setOnClickListener {
//            val myZoom = Rect(left, top, right, bottom)
//            left = 50
//            top = 50
//            right = 50
//            bottom = 50
//            preview.zoom(myZoom)
//        }
//


//        controls.findViewById<ImageButton>(R.id.camera_zoom_out_button).setOnClickListener {
//            left = 0
//            top = 0
//            right = 0
//            bottom = 0
//            val myZoom = Rect(left, top, right, bottom)
//            preview.zoom(myZoom)
//        }


        controls.findViewById<ImageButton>(R.id.camera_gallery) .setOnClickListener {
            galleryIntent()
        }
        controls.findViewById<ImageButton>(R.id.camera_capture_button).setOnClickListener {
            val file = File(
                context?.externalMediaDirs?.first(),
                "${System.currentTimeMillis()}.jpg"
            )
            imageCapture.takePicture(file, executor,
                object : ImageCapture.OnImageSavedListener {
                    override fun onError(
                        imageCaptureError: ImageCapture.ImageCaptureError,
                        message: String,
                        exc: Throwable?
                    ) {
                        val msg = "Photo capture failed: $message"
                        Log.e("CameraXApp", msg, exc)
                        view_finder.post {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onImageSaved(file: File) {
                        val msg = "Photo capture succeeded: ${file.absolutePath}"
                        Log.d("CameraXApp", msg)
                        CropImage.activity(Uri.fromFile(file))
                            ?.start(context!!, this@UplaodImageFragment);
                        view_finder.post {
                            preview
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }


        // Setup image analysis pipeline that computes average pixel luminance
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            // In our analysis, we care more about the latest image than
            // analyzing *every* image
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
        }.build()

        // Build the image analysis use case and instantiate our analyzer
        val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
            setAnalyzer(executor, LuminosityAnalyzer())
        }

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(this, preview, imageCapture, analyzerUseCase)

    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = view_finder.width / 2f
        val centerY = view_finder.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (view_finder?.display?.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        view_finder.setTransform(matrix)
    }


//    private fun loginToFirebase() {
//        val email = getString(R.string.firebase_email)
//        val password = getString(R.string.firebase_password)
//
//        // Authenticate with Firebase and subscribe to updates
//
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(
//            email, password
//        ).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//            } else {
//                Toast.makeText(activity, "firebase auth failed", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }


    val REQUEST_IMAGE_CAPTURE = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            CropImage.activity(intent?.getData()!!)
                .start(context!!, this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(intent!=null){
                val result: CropImage.ActivityResult = CropImage.getActivityResult(intent);
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    val resultUri: Uri = result.uri;
                    mMainActivityViewModel?.capturedImage?.postValue(resultUri)
                    val fragment: MasterFragment = PhotoFragment()
                    addFragment(fragmentManager, fragment)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error: Exception = result.getError();
                }
            }
        }
    }


    val SELECT_FILE: Int = 1;
    private fun galleryIntent() {
        var intent: Intent = Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
}