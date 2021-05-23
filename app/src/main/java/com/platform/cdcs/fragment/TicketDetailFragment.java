package com.platform.cdcs.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ImgAdapter;
import com.platform.cdcs.tool.FileUtil;
import com.platform.cdcs.tool.FragmentUtil;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.widget.TwSheetBuilder;

import java.io.File;

/**
 * Created by holytang on 2017/9/24.
 */
public class TicketDetailFragment extends BaseFragment {

    public static final int REQUEST_CODE_ALBUM = 10001;
    public static final int REQUEST_CODE_TAKE_PHOTO = 10002;
    public static final int REQUEST_CODE_CROUP_PHOTO = 10003;
    private ImgAdapter adapter;
    private Uri uri;
    private File file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ImgAdapter(getContext());
        ChooseItem item = new ChooseItem();
        item.setImageurl("http://files.jb51.net/file_images/article/201703/2017316101827092.jpg?2017216101842");
        adapter.addItem(item);
        item = new ChooseItem();
        item.setImageurl("http://files.jb51.net/file_images/article/201703/2017316101827092.jpg?2017216101842");
        adapter.addItem(item);
        item = new ChooseItem();
        item.setImageurl("http://files.jb51.net/file_images/article/201703/2017316101827092.jpg?2017216101842");
        adapter.addItem(item);

        item = new ChooseItem();
        item.setType(1);
        adapter.addItem(item);
        init();
    }

    private void init() {
        file = new File(FileUtil.tempPicPath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //TODO 通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            uri = FileProvider.getUriForFile(getContext(), "com.yf.useravatar", file);
        }
    }

    @Override
    public void initView(View view) {
        getToolBar().setNavigationIcon(R.mipmap.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        setTitle("发票详情");
        GridView gridView = (GridView) view.findViewById(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == adapter.getCount() - 1) {
                    //TODO 新增图片
                    new TwSheetBuilder(getContext()).bindSheets(new int[]{R.mipmap.icon_camera, R.mipmap.icon_photo}, new String[]{"拍摄", "相册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                if (PermissionsUtil.hasCameraPermission(getActivity())) {
                                    takeCamera();
                                }
                            } else {
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
                            }
                        }
                    }).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", i);
                    bundle.putStringArrayList("list", adapter.getImgList());
                    FragmentUtil.navigateToInNewActivity(getActivity(), ImagePagerFragment.class, bundle);
                }
            }
        });
        gridView.setAdapter(adapter);
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketRelationFragment.class, null);
            }
        });
    }

    @Override
    public int layoutId() {
        return R.layout.ticket_detail;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            Uri newUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                newUri = Uri.parse("file:///" + FileUtil.getPath(getContext(), data.getData()));
            } else {
                newUri = data.getData();
            }
            if (newUri != null) {
                startPhotoZoom(newUri);
            } else {
                Toast.makeText(getContext(), "没有得到相册图片", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            compressImg();
        }
    }

    private void compressImg() {
        File cover = FileUtil.getSmallBitmap(getContext(), file.getPath());

    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
//        intent.putExtra("outputX", 400);//图片输出大小
//        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case PermissionsUtil.PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    takeCamera();
                } else {
                    Toast.makeText(getContext(), "未获取摄像头权限", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
