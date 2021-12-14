/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.datacapture.matrixscansimplesample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.scandit.datacapture.barcode.data.Symbology;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTracking;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingListener;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingSession;
import com.scandit.datacapture.barcode.tracking.capture.BarcodeTrackingSettings;
import com.scandit.datacapture.barcode.tracking.data.TrackedBarcode;
import com.scandit.datacapture.barcode.tracking.ui.overlay.BarcodeTrackingAdvancedOverlay;
import com.scandit.datacapture.barcode.tracking.ui.overlay.BarcodeTrackingBasicOverlay;
import com.scandit.datacapture.barcode.tracking.ui.overlay.BarcodeTrackingBasicOverlayStyle;
import com.scandit.datacapture.core.capture.DataCaptureContext;
import com.scandit.datacapture.core.common.geometry.Anchor;
import com.scandit.datacapture.core.common.geometry.FloatWithUnit;
import com.scandit.datacapture.core.common.geometry.MeasureUnit;
import com.scandit.datacapture.core.common.geometry.PointWithUnit;
import com.scandit.datacapture.core.data.FrameData;
import com.scandit.datacapture.core.source.Camera;
import com.scandit.datacapture.core.source.CameraSettings;
import com.scandit.datacapture.core.source.FrameSourceState;
import com.scandit.datacapture.core.source.VideoResolution;
import com.scandit.datacapture.core.ui.DataCaptureView;
import com.scandit.datacapture.matrixscansimplesample.data.ScanResult;

import java.util.HashMap;
import java.util.HashSet;

