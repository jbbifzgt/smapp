package com.trueway.app.uilib.imgpick;/**
 * Created by user on 2017/9/9.
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.trueway.app.uilib.model.ImageItem;
import com.trueway.app.uilib.widget.photoview.PhotoView;

/**
 * author:caijw
 * <p/>
 * date:2017/9/9 14:13
 */
public class SinglePreviewFragment extends Fragment {
    public static final String KEY_URL = "key_url";
    private PhotoView imageView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        ImageItem imageItem = (ImageItem) bundle.getSerializable(KEY_URL);

        url = imageItem.path;


        imageView = new PhotoView(getActivity());
        imageView.setBackgroundColor(0xff000000);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);


        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "file://" + url;
        }
        Picasso.with(getContext()).load(Uri.parse(url)).resize(768, 1280).centerCrop().into(imageView);

//        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(
//                Uri.parse(url))
//                .setResizeOptions(new ResizeOptions(768, 1280))
//                .setAutoRotateEnabled(true);
//
//        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
//        controller.setOldController(imageView.getController());
//        controller.setImageRequest(requestBuilder.build());
//        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//                super.onFinalImageSet(id, imageInfo, animatable);
//                if (imageInfo == null) {
//                    return;
//                }
//                imageView.update(imageInfo.getWidth(), imageInfo.getHeight());
//            }
//        });
//        imageView.setController(controller.build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return imageView;
    }
}
