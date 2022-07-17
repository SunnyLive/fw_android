package com.fengwo.module_comment.utils.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fengwo.module_comment.R;
import com.fengwo.module_comment.event.GameEvent;
import com.fengwo.module_comment.utils.RxBus;


/**
 * ================================================
 * 作    者：Fuzp
 * 版    本：1.0
 * 创建日期：2018/11/15.
 * 描    述：聊天表情功能片段
 * 修订历史：
 * ================================================
 */
public class ChatGameFragment extends Fragment implements View.OnClickListener {

    View ivCaiquan;
    View ivTouzi;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_game, null);

        return view;
    }


    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        ivCaiquan = v.findViewById(R.id.iv_caiquan);
        ivTouzi = v.findViewById(R.id.iv_touzi);
        ivCaiquan.setOnClickListener(this);
        ivTouzi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_caiquan) {
            RxBus.get().post(new GameEvent(GameEvent.TYPE_CAIQUAN));
        } else if (id == R.id.iv_touzi) {
            RxBus.get().post(new GameEvent(GameEvent.TYPE_TOUZI));
        }
    }
}
