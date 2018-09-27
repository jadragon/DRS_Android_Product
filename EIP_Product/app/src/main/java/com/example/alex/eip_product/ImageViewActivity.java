package com.example.alex.eip_product;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Component.SimpleRoundProgress;
import Utils.CommonUtil;
import db.ImagePojo;
import db.SQLiteDatabaseHandler;

public class ImageViewActivity extends AppCompatActivity {
    ImageView image;
    JSONObject json;
    Button update_button, delete_button;
    SQLiteDatabaseHandler db;
    TextView total_image;
    ArrayList<ImagePojo> arrayList;
    SimpleRoundProgress progress;
    int t = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        image = findViewById(R.id.image);
        total_image = findViewById(R.id.total_image);
        progress = findViewById(R.id.srp_stroke_0);
        //從SD卡取得圖片
        /*
        image.setImageURI(null);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.png");
        image.setImageURI(Uri.fromFile(file));
        */
        //從資料庫取得圖片
        if (ActivityCompat.checkSelfPermission(ImageViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(ImageViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            return;
        }


        db = new SQLiteDatabaseHandler(ImageViewActivity.this);
        arrayList = db.getPhotoImage();
        total_image.setText("目前圖片有:" + arrayList.size() + "張  未上傳");

        if (arrayList.size() > 0)
            image.setImageBitmap(BitmapFactory.decodeByteArray(arrayList.get(0).getImage(), 0, arrayList.get(0).getImage().length));


        update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(getSDPath() + "/Record");
                makeDir(file);
                String filetitle = "test";
                String fileName = file.toString() + "/" + filetitle + ".pdf";

                imgToPdf(BitmapFactory.decodeByteArray(arrayList.get(0).getImage(), 0, arrayList.get(0).getImage().length), fileName);
                /*
                if (CommonUtil.checkWIFI(ImageViewActivity.this)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            arrayList = db.getPhotoImage();
                            for (final ImagePojo data : arrayList) {
                                int number = new Random().nextInt(50);
                                try {
                                    json = new PhotoApi().insert_photo("H9Tv7tBs8Esr914/MB62Aw==", "RalpBDfrS2EPx6t8QzgpcA==", number + "", data.getImage());
                                    db.updatePhotoStatus(data.getId(), 1);
                                    t++;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progress.setProgress((int) (((float) t / arrayList.size()) * 100));
                                        }
                                    });
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            db.updatePhotoStatus(data.getId(), 0);
                                            Toast.makeText(ImageViewActivity.this, "上傳失敗", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayList = db.getPhotoImage();
                                    total_image.setText("目前圖片有:" + arrayList.size() + "張  未上傳");
                                }
                            });
                        }
                    }).start();
                } else {
                    toastCheckWIFI();
                }
                */
            }
        });


        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.resetInsepectTables();
            }
        });

    }

    private void toastCheckWIFI() {
        if (!CommonUtil.checkWIFI(ImageViewActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("請確認網路是否為連線狀態?");
            builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!CommonUtil.checkWIFI(ImageViewActivity.this)) {
                        toastCheckWIFI();
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    /*** 将图片转换成pdf文件*imgFilePath 需要被转换的img所存放的位置。 例如imgFilePath="D:\\projectPath\\55555.jpg";*pdfFilePath 转换后的pdf所存放的位置 例如pdfFilePath="D:\\projectPath\\test.pdf";* @param image* @return* @throws IOException */
    public boolean imgToPdf(Bitmap bitmap, String fileName) {
        Document document = new Document();
        FileOutputStream fos = null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        try {
            fos = new FileOutputStream(fileName);
            PdfWriter.getInstance(document, fos);
            // 添加PDF文档的某些信息，比如作者，主题等等
            //document.addAuthor("www");
            // document.addSubject("www");
            // 设置文档的大小
            document.setPageSize(PageSize.A4);
            // 打开文档
            document.open();// 写入一段文字
            //      document.add(new Paragraph("JUST TEST ..."));
            // 读取一个图片
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //if you would have a chapter indentation
            //   int indentation = 0;
            //   float scaler = ((document.getPageSize().getWidth() - document.leftMargin()  - document.rightMargin() - indentation) / img.getWidth()) * 100;


            //img.scalePercent(scaler);
            if (width > PageSize.A4.getWidth() || height > PageSize.A4.getHeight()) {
                if (height > width) {
                    float x = (PageSize.A4.getWidth() - width * PageSize.A4.getHeight() / height) / 2;
                    img.setAbsolutePosition(x, 0);
                } else {
                    float y = (PageSize.A4.getHeight() - height * PageSize.A4.getWidth() / width) / 2;
                    img.setAbsolutePosition(0, y);
                }
            } else {
                img.setAlignment(Image.ALIGN_CENTER);
            }
            img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            img.setOriginalType(Image.ORIGINAL_PNG);
            //设置图片的绝对位置
            // image.setAbsolutePosition(0, 0);
            // image.scaleAbsolute(500, 400);
            // 插入一个图片
            document.add(img);
            document.close();
            fos.flush();
            fos.close();
            Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
        } catch (DocumentException de) {
            Log.e("PDF", de.getMessage());
        } catch (IOException ioe) {
            Log.e("PDF", ioe.getMessage());
        }

        return true;

    }

}
