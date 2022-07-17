package com.fengwo.module_chat.mvp.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fengwo.module_chat.R;
import com.fengwo.module_chat.mvp.model.bean.EmosBody;
import com.fengwo.module_chat.mvp.ui.activity.social.ChatActivity;
import com.fengwo.module_chat.utils.Logger;

import java.util.List;


public class EmosListViewAdapter extends BaseAdapter {

    private Context context;//上下文对象
    private List<EmosBody> dataList;//ListView显示的数据
    String[] emoticon_array = {"00.png","01.png","02.png","03.png","04.png","05.png","06.png","07.png","08.png","09.png","10.png","11.png","12.png","13.png","14.png","15.png","16.png","17.png","18.png","19.png","20.png","21.png","22.png","23.png"};
    String[] emoticon_array_txt = {"[招呼]","[害羞]","[调皮]","[委屈]","[流汗]","[敲你]","[微笑]","[坏笑]","[抠鼻]","[发火]","[疑问]","[惊讶]","[低级]","[耍酷]"
            ,"[尴尬]","[流泪]","[流汗]","[晕倒]","[偷笑]","[鼓掌]","[鼻滴]","[好色]","[傻笑]","[亲亲]"};

    /**
     * 构造器
     *
     * @param context  上下文对象
     * @param dataList 数据
     */
    public EmosListViewAdapter(Context context, List<EmosBody> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //判断是否有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.emos_item_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final EmosBody emosBody = dataList.get(position);
        viewHolder.emos_1.setImageResource(emosBody.getEmos_01());
        viewHolder.emos_2.setImageResource(emosBody.getEmos_02());
        viewHolder.emos_3.setImageResource(emosBody.getEmos_03());
        viewHolder.emos_4.setImageResource(emosBody.getEmos_04());
        viewHolder.emos_5.setImageResource(emosBody.getEmos_05());
        viewHolder.emos_6.setImageResource(emosBody.getEmos_06());
        viewHolder.emos_7.setImageResource(emosBody.getEmos_07());
        viewHolder.emos_8.setImageResource(emosBody.getEmos_08());


        //单击事件
        viewHolder.emos_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 0]);

                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 0]);

            }
        });
        viewHolder.emos_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 1]);

                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 1]);

            }
        });
        viewHolder.emos_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 2]);
                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 2]);

            }
        });
        viewHolder.emos_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 3]);
                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 3]);

            }
        });
        viewHolder.emos_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 4]);
                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 4]);

            }
        });
        viewHolder.emos_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 5]);
                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 5]);

            }
        });

        viewHolder.emos_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 6]);
                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 6]);

            }
        });
        viewHolder.emos_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e(emoticon_array_txt[position * 8 + 7]);
                int index = ((ChatActivity)context).input_box.getSelectionStart();
                Editable editable = ((ChatActivity)context).input_box.getText();
                editable.insert(index, emoticon_array_txt[position * 8 + 7]);

            }
        });

        return convertView;
    }

    /**
     * ViewHolder类
     */
    private final class ViewHolder {

        ImageView emos_1;//图片
        ImageView emos_2;//图片
        ImageView emos_3;//图片
        ImageView emos_4;//图片
        ImageView emos_5;//图片
        ImageView emos_6;//图片
        ImageView emos_7;//图片
        ImageView emos_8;//图片

        /**
         * 构造器
         *
         * @param view 视图组件（ListView的子项视图）
         */
        ViewHolder(View view) {
            emos_1 = (ImageView) view.findViewById(R.id.emos_1);
            emos_2 = (ImageView) view.findViewById(R.id.emos_2);
            emos_3 = (ImageView) view.findViewById(R.id.emos_3);
            emos_4 = (ImageView) view.findViewById(R.id.emos_4);
            emos_5 = (ImageView) view.findViewById(R.id.emos_5);
            emos_6 = (ImageView) view.findViewById(R.id.emos_6);
            emos_7 = (ImageView) view.findViewById(R.id.emos_7);
            emos_8 = (ImageView) view.findViewById(R.id.emos_8);
        }
    }


}
