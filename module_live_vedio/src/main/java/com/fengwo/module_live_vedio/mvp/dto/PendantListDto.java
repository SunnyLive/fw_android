package com.fengwo.module_live_vedio.mvp.dto;

import java.util.List;

/**
 * @anchor Administrator
 * @date 2020/9/3
 */
public class PendantListDto {

    private List<TextStickersBean> textStickers;
    private List<GraphicStickersBean> graphicStickers;

    public List<TextStickersBean> getTextStickers() {
        return textStickers;
    }

    public void setTextStickers(List<TextStickersBean> textStickers) {
        this.textStickers = textStickers;
    }

    public List<GraphicStickersBean> getGraphicStickers() {
        return graphicStickers;
    }

    public void setGraphicStickers(List<GraphicStickersBean> graphicStickers) {
        this.graphicStickers = graphicStickers;
    }

    public static class TextStickersBean {
        /**
         * id : 1
         * stickerType : 1
         * stickerUrl : follow
         * textLength : 10
         */

        private int id;
        private int stickerType;
        private String stickerUrl;
        private int textLength;
        private String textColor;

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStickerType() {
            return stickerType;
        }

        public void setStickerType(int stickerType) {
            this.stickerType = stickerType;
        }

        public String getStickerUrl() {
            return stickerUrl;
        }

        public void setStickerUrl(String stickerUrl) {
            this.stickerUrl = stickerUrl;
        }

        public int getTextLength() {
            return textLength;
        }

        public void setTextLength(int textLength) {
            this.textLength = textLength;
        }
    }

    public static class GraphicStickersBean {
        /**
         * id : 2
         * stickerType : 2
         * stickerUrl : flowers
         * textLength : 20
         */

        private int id;
        private int stickerType;
        private String stickerUrl;
        private int textLength;
        private String textColor;

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStickerType() {
            return stickerType;
        }

        public void setStickerType(int stickerType) {
            this.stickerType = stickerType;
        }

        public String getStickerUrl() {
            return stickerUrl;
        }

        public void setStickerUrl(String stickerUrl) {
            this.stickerUrl = stickerUrl;
        }

        public int getTextLength() {
            return textLength;
        }

        public void setTextLength(int textLength) {
            this.textLength = textLength;
        }
    }
}
