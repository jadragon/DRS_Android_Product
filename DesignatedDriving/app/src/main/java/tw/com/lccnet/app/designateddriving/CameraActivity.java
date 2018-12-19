package tw.com.lccnet.app.designateddriving;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tw.com.lccnet.app.designateddriving.Component.CameraSurfaceView;
import tw.com.lccnet.app.designateddriving.Utils.CameraUtils;
import tw.com.lccnet.app.designateddriving.Utils.ImageUtils;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private CameraSurfaceView mCameraSurfaceView;
    private Button back, albums, button_capture, change_camera, confirm, cancel;
    private ImageView prewview_image;
    private LinearLayout show_layout;
    private Bitmap mbitmap;
    private int mOrientation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initListener();
    }


    /**
     * 初始化View
     */
    private void initView() {
        prewview_image = findViewById(R.id.prewview_image);
        prewview_image.setVisibility(View.VISIBLE);
        button_capture = findViewById(R.id.button_capture);
        change_camera = findViewById(R.id.change_camera);
        albums = findViewById(R.id.albums);
        back = findViewById(R.id.back);
        mCameraSurfaceView = findViewById(R.id.cameraSurfaceView);
        mOrientation = CameraUtils.calculateCameraPreviewOrientation(CameraActivity.this);
        //showview
        show_layout = findViewById(R.id.show_layout);
        show_layout.setVisibility(View.INVISIBLE);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);
    }

    private void initListener() {
        button_capture.setOnClickListener(this);
        change_camera.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        albums.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_capture:
                prewview_image.setImageBitmap(null);
                show_layout.setVisibility(View.VISIBLE);
                takePicture();
                break;

            case R.id.change_camera:
                switchCamera();
                break;
            case R.id.confirm:
                if (mbitmap != null) {
                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                            +  "picture.png";
                    try {
                        FileOutputStream fout = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        mbitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bos.flush();
                        bos.close();
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
                break;
            case R.id.cancel:
                if (mbitmap != null)
                    mbitmap.recycle();
                show_layout.setVisibility(View.INVISIBLE);
                CameraUtils.startPreview();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.albums:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
                break;
        }
    }


    /**
     * 拍照
     */
    private void takePicture() {
        CameraUtils.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                CameraUtils.startPreview();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bitmap != null) {
                    show_layout.setVisibility(View.VISIBLE);
                    if (CameraUtils.getCameraID() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        mbitmap = ImageUtils.getFlipBitmap(bitmap, mOrientation + 180);
                        //ImageUtils.getFlipBitmap(bitmap);
                    } else {
                        mbitmap = ImageUtils.getRotatedBitmap(bitmap, mOrientation);
                    }
                    prewview_image.setImageBitmap(mbitmap);
                    /*
                    String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/"
                            + System.currentTimeMillis() + ".jpg";
                    try {
                        FileOutputStream fout = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */
                    CameraUtils.stopPreview();
                }

            }
        });
    }


    /**
     * 切换相机
     */
    private void switchCamera() {
        if (mCameraSurfaceView != null) {
            CameraUtils.switchCamera(1 - CameraUtils.getCameraID(), mCameraSurfaceView.getHolder());
            // 切换相机后需要重新计算旋转角度
            mOrientation = CameraUtils.calculateCameraPreviewOrientation(CameraActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) { //此處的 RESULT_OK 是系統自定義得一個常量
            return;
        }
//外界的程式訪問ContentProvider所提供數據 可以通過ContentResolver介面
        ContentResolver resolver = getContentResolver();
//此處的用於判斷接收的Activity是不是你想要的那個
        if (requestCode == 200) {
            try {
                Uri originalUri = data.getData(); //獲得圖片的uri
                mbitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri); //顯得到bitmap圖片
                show_layout.setVisibility(View.VISIBLE);
                int width = mbitmap.getWidth();
                int height = mbitmap.getHeight();
                if (width > height) {
                    mbitmap = Bitmap.createBitmap(mbitmap, (width - height) / 2, 0, height, height);
                } else if (width < height) {
                    mbitmap = Bitmap.createBitmap(mbitmap, 0, (height - width) / 2, width, width);
                }
                prewview_image.setImageBitmap(null);
                prewview_image.setImageBitmap(mbitmap);

                /*
                // 這裏開始的第二部分，獲取圖片的路徑：
                String[] proj = {MediaStore.Images.Media.DATA};
//好像是android多媒體數據庫的封裝介面，具體的看Android文檔
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
//按我個人理解 這個是獲得用戶選擇的圖片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//將光標移至開頭 ，這個很重要，不小心很容易引起越界
                cursor.moveToFirst();
//最後根據索引值獲取圖片路徑
                String path = cursor.getString(column_index);
                */
            } catch (IOException e) {
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        CameraUtils.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraUtils.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mbitmap != null) {
            mbitmap.recycle();
        }
        CameraUtils.releaseCamera();
    }


}
