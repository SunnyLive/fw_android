package com.fengwo.module_live_vedio.mvp.ui.df;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_comment.utils.ImageLoader;
import com.fengwo.module_comment.utils.RetrofitUtils;
import com.fengwo.module_comment.utils.RxUtils;
import com.fengwo.module_comment.utils.ToastUtils;
import com.fengwo.module_live_vedio.R;
import com.fengwo.module_live_vedio.api.LiveApiService;
import com.fengwo.module_live_vedio.mvp.dto.RoomMemberChangeMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InviteTeamPKDialogFragment extends BaseDialogFragment {

    private final int MAX_INVITE_NUM = 3;

    private GridLayoutManager gridLayoutManager;
    private InviteAdapter adapter;
    private int num = 0;
    private List<RoomMemberChangeMsg> list = new ArrayList<>();
    private int userId;
    private boolean isRoomManager = false;
    private String teamId;


    public static DialogFragment getInstance(int userId, List<RoomMemberChangeMsg> list) {
        InviteTeamPKDialogFragment dialogFragment = new InviteTeamPKDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        bundle.putSerializable("list", (Serializable) list);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected void initView() {
        userId = getArguments().getInt("userId");
        list = (List<RoomMemberChangeMsg>) getArguments().getSerializable("list");
        num = list.size();
        view.findViewById(R.id.ivInviteClose).setOnClickListener(v -> {
            dismiss();

        });
        view.findViewById(R.id.tvConfirm).setOnClickListener(v -> {
//            num += 1;
//            setData(num);
            if (onAddRandomTeamPkListener != null) {
                onAddRandomTeamPkListener.onAddRandomTeamPk(teamId);
            }
        });
        refreshManagerView();
        RecyclerView recyclerView = view.findViewById(R.id.rvInvite);
        gridLayoutManager = new GridLayoutManager(getContext(), MAX_INVITE_NUM);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new InviteAdapter(list);
        recyclerView.setAdapter(adapter);

    }

    private void refreshManagerView() {
        if (list.size() <= 0) return;
        if (list.size() > 0 && list.get(0) != null)
            this.teamId = list.get(0).teamId;
        for (RoomMemberChangeMsg roomMemberChangeMsg : list) {
            if (roomMemberChangeMsg.leader == 1 && roomMemberChangeMsg.userId == userId) {
                view.findViewById(R.id.tvConfirm).setVisibility(View.VISIBLE);
                isRoomManager = true;
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.live_dialog_invite_pk;
    }

    @Override
    public void onStart() {
        super.onStart();
        setData(num);
    }

    public void addData(List<RoomMemberChangeMsg> list) {
        setData(list.size());
        this.list = list;
        if (adapter != null) adapter.addData(list);
        refreshManagerView();
    }

    public boolean isFull() {
        return list.size() == MAX_INVITE_NUM;
    }

    private void setData(@IntRange(from = 0, to = MAX_INVITE_NUM) int num) {
        this.num = num;
        if (adapter == null) return;
        gridLayoutManager.setSpanCount(num < MAX_INVITE_NUM ? num + 1 : MAX_INVITE_NUM);
        adapter.setNumber(num);
    }

    public interface OnInviteTeamListener {
        void onInviteTeam(String teamId);
    }

    public interface OnAddRandomTeamPkListener {
        void onAddRandomTeamPk(String teamId);
    }

    private OnInviteTeamListener onInviteTeamListener;
    private OnAddRandomTeamPkListener onAddRandomTeamPkListener;

    public void setOnInviteTeamListener(OnInviteTeamListener onInviteTeamListener) {
        this.onInviteTeamListener = onInviteTeamListener;
    }

    public void setOnAddRandomTeamPkListener(OnAddRandomTeamPkListener onAddRandomTeamPkListener) {
        this.onAddRandomTeamPkListener = onAddRandomTeamPkListener;
    }

    private class InviteAdapter extends RecyclerView.Adapter {

        private final int TYPE_CONTENT = 0;
        private final int TYPE_INVITE = 1;

        private int contentNum = 0;
        private List<RoomMemberChangeMsg> pkMsgs;

        public InviteAdapter(List<RoomMemberChangeMsg> pkMsgs) {
            this.pkMsgs = pkMsgs;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_CONTENT:
                    return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.live_item_invite_pk, parent, false));
                case TYPE_INVITE:
                    return new InviteViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.live_item_invite_pk, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ContentViewHolder) {
                ContentViewHolder viewHolder = (ContentViewHolder) holder;
                viewHolder.tvTitle.setText(pkMsgs.get(position).nickname);
                ImageLoader.loadImg(viewHolder.ivHeader, pkMsgs.get(position).headImg);
            }
//            if (holder instanceof InviteViewHolder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onInviteTeamListener != null) {
                        onInviteTeamListener.onInviteTeam(teamId);
                    }
                }
            });
//            }
        }

        @Override
        public int getItemCount() {
            if (isRoomManager) {
                return contentNum >= MAX_INVITE_NUM ? MAX_INVITE_NUM : contentNum + 1;
            } else {
                return contentNum;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRoomManager) {
                return position < contentNum ? TYPE_CONTENT : TYPE_INVITE;
            } else {
                return TYPE_CONTENT;
            }
        }

        public void setNumber(int number) {
            this.contentNum = number;
        }

        public void addData(List<RoomMemberChangeMsg> invitePkMsg) {
            pkMsgs = invitePkMsg;
            notifyDataSetChanged();
        }

        private class ContentViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvTitle;
            private final ImageView ivHeader;

            ContentViewHolder(@NonNull View itemView) {
                super(itemView);
                ivHeader = itemView.findViewById(R.id.ivItemInVite);
                tvTitle = itemView.findViewById(R.id.tvInviteTitle);
            }
        }

        private class InviteViewHolder extends RecyclerView.ViewHolder {
            private final TextView tvTitle;
            private final ImageView ivHeader;

            InviteViewHolder(@NonNull View itemView) {
                super(itemView);
                ivHeader = itemView.findViewById(R.id.ivItemInVite);
                tvTitle = itemView.findViewById(R.id.tvInviteTitle);
                tvTitle.setText("邀请好友");
                ivHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onInviteTeamListener != null) {
                            onInviteTeamListener.onInviteTeam(teamId);
                        }
                    }
                });
            }
        }
    }

    /**
     * 离开了房间
     */
    public void leavePkRoom(String teamId) {
        list.clear();
        new RetrofitUtils().createApi(LiveApiService.class).leavePKRoom(teamId)
                .compose(RxUtils.applySchedulers())
                .subscribe(new LoadingObserver<HttpResult>() {
                    @Override
                    public void _onNext(HttpResult data) {
                        dismiss();
                    }

                    @Override
                    public void _onError(String msg) {
                        ToastUtils.showLong(getActivity(), msg);
                    }
                });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            leavePkRoom(list.get(0).teamId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean cancelable() {
        return false;
    }
}