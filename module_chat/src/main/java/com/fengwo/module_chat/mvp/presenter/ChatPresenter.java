package com.fengwo.module_chat.mvp.presenter;

import android.app.Activity;

import com.fengwo.module_chat.mvp.ui.contract.IChatView;
import com.fengwo.module_comment.base.BasePresenter;
import com.fengwo.module_comment.utils.UploadHelper;
import com.fengwo.module_websocket.EventConstant;
import com.fengwo.module_websocket.FWWebSocket;

import org.json.JSONObject;

import java.io.File;

/**
 * @author chenshanghui
 * @intro
 * @date 2019/9/19
 */
public class ChatPresenter extends BasePresenter<IChatView> {

    private String toUid;
    private String fromUid;

    public ChatPresenter() {
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public void sendImageMessage(File f, int imgWidth, int imgHeight) {
        UploadHelper uploadHelper = UploadHelper.getInstance(((Activity) getView()).getApplicationContext());
        uploadHelper.doUpload(UploadHelper.TYPE_IMAGE, f, new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @Override
            public void onSuccess(String url) {
                try {
                    String message = f.getName() + "&" + url + "&" + imgWidth + "&" + imgHeight;
                    JSONObject clientpara = new JSONObject();
                    clientpara.put("eventId", EventConstant.saveAMessage);
                    clientpara.put("fromUid", "" + fromUid);
                    clientpara.put("toUid", "" + toUid);
                    clientpara.put("forword", "0");
                    clientpara.put("message", message);
                    clientpara.put("messageType", "1");

                    sendMessage(clientpara.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        });


//        HttpClient.getInstance((Context) getView()).uploadAudioFiles(imgPath, imgName, para, new HttpClient.MyCallback() {
//            @Override
//            public void success(Response res) throws IOException {
//
//                try {
//                    String json = res.body().string();
//                    json = desUtils.decrypt(json);
//                    Logger.e(json);
//                    JSONObject jsonObject = new JSONObject(json);
//                    String file_ip = jsonObject.getString("description");
//
//                    //"\(imgName)&\(file_ip)&\(img.size.width)&\(img.size.height)"
//                    String message = imgName + "&" + file_ip + "&" + imgWidth + "&" + imgHeight;
//                    JSONObject clientpara = new JSONObject();
//                    clientpara.put("eventId", EventConstant.saveAMessage);
//                    clientpara.put("fromUid", "" + fromUid);
//                    clientpara.put("toUid", "" + toUid);
//                    clientpara.put("forword", "0");
//                    clientpara.put("message", message);
//                    clientpara.put("messageType", "1");
//
//                    sendMessage(clientpara.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void failed(IOException e) {
//
//            }
//        });
    }

    public void sendAudioMessage(File f, double audioLength) {
        UploadHelper uploadHelper = UploadHelper.getInstance(((Activity) getView()).getApplicationContext());
        uploadHelper.doUpload(UploadHelper.TYPE_AUDIOS, f, new UploadHelper.OnUploadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long cur, long total) {

            }

            @Override
            public void onSuccess(String url) {
                try {
                    String message = f.getName() + "&" + url + "&" + audioLength;
                    JSONObject clientpara = new JSONObject();
                    clientpara.put("eventId", EventConstant.saveAMessage);
                    clientpara.put("fromUid", "" + fromUid);
                    clientpara.put("toUid", "" + toUid);
                    clientpara.put("forword", "0");
                    clientpara.put("message", message);
                    clientpara.put("messageType", "2");

                    sendMessage(clientpara.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
            }
        });
    }

    public void sendTextMessage(String content) {
        try {
            JSONObject clientpara = new JSONObject();
            clientpara.put("eventId", EventConstant.saveAMessage);
            clientpara.put("fromUid", "" + fromUid);
            clientpara.put("toUid", "" + toUid);
            clientpara.put("forword", "0");
            clientpara.put("message", content);
            clientpara.put("messageType", "0");
            sendMessage(clientpara.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String msg) {
        FWWebSocket.getInstance().sendTextMessage(msg);
    }

}
