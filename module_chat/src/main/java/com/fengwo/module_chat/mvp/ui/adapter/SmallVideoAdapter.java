package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.ui.activity.social.userpage.SmallVideoFragment;
import com.fengwo.module_comment.utils.ScreenUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 社交个人详情小视频列表适配器
 * @author chenshanghui
 * @intro
 * @date 2019/9/23
 */
public class SmallVideoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    private int itemHeight = 0;

    public SmallVideoAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.chat_item_small_video,data);
        int screenWidth = ScreenUtils.getScreenWidth(context);
        itemHeight = (int) ((screenWidth - context.getResources().getDimension(R.dimen.dp_32)/*总的留白间隙*/)/3 * 1.304f/*设计稿的比例 -> 高/宽 */);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        ImageView ivImg = helper.getView(R.id.iv_img);
        ViewGroup.LayoutParams ivImgLayoutParams= ivImg.getLayoutParams();
        ivImgLayoutParams.height = itemHeight;
    }
}