public class MatrixScanActivity extends CameraPermissionActivity
        implements BarcodeTrackingListener {

    // Enter your Scandit License key here.
    // Your Scandit License key is available via your Scandit SDK web account.
    public static final String SCANDIT_LICENSE_KEY = "AXNAcQNpItGsKkzh2zlwJ+E98UiqHPrNajty+qZtRtfVUg3TxkWUwmp+OAYWG8g8zFsigo8gP/ELDiMmUEVYaixuWZ4fexibNgXp2dAttcY0b75kYEwZvht6/h6zYMnrvUtacqRC9f5JWSQo1UesNa5y6jPedHvzmXAhUtJesS5PTFy9NHMncGNQkeDDdwHwqUE+ltxFo84aPztuXULRGgNMDvlpYl3o9kYxoQ9JjayCUN7k6Fgs0Wx9vEepQIyDhmT41ygovR5WXWovoGQSKpB4F9RzfTyVJ3uY1mYnwc7pUbO2uVrE7QVXG3d6Q4r9Y2+BkxxQfs/WJxq/105K3XRuwle0XE85SGJAb8BEh8tYZ7j/J0PZ/cpLe9qbc0BleU4jl49og41TCL4o8nymWRZ8wU5BafiojmqoIQVrVmjZeby8IGBK8oFRFc3RSbyrMHwmsnpXwW5bFCJ9u3tNdx5ssjf/SuKkzlV6EtNeadFofTUk2E5MIAJPUpLgeuXhF0YX7hE3QehkQbWbyj8KIFI+5YdSr46XyaOGkaBH91uL9eDwYzpqQKf1R+iH1gy4354QmsSj1dmJ3P7QMdQ0gZeuaUgGVTo3QnJUCc/IGfjAW9WrppZfZxhU5ouN/FGXSq+IDWDbKUeUD2Ru1V5pxKe75+dPtdYvrFtiMQI9zIby9l6hOOZf5xuotv64QjvEZ2TnfhFHeO2vEqtDGX2OsSwvoOWgFgrGmohnfraImf40Sj54PylIwhfRIlbKMcVaTJliztJrHEj4tRTBGvvR1HGzve97/6Z8m3D4HARUH8kYzyK1XX6d0cORe52AZNQojSSgnCemMbf7zWnAN/gpuOKVGzV8n80O1XPFQoPBI4c00jtN0HsofskJSLKykg7HfC89lZMVHJl+chUXw4mOdHPRbIiEvqtu2rOvLdsE7/B7tAwUeBNpQWau8kf9C7xmYCYpYodek5DTWTmtp1W2DBWTvGmsd4PRXuPOaOdgFe9rMxOrwSLBefkhOgDl9fyp7kZPDM9KdD7N04ViDemwq7p+u1fIVY1WMIDSUPJySHoRHCnsz/5L7mxgsENtITdkvIemjUkamc6FTG4K4SKFw6vQo+7do57zzTzsl0GIODI9J3rDxZndTNVffj6D1TsH2QrMjbTmYOzRBxPe9EG8MvOte+3v6dooZdRnN8o/1XcuyOegFtPL/A/OPDVVhRTk";

    public static final int REQUEST_CODE_SCAN_RESULTS = 1;

    private Camera camera;
    private BarcodeTracking barcodeTracking;
    private DataCaptureContext dataCaptureContext;
    private BarcodeTrackingAdvancedOverlay overlay;


    private final HashSet<ScanResult> scanResults = new HashSet<>();
    private final HashMap<String, Integer> scanResultsMap = new HashMap<String, Integer>();

    private int tagsLength = 6;
    private int assignedMax = -1;
    private String[] tags = { "Next delivery", "Delayed", "Return","Reschedule Requested", "Early", "Ontime"};
    private String[] colors = {"WHITE", "RED", "YELLOW", "GRAY", "GREEN", "GREEN"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_scan);

        // Initialize and start the barcode recognition.
        initialize();

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (scanResults) {
                    // Show new screen displaying a list of all barcodes that have been scanned.
                    Intent intent = ResultsActivity.getIntent(MatrixScanActivity.this, scanResults);
                    startActivityForResult(intent, REQUEST_CODE_SCAN_RESULTS);
                }
            }
        });
    }

    private void initialize() {
        // Create data capture context using your license key.
        dataCaptureContext = DataCaptureContext.forLicenseKey(SCANDIT_LICENSE_KEY);

        // Use the recommended camera settings for the BarcodeTracking mode.
        CameraSettings cameraSettings = BarcodeTracking.createRecommendedCameraSettings();
        // Adjust camera settings - set Full HD resolution.
        cameraSettings.setPreferredResolution(VideoResolution.FULL_HD);
        // Use the default camera and set it as the frame source of the context.
        // The camera is off by default and must be turned on to start streaming frames to the data
        // capture context for recognition.
        // See resumeFrameSource and pauseFrameSource below.
        camera = Camera.getDefaultCamera(cameraSettings);
        if (camera != null) {
            dataCaptureContext.setFrameSource(camera);
        } else {
            throw new IllegalStateException("Sample depends on a camera, which failed to initialize.");
        }

        // The barcode tracking process is configured through barcode tracking settings
        // which are then applied to the barcode tracking instance that manages barcode tracking.
        BarcodeTrackingSettings barcodeTrackingSettings = new BarcodeTrackingSettings();

        // The settings instance initially has all types of barcodes (symbologies) disabled.
        // For the purpose of this sample we enable a very generous set of symbologies.
        // In your own app ensure that you only enable the symbologies that your app requires
        // as every additional enabled symbology has an impact on processing times.
        HashSet<Symbology> symbologies = new HashSet<>();
        symbologies.add(Symbology.EAN13_UPCA);
        symbologies.add(Symbology.EAN8);
        symbologies.add(Symbology.UPCE);
        symbologies.add(Symbology.CODE39);
        symbologies.add(Symbology.CODE128);

        barcodeTrackingSettings.enableSymbologies(symbologies);

        // Create barcode tracking and attach to context.
        barcodeTracking =
                BarcodeTracking.forDataCaptureContext(dataCaptureContext, barcodeTrackingSettings);

        // Register self as a listener to get informed of tracked barcodes.
        barcodeTracking.addListener(this);

        // To visualize the on-going barcode tracking process on screen, setup a data capture view
        // that renders the camera preview. The view must be connected to the data capture context.
        DataCaptureView dataCaptureView = DataCaptureView.newInstance(this, dataCaptureContext);

        // Add a barcode tracking overlay to the data capture view to render the tracked barcodes on
        // top of the video preview. This is optional, but recommended for better visual feedback.
        BarcodeTrackingBasicOverlay.newInstance(
                barcodeTracking,
                dataCaptureView,
                BarcodeTrackingBasicOverlayStyle.FRAME
        );


        overlay = BarcodeTrackingAdvancedOverlay.newInstance(barcodeTracking, dataCaptureView);

        // Add the DataCaptureView to the container.
        FrameLayout container = findViewById(R.id.data_capture_view_container);
        container.addView(dataCaptureView);
    }

    @Override
    protected void onPause() {
        pauseFrameSource();
        super.onPause();
    }

    private void pauseFrameSource() {
        // Switch camera off to stop streaming frames.
        // The camera is stopped asynchronously and will take some time to completely turn off.
        // Until it is completely stopped, it is still possible to receive further results, hence
        // it's a good idea to first disable barcode tracking as well.
        barcodeTracking.setEnabled(false);
        camera.switchToDesiredState(FrameSourceState.OFF, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        resumeFrameSource();
    }

    private void resumeFrameSource() {
        // Switch camera on to start streaming frames.
        // The camera is started asynchronously and will take some time to completely turn on.
        barcodeTracking.setEnabled(true);
        camera.switchToDesiredState(FrameSourceState.ON, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN_RESULTS
                && resultCode == ResultsActivity.RESULT_CODE_CLEAN) {
            synchronized (scanResults) {
                scanResults.clear();
                scanResults.clear();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onObservationStarted(@NonNull BarcodeTracking barcodeTracking) {
        // Nothing to do.
    }

    @Override
    public void onObservationStopped(@NonNull BarcodeTracking barcodeTracking) {
        // Nothing to do.
    }

    // This function is called whenever objects are updated and it's the right place to react to
    // the tracking results.
    @Override
    public void onSessionUpdated(
            @NonNull BarcodeTracking mode,
            @NonNull BarcodeTrackingSession session,
            @NonNull FrameData data
    ) {


        Toast.makeText(getApplicationContext(), "Scanned: ", Toast.LENGTH_SHORT).show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (TrackedBarcode trackedBarcode : session.getAddedTrackedBarcodes()) {

                    TextView textView = new TextView(getApplicationContext());
                    String trackedCode =trackedBarcode.getBarcode().getData();
                    String color;
                    String tag;
                    synchronized(scanResultsMap){
                    if(scanResultsMap.containsKey(trackedCode)){
                       color = colors[scanResultsMap.get(trackedCode)];
                       tag = tags[scanResultsMap.get(trackedCode)];
                    }else{
                        assignedMax = assignedMax+1 % (tagsLength-1);
                        scanResultsMap.put(trackedCode, assignedMax);
                        color = colors[scanResultsMap.get(trackedCode)];
                        tag = tags[scanResultsMap.get(trackedCode)];
                        }
                    }

                    textView.setBackgroundColor(Color.parseColor(color));
                    textView.setLayoutParams(
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    );

                    textView.setText(trackedCode+ "<=>"+ tag);
                    overlay.setViewForTrackedBarcode(trackedBarcode, textView);
                    overlay.setAnchorForTrackedBarcode(
                            trackedBarcode, Anchor.TOP_CENTER
                    );
                    overlay.setOffsetForTrackedBarcode(
                            trackedBarcode,
                            new PointWithUnit(
                                    new FloatWithUnit(0f, MeasureUnit.FRACTION),
                                    new FloatWithUnit(-1f, MeasureUnit.FRACTION)
                            )
                    );
                }
            }
        });
        synchronized (scanResults) {
            for (TrackedBarcode trackedBarcode : session.getAddedTrackedBarcodes()) {
                scanResults.add(new ScanResult(trackedBarcode.getBarcode()));
            }
        }
    }



    @Override
    protected void onDestroy() {
        dataCaptureContext.removeMode(barcodeTracking);
        super.onDestroy();
    }
}
