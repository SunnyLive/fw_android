package com.fengwo.module_flirt.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fengwo.module_chat.utils.TimeUtils;
import com.fengwo.module_chat.utils.chat_new.VoicePlayHelper;
import com.fengwo.module_comment.utils.CommentUtils;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.KLog;
import com.fengwo.module_comment.utils.chat.ChatTimeUtils;
import com.fengwo.module_flirt.R;
import com.fengwo.module_flirt.UI.DrawTextView;
import com.fengwo.module_flirt.bean.CityHost;
import com.fengwo.module_flirt.widget.DetailCardView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 卡片详情适配器
 *
 * @Author BLCS
 * @Time 2020/3/27 17:33
 */
public class FlirtCardDetailsAdapter extends BaseQuickAdapter<CityHost, BaseViewHolder> {
    SparseIntArray currentPoints;

    //    SparseArray<DetailCardView> detailCardViewSparseArray;
//    SparseArray<ImageView> wavaImageViews;
    private VoicePlayHelper voicePlayHelper;

    public FlirtCardDetailsAdapter(VoicePlayHelper voicePlayHelper) {
        super(R.layout.adapter_flirt_card_details);
        this.voicePlayHelper = voicePlayHelper;
        currentPoints = new SparseIntArray();
//        detailCardViewSparseArray = new SparseArray<>();
//        wavaImageViews = new SparseArray<>();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, CityHost item) {
        holder.addOnClickListener(R.id.iv_card_close, R.id.iv_card_setting, R.id.ll_card_flirt, R.id.iv_card_header, R.id.tv_card_player_status);

        String commentTime = item.getEndLiveTime() > 0 ? ChatTimeUtils.getFlirtCardTime(item.getEndLiveTime()) + " " : "";

        holder.setText(R.id.tv_card_status, commentTime);


//        String occuName = TextUtils.isEmpty(item.getOccuName()) ? "" : item.getOccuName() + "";
//        holder.setText(R.id.  tv_card_details_type, location);
//        holder.setGone(R.id.  tv_card_details_type, !TextUtils.isEmpty(item.getLocation()));


//        holder.setText(R.id.tv_charm, item.getCharm() + "");//魅力值 产品提示隐藏

        holder.setText(R.id.tv_card_name, item.getNickname());//姓名
        //性别
        boolean sexIsUnknown = item.getSex() == 0 || item.getSex() == 3;
        if (sexIsUnknown && item.getAge() == 0) {
            holder.setGone(R.id.tv_card_age_and_sex, false);
        } else {
            holder.setGone(R.id.tv_card_age_and_sex, true);
            if (item.getAge() <= 0) {
                holder.setText(R.id.tv_card_age_and_sex, null);
            } else {
                holder.setText(R.id.tv_card_age_and_sex, item.getAge() + "岁");
            }

            DrawTextView drawTextView = holder.getView(R.id.tv_card_age_and_sex);
            if (item.getSex() == 0 || item.getSex() == 3) {
                drawTextView.setStartDrawAble(R.drawable.ic_gender, R.dimen.dp_0, R.dimen.dp_0);
            } else {
                drawTextView.setStartDrawAble(R.drawable.ic_gender, R.dimen.dp_10, R.dimen.dp_10);
            }
            /*语音*/
            holder.setVisible(R.id.ll_card_voice, !TextUtils.isEmpty(item.getAudioPath()));

            holder.addOnClickListener(R.id.ll_card_voice);
            holder.setText(R.id.tv_voice_time, TimeUtils.int2String(item.getAudioLength()));
            holder.getView(R.id.tv_card_age_and_sex).setSelected(item.getSex() != 1);
        }


        CircleImageView circleImageView = holder.getView(R.id.iv_card_header);
        ImageLoader.loadCircleWithBorder(circleImageView, item.getHeadImg(), 5, Color.WHITE);
        /*直播状态*/
        holder.setGone(R.id.tv_card_player_status, item.getLiveStatus() == 2);


        holder.addOnClickListener(R.id.ll_card_voice);

        /*标签*/
        initLabel(holder, item);
        holder.setText(R.id.tv_card_flirt, item.getBstatus() == 0 ? "打招呼" : "视频");
        /*---------------------------------------*/
        //setStatus(item.getBstatus(), holder.getAdapterPosition());
        TextView tvStatus = (TextView) holder.getView(R.id.tv_card_status);
        String location = TextUtils.isEmpty(item.getLocation()) ? "" : item.getLocation() + " ";
        tvStatus.setText(commentTime + location);

//        TextView tv_card_details_city = (TextView) holder.getView(R.id.tv_card_details_city);
//        if (location.equals("")) {
//            tv_card_details_city.setVisibility(View.GONE);
//        } else {
//            tv_card_details_city.setText(location);
//            tv_card_details_city.setVisibility(View.VISIBLE);
//        }
        String occuName = TextUtils.isEmpty(item.getOccuName()) ? "" : item.getOccuName() + "";
        TextView tv_card_details_occupation = (TextView) holder.getView(R.id.tv_card_details_occupation);
        if (occuName.equals("")) {
            tv_card_details_occupation.setVisibility(View.GONE);
        } else {
            tv_card_details_occupation.setText(occuName);
            tv_card_details_occupation.setVisibility(View.VISIBLE);
        }

        DetailCardView view = holder.getView(R.id.detail_view);
        TextView tv_page = holder.getView(R.id.tv_page);
        if (null != item.getRes()) {
            tv_page.setText(String.format("1/%d", item.getRes().size()));
            view.addClickPlayListener(new DetailCardView.ClickPlayListener() {
                @Override
                public void click() {
                    stopVoicePlay(holder.getAdapterPosition());
                    voicePlayHelper.stopVoice();
                }

                @Override
                public void setPsition(int psition) {
                    tv_page.setText(psition + 1 + "/" + item.getRes().size());
                }
            });
//        detailCardViewSparseArray.put(position, view);
            view.setBanner(item.getRes());
        }
    }

