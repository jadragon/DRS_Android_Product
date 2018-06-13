package com.test.tw.wrokproduct.我的帳戶.個人管理.個人資料;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.test.tw.wrokproduct.GlobalVariable;
import com.test.tw.wrokproduct.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import library.Http.PostByteArrayformImage;
import library.SQLiteDatabaseHandler;
import library.component.ToastMessageDialog;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView1;
    Button back, albums, button1, change_camera, confirm, cancel;
    CircleImageView imageView1;
    Camera camera;
    LinearLayout show_layout;
    Bitmap bitmap;
    int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    DisplayMetrics dm;
    Camera.Parameters parameters;
    String token;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
        if (ContextCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this,
                    Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(CameraActivity.this)
                        .setMessage("我真的沒有要做壞事, 給我權限吧?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CameraActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            } else {

                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            token = ((GlobalVariable) getApplicationContext()).getToken();
            dm = getResources().getDisplayMetrics();
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
            surfaceView1 = findViewById(R.id.surfaceView1);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            imageView1 = findViewById(R.id.imageView1);
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
        show_layout = findViewById(R.id.show_layout);
        show_layout.setVisibility(View.INVISIBLE);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                        final byte[] bitmapByte = baos.toByteArray();
                        final JSONObject aa = new PostByteArrayformImage().getJSONFromUrl("http://api.gok1945.com/main/mcenter/person/updatePortrait.php", token, bitmapByte);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if(aa.getBoolean("Success")) {
                                        SQLiteDatabaseHandler sql = new SQLiteDatabaseHandler(getApplicationContext());
                                        sql.updatePhotoImage(bitmapByte);
                                        sql.close();
                                        Intent intent = new Intent();
                                        intent.putExtra("picture", bitmapByte);
                                        setResult(100, intent);
                                        finish();
                                    }else {
                                        new ToastMessageDialog(CameraActivity.this,aa.getString("Message")).confirm();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();

            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            Log.e("bitmap", width + "\n" + height);
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
        Log.e("tag", " surfaceChanged");
        parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        Log.e("tag",
                "parameters.getPictureSize()"
                        + parameters.getPictureSize().width);
        setPictureSize(parameters);
        Log.i("tag", "holder width:" + width + "  height:" + height);
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
            if (width > heigh)
                params = new FrameLayout.LayoutParams(dm.widthPixels, dm.widthPixels * width / heigh);

            else
                params = new FrameLayout.LayoutParams(dm.widthPixels, dm.widthPixels * heigh / width);
            params.gravity= Gravity.CENTER;
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

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
                Log.e("bitmap", width + "\n" + height);
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

}
