package com.fengwo.module_flirt.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_flirt.R;
import com.fengwo.module_websocket.bean.ReceiveCommentMsg;

import java.util.List;

public class ReceiveCommentGiftAdapter extends BaseQuickAdapter<ReceiveCommentGiftAdapter.GiftListDTO, BaseViewHolder> {
    public ReceiveCommentGiftAdapter() {
        super(R.layout.adapter_receive_comment_gift);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GiftListDTO item) {
        ImageView giftImageView = helper.getView(R.id.gift_img);
        ImageLoader.loadImg(giftImageView, item.getGiftSmallImgPath());
        helper.setText(R.id.gift_name, item.getGiftName());
        helper.setText(R.id.gift_count, "x" + item.getGiftNum());
    }

    public static class GiftListDTO {

        private String giftName;
        private int  giftNum;
        private String giftSmallImgPath;

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public int getGiftNum() {
            return giftNum;
        }

        public void setGiftNum(int giftNum) {
            this.giftNum = giftNum;
        }

        public String getGiftSmallImgPath() {
            return giftSmallImgPath;
        }

        public void setGiftSmallImgPath(String giftSmallImgPath) {
            this.giftSmallImgPath = giftSmallImgPath;
        }
    }
}
