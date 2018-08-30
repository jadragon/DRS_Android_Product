package com.example.alex.posdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.alex.posdemo.GlobalVariable.UserInfo;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Utils.AsyncTaskUtils;
import Utils.IDataCallBack;
import library.AnalyzeJSON.AnalyzeUtil;
import library.Component.ToastMessageDialog;
import library.JsonApi.PhotoApi;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView1;
    Button back, albums, button1, change_camera, confirm, cancel;
    ImageView imageView1;
    Camera camera;
    LinearLayout show_layout;
    Bitmap bitmap;
    int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    DisplayMetrics dm;
    Camera.Parameters parameters;
    UserInfo userInfo;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    String a_no;
    EditText camera_photo_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        a_no = getIntent().getStringExtra("a_no");
        dm = getResources().getDisplayMetrics();
        userInfo = (UserInfo) getApplicationContext();
        imageView1 = findViewById(R.id.imageView2);
        imageView1.setVisibility(View.VISIBLE);

        button1 = findViewById(R.id.button_capture);
        albums = findViewById(R.id.albums);
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (ContextCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this,
                    Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this)
                        .setMessage("此功能需要權限，請同意權限後再進行下一步")
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CameraActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {

            surfaceView1 = findViewById(R.id.surfaceView1);
            //Orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            initShow();
            initChangeCamera();
            surfaceHolder = surfaceView1.getHolder();
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            surfaceHolder.addCallback(this);
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //自動對焦
                    // camera.autoFocus(afcb);
                    imageView1.setImageBitmap(null);
                    show_layout.setVisibility(View.VISIBLE);
                    camera.takePicture(null, null, jpeg);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        finish();
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void initChangeCamera() {
        change_camera = findViewById(R.id.change_camera);

        change_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopPreview();
                //NB: if you don't release the current camera before switching, you app will crash
                camera.release();
                //swap the id of the camera to be used
                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                camera = Camera.open(currentCameraId);
                try {
                    //設置參數
                    camera.setPreviewDisplay(surfaceHolder);
                    int rotation = getOrientation(getApplicationContext());
                    camera.setDisplayOrientation(rotation);
                    setPictureSize(parameters);
                    //鏡頭的方向和手機相差90度，所以要轉向
                    //攝影頭畫面顯示在Surface上
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }
        });
    }

    private void initShow() {
        camera_photo_name = findViewById(R.id.camera_photo_name);
        show_layout = findViewById(R.id.show_layout);
        show_layout.setVisibility(View.INVISIBLE);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] bitmapByte = baos.toByteArray();
                       Intent intent = new Intent();
                intent.putExtra("picture", bitmapByte);
                setResult(100, intent);
                */
                //finish
                if (bitmap != null && !camera_photo_name.getText().toString().equals("")) {
                    AsyncTaskUtils.doAsync(new IDataCallBack<JSONObject>() {
                        @Override
                        public void onTaskBefore() {

                        }

                        @Override
                        public JSONObject onTasking(Void... params) {
                            return new PhotoApi().insert_photo(a_no, userInfo.getDu_no(), camera_photo_name.getText().toString(), bitmap);
                        }

                        @Override
                        public void onTaskAfter(JSONObject jsonObject) {
                            if (AnalyzeUtil.checkSuccess(jsonObject)) {
                                finish();
                            } else {
                                new ToastMessageDialog(CameraActivity.this, ToastMessageDialog.TYPE_INFO).confirm(AnalyzeUtil.getMessage(jsonObject));
                            }
                        }
                    });

                }
            }
        });

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null)
                    bitmap.recycle();
                show_layout.setVisibility(View.INVISIBLE);
            }
        });
    }

    Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                bitmap = rotateImage(bitmap, 90);
            } else {
                bitmap = rotateImage(bitmap, 270);
            }
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > height) {
                bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
            } else if (width < height) {
                bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
            }
            //byte數组轉換成Bitmap
            imageView1.setImageBitmap(null);
            imageView1.setImageBitmap(bitmap);
            //拍下圖片顯示在下面的ImageView裡

            FileOutputStream fop;
            try {
                fop = new FileOutputStream("/sdcard/dd.jpg");
                //實例化FileOutputStream，參數是生成路徑
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fop);
                //壓缩bitmap寫進outputStream 參數：輸出格式  輸出質量  目標OutputStream
                //格式可以為jpg,png,jpg不能存儲透明
                fop.close();
                System.out.println("拍照成功");
                //關閉流
            } catch (FileNotFoundException e) {

                e.printStackTrace();
                System.out.println("FileNotFoundException");

            } catch (IOException e) {

                e.printStackTrace();
                System.out.println("IOException");
            }
            camera.startPreview();
            //需要手動重新startPreview，否則停在拍下的瞬間

        }

    };

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        setPictureSize(parameters);
        // parameters.setPreviewSize(width, height);//需要判断支持的预览
        camera.setParameters(parameters);
        camera.startPreview();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            //設置參數
            camera.setPreviewDisplay(surfaceHolder);
            //鏡頭的方向和手機相差90度，所以要轉向
            int rotation = getOrientation(getApplicationContext());
            camera.setDisplayOrientation(rotation);
            //攝影頭畫面顯示在Surface上
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surfaceDestroyed");
        if (camera != null) {
            camera.stopPreview();
            //關閉預覽
            camera.release();
        }

    }

    private void setPictureSize(Camera.Parameters parameters) {
        List<Camera.Size> list = parameters.getSupportedPictureSizes();
        if (list.size() > 2) {
            /*
            Camera.Size size = list.get(list.size() / 2);
            parameters.setPictureSize(size.width, size.height);
            */
            int width = 0;
            int heigh = 0;
            for (Camera.Size size : list) {
                if (size.width < 1500 && size.width > 1000) {
                    width = size.width;
                    heigh = size.height;
                    break;
                }
            }
            parameters.setPictureSize(width, heigh);
            parameters.setPreviewSize(width, heigh);
            FrameLayout.LayoutParams params;
            if (width > heigh) {
                params = new FrameLayout.LayoutParams(dm.widthPixels, dm.widthPixels * (heigh / width));

            } else {
                params = new FrameLayout.LayoutParams((int) (dm.heightPixels * (width / (float) heigh)), dm.heightPixels);
            }
            params.gravity = Gravity.CENTER;
            surfaceView1.setLayoutParams(params);
        } else {
            Camera.Size size = list.get(0);
            parameters.setPictureSize(size.width, size.height);
            parameters.setPreviewSize(size.width, size.height);
        }
// 设置相机参数
        parameters.setFocusMode(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        camera.setParameters(parameters);
    }

    //取得display rotation
    public static int getOrientation(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int orientation;
        boolean expectPortrait;
        switch (rotation) {
            case Surface.ROTATION_0:
            default:
                orientation = 90;
                expectPortrait = true;
                break;
            case Surface.ROTATION_90:
                orientation = 0;
                expectPortrait = false;
                break;
            case Surface.ROTATION_180:
                orientation = 270;
                expectPortrait = true;
                break;
            case Surface.ROTATION_270:
                orientation = 180;
                expectPortrait = false;
                break;
        }
        boolean isPortrait = display.getHeight() > display.getWidth();
        if (isPortrait != expectPortrait) {
            orientation = (orientation + 270) % 360;
        }
        return orientation;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri); //顯得到bitmap圖片
                show_layout.setVisibility(View.VISIBLE);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > height) {
                    bitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
                } else if (width < height) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
                }
                imageView1.setImageBitmap(null);
                imageView1.setImageBitmap(bitmap);

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
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }
}
