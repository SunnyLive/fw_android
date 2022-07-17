package com.fengwo.module_flirt.IM.bean;

/**
 * @Author BLCS
 * @Time 2020/5/4 14:21
 */
public class AcceptOrderBean {

    /**
     * action : acceptOrder
     * result : {"accept":1}
     * content : {"type":"text","value":"主播已接单"}
     */

    private String action;
    private ResultBean result;
    private ContentBean content;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ResultBean {
        /**
         * accept : 1
         */

        private int accept;

        public int getAccept() {
            return accept;
        }

        public void setAccept(int accept) {
            this.accept = accept;
        }
    }

    public static class ContentBean {
        /**
         * type : text
         * value : 主播已接单
         */

        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
