package com.platform.cdcs.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.cdcs.MyApp;
import com.platform.cdcs.R;
import com.platform.cdcs.adapter.ImgAdapter;
import com.platform.cdcs.model.BaseObjResponse;
import com.platform.cdcs.model.InvoiceList;
import com.platform.cdcs.model.MockObj;
import com.platform.cdcs.model.PicModel;
import com.platform.cdcs.tool.Constant;
import com.platform.cdcs.tool.FileUtil;
import com.platform.cdcs.tool.FragmentUtil;
import com.platform.cdcs.tool.ViewTool;
import com.trueway.app.uilib.fragment.BaseFragment;
import com.trueway.app.uilib.imgpick.AndroidImagePicker;
import com.trueway.app.uilib.imgpick.ImageSet;
import com.trueway.app.uilib.imgpick.ImagesGridActivity;
import com.trueway.app.uilib.model.ChooseItem;
import com.trueway.app.uilib.model.ImageItem;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.tool.Utils;
import com.trueway.app.uilib.widget.TwDialogBuilder;
import com.trueway.app.uilib.widget.TwSheetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.Call;

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
    private LinearLayout rootView;
    private String invoiceId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ImgAdapter(getContext());
        invoiceId = getArguments().getString("id");
        init();
    }

    private void init() {
        file = new File(FileUtil.tempPicPath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //TODO 通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            uri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName(), file);
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
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i != adapter.getCount() - 1) {
                    new TwDialogBuilder(getContext()).setTitle(R.string.attention).setMessage("是否删除该图片？").setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            deleteImg(i);
                        }
                    }).setPositiveButton(R.string.cancel_text, null).create().show();
                }
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == adapter.getCount() - 1) {
                    //TODO 新增图片
                    new TwSheetBuilder(getContext()).bindSheets(new int[]{R.mipmap.icon_camera, R.mipmap.icon_photo}, new String[]{"拍摄", "相册"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (adapter.getCount() == 10) {
                                Utils.showToast(getContext(), "最多9张照片！");
                                return;
                            }
                            if (i == 0) {
                                if (PermissionsUtil.hasCameraPermission(getActivity())) {
                                    takeCamera();
                                }
                            } else {
//                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                                photoPickerIntent.setType("image/*");
//                                startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
                                AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
                                AndroidImagePicker.getInstance().setShouldShowCamera(false);
                                AndroidImagePicker.getInstance().setSelectLimit(9 - (adapter.getCount() - 1));
                                Intent intent = new Intent(getContext(), ImagesGridActivity.class);
                                getActivity().startActivityForResult(intent, REQUEST_CODE_ALBUM);
                            }
                        }
                    }).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", i);
                    bundle.putStringArrayList("list", adapter.getBigImgList());
                    FragmentUtil.navigateToInNewActivity(getActivity(), ImagePagerFragment.class, bundle);
                }
            }
        });
        gridView.setAdapter(adapter);
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateToInNewActivity(getActivity(), TicketRelationFragment.class, getArguments());
            }
        });
        rootView = (LinearLayout) view.findViewById(R.id.root);
        initLoadImg(view.findViewById(R.id.load));
        requestDetail(false);
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
        if (requestCode == REQUEST_CODE_ALBUM) {
//            Uri newUri;
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                newUri = Uri.parse("file:///" + FileUtil.getPath(getContext(), data.getData()));
//            } else {
//                newUri = data.getData();
//            }
//            if (newUri != null) {
//                startPhotoZoom(newUri);
//            } else {
//                Toast.makeText(getContext(), "没有得到相册图片", Toast.LENGTH_LONG).show();
//            }
            List<ImageItem> imgs = AndroidImagePicker.getInstance().getSelectedImages();
            if (imgs.size() > 0) {
                //TODO
                compressImg(imgs);
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            List<ImageItem> sets = new ArrayList<>();
            ImageItem set = new ImageItem();
            set.path = file.getAbsolutePath();
            sets.add(set);
            compressImg(sets);
        }
    }

    private void compressImg(final List<ImageItem> imgs) {
        showLoadImg();
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final List<File> files = new ArrayList<File>();
                for (ImageItem set : imgs) {
                    files.add(FileUtil.getSmallBitmap(getContext(), set.path));
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postFile(files);
                        }
                    });
                }
            }
        });
    }

    private void postFile(List<File> files) {
        Map<String, String> map = new HashMap<>();
        map.put("invoiceId", invoiceId);
        map.put("oldPicIds", adapter.getIds());
        PostFormBuilder build = getHttpClient().post().url(Constant.INVOICE_PIC_UPLOAD).params(Constant.makeParam(map));
        int i = 1;
        for (File file : files) {
            build.addFile("uploadFile" + i, file.getName(), file);
            i++;
        }
        build.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                Type type = new TypeToken<BaseObjResponse<MockObj>>() {
                }.getType();
                BaseObjResponse<MockObj> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    Utils.showToast(getContext(), "图片上传成功");
                    requestDetail(true);
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }
            }
        });
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

    private void requestDetail(final boolean imgFlag) {
        showLoadImg();
        Map<String, String> map = new HashMap();
        map.put("invoiceId", invoiceId);
        getHttpClient().post().url(Constant.INVOICE_DETAIL).params(Constant.makeParam(map)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                dismissLoadImg();
                Utils.showToast(getContext(), R.string.server_error);
            }

            @Override
            public void onResponse(String s, int i) {
                dismissLoadImg();
                if (getActivity() == null) {
                    return;
                }
                Type type = new TypeToken<BaseObjResponse<InvoiceList.Invoice>>() {
                }.getType();
                BaseObjResponse<InvoiceList.Invoice> response = new Gson().fromJson(s, type);
                if ("1".equals(response.getResult().getCode())) {
                    if (!imgFlag) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View itemView = ViewTool.setFourItem(inflater, rootView, new String[]{"发票代码", "发票状态"}, new String[]{response.getResult().getInCode(), response.getResult().getStatus()});
                        ((TextView) itemView.findViewById(R.id.text1)).getPaint().setFakeBoldText(true);
                        ((TextView) itemView.findViewById(R.id.text2)).getPaint().setFakeBoldText(true);
                        itemView = ViewTool.setUpDownItem(inflater, rootView, "发票号码", response.getResult().getInNO());
                        ((TextView) itemView.findViewById(R.id.text1)).getPaint().setFakeBoldText(true);

                        ViewTool.setFourItem(inflater, rootView, new String[]{"校验码", "发票日期"}, new String[]{response.getResult().getNumber(), response.getResult().getInDate()});
                        ViewTool.setUpDownItem(inflater, rootView, "发货方", response.getResult().getDistName());
                        ViewTool.setUpDownItem(inflater, rootView, "客户", response.getResult().getCusName());
                        ViewTool.setSixItem(inflater, rootView, new String[]{"不含税金额 ¥", "税率", "含税金额 ¥"}, new String[]{response.getResult().getNonetaxTotal(), response.getResult().getTax() + "%", response.getResult().getTaxTotal()});
                        ViewTool.setUpDownItem(inflater, rootView, "备注", response.getResult().getInRemark());
                    }
                    setImg(response.getResult().getPicList());
                } else {
                    Utils.showToast(getContext(), response.getResult().getMsg());
                }


            }
        });
    }

    public void setImg(List<PicModel> imgs) {
        try {
            adapter.clear();
            adapter.addAll(imgs);
        } catch (Exception e) {

        } finally {
            PicModel item = new PicModel();
            item.setType(1);
            adapter.addItem(item);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除图片
     */
    private void deleteImg(int position) {

    }
}
