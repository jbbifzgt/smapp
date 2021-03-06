package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.platform.cdcs.R;

public abstract class QRCodeView extends RelativeLayout implements Camera.PreviewCallback, ProcessDataTask.Delegate {
    protected Camera mCamera;
    protected CameraPreview mPreview;
    protected ScanBoxView mScanBoxView;
    protected Delegate mDelegate;
    protected Handler mHandler;
    protected boolean mSpotAble = false;
    protected ProcessDataTask mProcessDataTask;
    private int mOrientation;
    private Runnable mOneShotPreviewCallbackTask = new Runnable() {
        @Override
        public void run() {
            if (mCamera != null && mSpotAble) {
                try {
                    mCamera.setOneShotPreviewCallback(QRCodeView.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public QRCodeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mPreview = new CameraPreview(getContext());

        mScanBoxView = new ScanBoxView(getContext());
        mScanBoxView.initCustomAttrs(context, attrs);
        mPreview.setId(R.id.bgaqrcode_camera_preview);
        addView(mPreview);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(context, attrs);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, mPreview.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mPreview.getId());
        addView(mScanBoxView, layoutParams);

        mOrientation = BGAQRCodeUtil.getOrientation(context);
    }

    /**
     * ??????????????????????????????
     *
     * @param delegate ????????????????????????
     */
    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public ScanBoxView getScanBoxView() {
        return mScanBoxView;
    }

    /**
     * ???????????????
     */
    public void showScanRect() {
        if (mScanBoxView != null) {
            mScanBoxView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ???????????????
     */
    public void hiddenScanRect() {
        if (mScanBoxView != null) {
            mScanBoxView.setVisibility(View.GONE);
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    public void startCamera() {
        startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param cameraFacing
     */
    public void startCamera(int cameraFacing) {
        if (mCamera != null) {
            return;
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == cameraFacing) {
                startCameraById(cameraId);
                break;
            }
        }
    }

    private void startCameraById(int cameraId) {
        try {
            mCamera = Camera.open(cameraId);
            mPreview.setCamera(mCamera);
        } catch (Exception e) {
            if (mDelegate != null) {
                mDelegate.onScanQRCodeOpenCameraError();
            }
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    public void stopCamera() {
        try {
            stopSpotAndHiddenRect();
            if (mCamera != null) {
                mPreview.stopCameraPreview();
                mPreview.setCamera(null);
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * ??????1.5??????????????????
     */
    public void startSpot() {
        startSpotDelay(1500);
    }

    /**
     * ??????delay?????????????????????
     *
     * @param delay
     */
    public void startSpotDelay(int delay) {
        mSpotAble = true;

        startCamera();
        // ?????????????????????????????????
        mHandler.removeCallbacks(mOneShotPreviewCallbackTask);
        mHandler.postDelayed(mOneShotPreviewCallbackTask, delay);
    }

    /**
     * ????????????
     */
    public void stopSpot() {
        cancelProcessDataTask();

        mSpotAble = false;

        if (mCamera != null) {
            try {
                mCamera.setOneShotPreviewCallback(null);
            } catch (Exception e) {
            }
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mOneShotPreviewCallbackTask);
        }
    }

    /**
     * ????????????????????????????????????
     */
    public void stopSpotAndHiddenRect() {
        stopSpot();
        hiddenScanRect();
    }

    /**
     * ??????????????????????????????1.5??????????????????
     */
    public void startSpotAndShowRect() {
        startSpot();
        showScanRect();
    }

    /**
     * ???????????????
     */
    public void openFlashlight() {
        mPreview.openFlashlight();
    }

    /**
     * ???????????????
     */
    public void closeFlashlight() {
        mPreview.closeFlashlight();
    }

    /**
     * ???????????????????????????
     */
    public void onDestroy() {
        stopCamera();
        mHandler = null;
        mDelegate = null;
        mOneShotPreviewCallbackTask = null;
    }

    /**
     * ????????????????????????
     */
    protected void cancelProcessDataTask() {
        if (mProcessDataTask != null) {
            mProcessDataTask.cancelTask();
            mProcessDataTask = null;
        }
    }

    /**
     * ???????????????????????????
     */
    public void changeToScanBarcodeStyle() {
        if (!mScanBoxView.getIsBarcode()) {
            mScanBoxView.setIsBarcode(true);
        }
    }

    /**
     * ??????????????????????????????
     */
    public void changeToScanQRCodeStyle() {
        if (mScanBoxView.getIsBarcode()) {
            mScanBoxView.setIsBarcode(false);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    public boolean getIsScanBarcodeStyle() {
        return mScanBoxView.getIsBarcode();
    }

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        if (mSpotAble) {
            cancelProcessDataTask();
            mProcessDataTask = new ProcessDataTask(camera, data, this, mOrientation) {
                @Override
                protected void onPostExecute(String result) {
                    if (mSpotAble) {
                        if (mDelegate != null && !TextUtils.isEmpty(result)) {
                            try {
                                mDelegate.onScanQRCodeSuccess(result);
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                camera.setOneShotPreviewCallback(QRCodeView.this);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }.perform();
        }
    }

    public interface Delegate {
        /**
         * ??????????????????
         *
         * @param result
         */
        void onScanQRCodeSuccess(String result);

        /**
         * ????????????????????????
         */
        void onScanQRCodeOpenCameraError();
    }
}