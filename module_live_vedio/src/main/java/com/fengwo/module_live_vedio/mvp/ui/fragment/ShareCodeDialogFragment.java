package com.fengwo.module_live_vedio.mvp.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fengwo.module_comment.base.BaseNiceDialog;
import com.fengwo.module_comment.iservice.UserProviderService;
import com.fengwo.module_comment.utils.BitmapUtils;
import com.fengwo.module_comment.utils.DensityUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.QRCodeUtils;
import com.fengwo.module_comment.utils.ViewHolder;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.mvp.dto.ShareInfoDto;

import java.io.Serializable;

public class ShareCodeDialogFragment extends BaseNiceDialog implements View.OnClickListener {

    private ConstraintLayout llBitmap;
    public static final String KEY_SHARE_INFO = "KEY_SHARE_INFO";
    @Autowired
    UserProviderService userProviderService;
    public static ShareCodeDialogFragment getInstance(ShareInfoDto shareInfoDto){
        ShareCodeDialogFragment fragment = new ShareCodeDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_SHARE_INFO,shareInfoDto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int intLayoutId() {
        return   R.layout.dialogfragment_sharecode;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        ShareInfoDto shareInfoDto = (ShareInfoDto) getArguments().getSerializable(KEY_SHARE_INFO);
        ARouter.getInstance().inject(this);
        ImageView iv = holder.getView(R.id.iv);
        ImageView ivCode = holder.getView(R.id.iv_code);
        holder.setText(R.id.tv_id,String.format("蜂窝推荐码: %s", userProviderService.getUserInfo().fwId));
        llBitmap = holder.getView(R.id.cl_bitmap);
        holder.setOnClickListener(R.id.btn_save,this);
        ImageLoader.loadImg(iv, shareInfoDto.getPoster());
        QRCodeUtils.generQRCode(shareInfoDto.getLink(),
                DensityUtils.dp2px(getActivity(),77), bitmap -> ivCode.setImageBitmap(bitmap));
    }

    @Override
    public void onClick(View v) {
        Bitmap m = BitmapUtils.createViewBitmap(llBitmap);
        BitmapUtils.saveBitmap(m, getActivity());
        Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
    }

}