    public void setStatus(int status, int pos) {
        TextView tvBtn = (TextView) getViewByPosition(pos, R.id.tv_card_flirt);
        if (tvBtn != null) {
            KLog.e("tag", "status=" + status + "position" + pos);
            if (status == 0) {
                tvBtn.setText("打招呼");
            } else {
                tvBtn.setText("视频");
            }
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    /**
     * 显示标签
     */
    private void initLabel(@NonNull BaseViewHolder holder, CityHost item) {
        try {
            RecyclerView mRvLabel = holder.getView(R.id.rv_card_label);
            //    mRvLabel.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mRvLabel.setLayoutManager(new GridLayoutManager(mContext, 4));
            CardLabelAdapter cardLableAdapter = new CardLabelAdapter();
            mRvLabel.setAdapter(cardLableAdapter);
            String tagName = item.getTagName();
            if (!TextUtils.isEmpty(tagName)) {
                String[] split = tagName.split(",");
                List<String> tables = Arrays.asList(split);
                cardLableAdapter.setNewData(tables);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startVoicePlay(int pos) {
        ImageView ivWava = (ImageView) getViewByPosition(pos, R.id.iv_wava);
        if (null != ivWava) {//TODO 动图未修改
            ImageLoader.loadGif(ivWava, R.drawable.pic_video_mp3);
//            wavaImageViews.put(pos, ivWava);
        }
    }

    public void stopVoicePlay(int pos) {
        ImageView ivWava = (ImageView) getViewByPosition(pos, R.id.iv_wava);
        if (null != ivWava) {
            ivWava.setImageResource(R.mipmap.pic_video);
        }
    }

//    public void refreshCardView(int pos) {
//        DetailCardView detailCardView = detailCardViewSparseArray.get(pos);
//        if (null != detailCardView) {
//            detailCardView.refreshView();
//        }
//    }

    public void stopMediaPlay(int pos) {
        DetailCardView detailCardView = (DetailCardView) getViewByPosition(pos, R.id.detail_view);
//        DetailCardView detailCardView = detailCardViewSparseArray.get(pos);
        if (null != detailCardView) {
            detailCardView.stopPlay();
        }
    }

    public void replaceData(int pos, CityHost data) {
        DetailCardView detailCardView = (DetailCardView) getViewByPosition(pos, R.id.detail_view);
        if (null != detailCardView) {
            detailCardView.setBanner(data.getRes());
        }

      //  setStatus(data.getBstatus(), pos);
        TextView tvStatus = (TextView) getViewByPosition(pos, R.id.tv_card_status);
        String commentTime = data.getEndLiveTime() > 0 ? ChatTimeUtils.getFlirtCardTime(data.getEndLiveTime()) + " " : "";
        String location = TextUtils.isEmpty(data.getLocation()) ? "" : data.getLocation() + " ";
        tvStatus.setText(commentTime + location);

//        TextView tv_card_details_city = (TextView) getViewByPosition(pos, R.id.tv_card_details_city);
//        if (location.equals("")) {
//            tv_card_details_city.setVisibility(View.GONE);
//        } else {
//            tv_card_details_city.setText(location);
//            tv_card_details_city.setVisibility(View.VISIBLE);
//        }
        String occuName = TextUtils.isEmpty(data.getOccuName()) ? "" : data.getOccuName() + "";
        TextView tv_card_details_occupation = (TextView) getViewByPosition(pos, R.id.tv_card_details_occupation);
        if (occuName.equals("")) {
            tv_card_details_occupation.setVisibility(View.GONE);
        } else {
            tv_card_details_occupation.setText(occuName);
            tv_card_details_occupation.setVisibility(View.VISIBLE);
        }
    }
}
