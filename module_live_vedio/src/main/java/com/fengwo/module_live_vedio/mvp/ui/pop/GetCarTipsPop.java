package com.fengwo.module_live_vedio.mvp.ui.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_live_vedio.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author GuiLianL
 * @intro
 * @date 2020/6/11
 */
public class GetCarTipsPop extends BasePopupWindow {

    private ImageView ivSignCar;
    private TextView tvMotorName;
    private TextView tvSure;
    public GetCarTipsPop(Context context, String carUrl,String name) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        ivSignCar = findViewById(R.id.iv_sign_car);
        tvMotorName = findViewById(R.id.tv_motor_name);
        tvSure = findViewById(R.id.tv_sure);
        ImageLoader.loadImg(ivSignCar,carUrl);
        tvMotorName.setText("恭喜获得"+name+"座驾*30天");

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_sign_car);
    }

}
