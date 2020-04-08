package com.android.library.ui;

import android.view.View;

import com.android.baselibrary.baseui.BaseActivity;
import com.android.baselibrary.baseui.BasePresenter;
import com.android.library.R;

import butterknife.BindView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.GlideImageView;
import com.sunfusheng.progress.CircleProgressView;

public class ImageActivity extends BaseActivity {

    @BindView(R.id.img1)
    GlideImageView img1;
    @BindView(R.id.img2)
    GlideImageView img2;
    @BindView(R.id.img3)
    GlideImageView img3;
    @BindView(R.id.image31)
    GlideImageView image31;
    @BindView(R.id.progressView1)
    CircleProgressView progressView1;

    String url1 =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497688355699&di"
        + "=ea69a930b82ce88561c635089995e124&imgtype=0&src=http%3A%2F%2Fcms-bucket"
        + ".nosdn.127.net%2Ff84e566bcf654b3698363409fbd676ef20161119091503.jpg";
    String url2 = "http://img1.imgtn.bdimg.com/it/u=4027212837,1228313366&fm=23&gp=0.jpg";
    String url3 =
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529402445474&di"
        + "=b5da3b2f6a466e618e1e32d4dd2bda4d&imgtype=0&src=http%3A%2F%2F2b.zol-img.com"
        + ".cn%2Fproduct%2F133_500x2000%2F801%2Fce21ke76FRh4A.jpg";

    String gif1 = "http://img.zcool.cn/community/01e97857c929630000012e7e3c2acf.gif";
    String gif2 =
        "http://5b0988e595225.cdn.sohucs.com/images/20171202/a1cc52d5522f48a8a2d6e7426b13f82b.gif";
    String gif3 = "http://img.zcool.cn/community/01d6dd554b93f0000001bf72b4f6ec.jpg";

    public static final String cat =
        "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/cat.jpg";
    public static final String cat_thumbnail =
        "https://raw.githubusercontent"
        + ".com/sfsheng0322/GlideImageView/master/resources/cat_thumbnail.jpg";

    public static final String girl =
        "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/girl.jpg";
    public static final String girl_thumbnail =
        "https://raw.githubusercontent"
        + ".com/sfsheng0322/GlideImageView/master/resources/girl_thumbnail.jpg";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_image;
    }

    @Override
    protected void initViewsAndEvents() {
        line1();
    }

    private void line1() {

        img1.enableState(true).load(url1);
        img2.loadCircle(url1);
        img3.load(url2, R.drawable.ic_arrow, 10);

      /*     image31.centerCrop().error(R.mipmap.image_load_err)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .load(girl, R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
                if (isComplete) {
                    progressView1.setVisibility(View.GONE);
                } else {
                    progressView1.setVisibility(View.VISIBLE);
                    progressView1.setProgress(percentage);
                }
            });*/

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(NineImageViewActivity.class);
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
