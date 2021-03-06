/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.trueway.app.uilib.imgpick;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.trueway.app.uilib.R;
import com.trueway.app.uilib.model.ImageItem;
import com.trueway.app.uilib.tool.PermissionsUtil;
import com.trueway.app.uilib.tool.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImagesGridFragment extends Fragment implements OnImagesLoadedListener, AndroidImagePicker.OnImageSelectedListener, AndroidImagePicker.OnImageCropCompleteListener {
    private static final String TAG = ImagesGridFragment.class.getSimpleName();
    private static final int ITEM_TYPE_CAMERA = 0;//the first Item may be Camera
    private static final int ITEM_TYPE_NORMAL = 1;
    Activity mContext;
    GridView mGridView;
    ImageGridAdapter mAdapter;
    int imageGridSize;
    Button btnDir;//button to change ImageSet
    List<ImageSet> mImageSetList;//data of all ImageSets
    //    ImagePresenter mImagePresenter;
    AndroidImagePicker androidImagePicker;
    private View mFooterView;
    private ListPopupWindow mFolderPopupWindow;//ImageSet PopupWindow
    private ImageSetAdapter mImageSetAdapter;
    private OnItemClickListener mOnItemClickListener;//Grid Item click Listener

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        androidImagePicker = AndroidImagePicker.getInstance();
        //androidImagePicker.clear();

        androidImagePicker.addOnImageSelectedListener(this);
        androidImagePicker.addOnImageCropCompleteListener(this);

        //androidImagePicker.clearSelectedImages();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_images_grid, null);

        mFooterView = contentView.findViewById(R.id.footer_panel);
        imageGridSize = (mContext.getWindowManager().getDefaultDisplay().getWidth() - Utils.dp2px(mContext, 2) * 2) / 3;
        btnDir = (Button) contentView.findViewById(R.id.btn_dir);
        mGridView = (GridView) contentView.findViewById(R.id.gridview);

        /*mGridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //int firstVisibleItem

                if(scrollState == SCROLL_STATE_IDLE){
                    int lastPostion =view.getLastVisiblePosition();
                    int totalItemCount = view.getCount();

                    int preSize = totalItemCount - lastPostion <=6?totalItemCount - lastPostion:6;
                    Log.i(TAG,"=====lastVisibleItem:"+lastPostion+"   preLoad:"+preSize);
                    for(int i = 0;i<preSize-1;i++){
                        String  fileScheme = ImageDownloader.Scheme.FILE.wrap(mImageSetList.get(0).imageItems.get(lastPostion+i).path);
                        ImageSize size = new ImageSize(imageGridSize,imageGridSize);
                        ImageLoader.getInstance().loadImage(fileScheme, size, new ImageLoadingListener() {
                            @Override public void onLoadingStarted(String imageUri, View view) { }

                            @Override public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            }

                            @Override  public void onLoadingCancelled(String imageUri, View view) { }
                        });
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        }));//stop loading if fling or scrolling if using UIL*/


        DataSource dataSource = new LocalDataSource(mContext);
        dataSource.provideMediaItems(this);//select all images from local database

        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;

        btnDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFolderPopupWindow == null) {
                    createPopupFolderList(width, height);
                }
                backgroundAlpha(0.3f);
                mImageSetAdapter.refreshData(mImageSetList);
                mFolderPopupWindow.setAdapter(mImageSetAdapter);
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mImageSetAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }

            }
        });

        mImageSetAdapter = new ImageSetAdapter(mContext);
        mImageSetAdapter.refreshData(mImageSetList);

        return contentView;

    }


    public void setOnImageItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    @Override
    public void onImageSelected(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        mAdapter.refreshData(AndroidImagePicker.getInstance().getImageItemsOfCurrentImageSet());
        Log.i(TAG, "=====EVENT:onImageSelected");
    }

    @Override
    public void onImageCropComplete(Bitmap bmp, float ratio) {
        getActivity().finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && requestCode == PermissionsUtil.PERMISSION_CAMERA) {
            try {
                androidImagePicker.takePicture(ImagesGridFragment.this, AndroidImagePicker.REQ_CAMERA);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shouldSelectMulti() {
        return androidImagePicker.getSelectMode() == AndroidImagePicker.Select_Mode.MODE_MULTI;
    }

    private boolean shouldShowCamera() {
        return androidImagePicker.isShouldShowCamera();
    }

    @Override
    public void onImagesLoaded(List<ImageSet> imageSetList) {

        mImageSetList = imageSetList;

        btnDir.setText(imageSetList.get(0).name);
        mAdapter = new ImageGridAdapter(mContext, imageSetList.get(0).imageItems);
        mGridView.setAdapter(mAdapter);

    }

    /**
     * ???????????????ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(mContext);
        //mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mImageSetAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mFooterView);
        mFolderPopupWindow.setModal(true);

        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);

        mFolderPopupWindow.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mImageSetAdapter.setSelectIndex(i);
                androidImagePicker.setCurrentSelectedImageSetPosition(i);

                final int index = i;
                final AdapterView tempAdapterView = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        ImageSet imageSet = (ImageSet) tempAdapterView.getAdapter().getItem(index);
                        if (null != imageSet) {
                            mAdapter.refreshData(imageSet.imageItems);
                            btnDir.setText(imageSet.name);

                        }
                        // scroll to the top
                        mGridView.smoothScrollToPosition(0);

                    }
                }, 100);

            }
        });

    }

    // ?????????????????????
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        mContext.getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroy() {
        androidImagePicker.removeOnImageItemSelectedListener(this);
        androidImagePicker.removeOnImageCropCompleteListener(this);
        //androidImagePicker.clear();
        Log.i(TAG, "=====removeOnImageItemSelectedListener");
        Log.i(TAG, "=====removeOnImageCropCompleteListener");
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AndroidImagePicker.REQ_CAMERA && resultCode == Activity.RESULT_OK) {
            if (!TextUtils.isEmpty(androidImagePicker.getCurrentPhotoPath())) {
                AndroidImagePicker.galleryAddPic(mContext, androidImagePicker.getCurrentPhotoPath());
                Intent intent = new Intent();
                intent.putExtra("photoPath", androidImagePicker.getCurrentPhotoPath());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
//                androidImagePicker.notifyPictureTaken();
            } else {
                Log.i(TAG, "didn't save to your path");
            }
        }

    }

    /**
     * Adapter of image GridView
     */
    class ImageGridAdapter extends BaseAdapter {
        List<ImageItem> images;
        Context mContext;

        public ImageGridAdapter(Context ctx, List<ImageItem> images) {
            this.images = images;
            this.mContext = ctx;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (shouldShowCamera()) {
                return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
            }
            return ITEM_TYPE_NORMAL;
        }


        @Override
        public int getCount() {
            return shouldShowCamera() ? images.size() + 1 : images.size();
        }

        @Override
        public ImageItem getItem(int position) {
            if (shouldShowCamera()) {
                if (position == 0) {
                    return null;
                }
                return images.get(position - 1);
            } else {
                return images.get(position);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            int itemViewType = getItemViewType(position);
            if (itemViewType == ITEM_TYPE_CAMERA) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_camera, parent, false);
                convertView.setTag(null);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            String[] permissions = {Manifest.permission.CAMERA};
                            for (String str : permissions) {
                                if (getActivity().checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(permissions, PermissionsUtil.PERMISSION_CAMERA);
                                    return;
                                }
                            }
                        }
                        try {
                            androidImagePicker.takePicture(ImagesGridFragment.this, AndroidImagePicker.REQ_CAMERA);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                final ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, null);
                    holder = new ViewHolder();
                    holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_thumb);
                    holder.cbSelected = (SuperCheckBox) convertView.findViewById(R.id.iv_thumb_check);
                    holder.cbPanel = convertView.findViewById(R.id.thumb_check_panel);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if (shouldSelectMulti()) {//Multi Select mode will show a CheckBox at the Top Right corner
                    holder.cbSelected.setVisibility(View.VISIBLE);
                } else {
                    holder.cbSelected.setVisibility(View.GONE);
                }

                final ImageItem item = getItem(position);

                holder.cbSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (androidImagePicker.getSelectImageCount() > androidImagePicker.getSelectLimit()) {
                            if (holder.cbSelected.isChecked()) {
                                //had better use ImageView instead of CheckBox
                                holder.cbSelected.toggle();//do this because CheckBox will auto toggle when clicking,must inverse
                                String toast = getResources().getString(R.string.you_have_a_select_limit, androidImagePicker.getSelectLimit());
                                Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
                            } else {
                                //
                            }
                        } else {
                            //
                        }
                    }
                });

                holder.cbSelected.setOnCheckedChangeListener(null);//first set null or will have a bug when Recycling the view
                if (androidImagePicker.isSelect(position, item)) {
                    holder.cbSelected.setChecked(true);
                    holder.ivPic.setSelected(true);
                } else {
                    holder.cbSelected.setChecked(false);
                }

                ViewGroup.LayoutParams params = holder.ivPic.getLayoutParams();
                params.width = params.height = imageGridSize;

                final View imageItemView = convertView.findViewById(R.id.iv_thumb);
                imageItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(mGridView, imageItemView, position, position);
                    }
                });

                holder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            androidImagePicker.addSelectedImageItem(position, item);
                        } else {
                            androidImagePicker.deleteSelectedImageItem(position, item);
                        }

                    }

                });

                Picasso.with(getContext()).load(Uri.parse(String.format("file://%s", getItem(position).path)))
                        .resize(imageGridSize, imageGridSize).centerCrop().into(holder.ivPic);
            }

            return convertView;

        }

        public void refreshData(List<ImageItem> items) {
            if (items != null && items.size() > 0) {
                images = items;
            }
            notifyDataSetChanged();
        }

        class ViewHolder {
            ImageView ivPic;
            SuperCheckBox cbSelected;
            View cbPanel;
        }

    }

    /**
     * ImageSet adapter
     */
    class ImageSetAdapter extends BaseAdapter {
        int mImageSize;
        int lastSelected = 0;
        private Context mContext;
        private LayoutInflater mInflater;
        private List<ImageSet> mImageSets = new ArrayList<>();

        public ImageSetAdapter(Context context) {
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.image_cover_size);
        }

        public void refreshData(List<ImageSet> folders) {
            if (folders != null && folders.size() > 0) {
                mImageSets = folders;
            } else {
                mImageSets.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mImageSets.size();
        }

        @Override
        public ImageSet getItem(int i) {
            return mImageSets.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item_folder, viewGroup, false);
                holder = new ViewHolder(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.bindData(getItem(i));

            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }

            return view;
        }

        public int getSelectIndex() {
            return lastSelected;
        }

        public void setSelectIndex(int i) {
            if (lastSelected == i) {
                return;
            }
            lastSelected = i;
            notifyDataSetChanged();
        }

        class ViewHolder {
            ImageView cover;
            TextView name;
            TextView size;
            ImageView indicator;

            ViewHolder(View view) {
                cover = (ImageView) view.findViewById(R.id.cover);
                name = (TextView) view.findViewById(R.id.name);
                size = (TextView) view.findViewById(R.id.size);
                indicator = (ImageView) view.findViewById(R.id.indicator);
                view.setTag(this);
            }

            void bindData(ImageSet data) {
                name.setText(data.name);
                size.setText(data.imageItems.size() + mContext.getResources().getString(R.string.piece));
                Picasso.with(getContext()).load(Uri.parse(String.format("file://%s", data.cover.path)))
                        .resize(imageGridSize, imageGridSize).centerCrop().into(cover);
            }

        }

    }

}
