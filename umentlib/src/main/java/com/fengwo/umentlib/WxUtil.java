//package com.fengwo.umentlib;
//
//
//import androidx.annotation.RequiresApi;
//
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import javax.xml.parsers.DocumentBuilder;
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.util.*;
//
///**
// * @author phoebe
// */
//public class WxUtil {
//
//    private static final String FAIL     = "FAIL";
//    private static final String SUCCESS  = "SUCCESS";
//    private static final String FIELD_SIGN = "sign";
//
//    public static String removeDot(String money){
//        String dot=".";
//        String zero="0";
//        int i = money.indexOf(dot);
//        if(i<0){
//            return money+zero+zero;
//        }
//        if(money.endsWith(dot)){
//            return money.substring(0,money.length()-1)+zero+zero;
//        }
//        String[] split = money.split("\\.");
//        split[1]+=zero+zero;
//        String s = split[0] + split[1].substring(0, 2);
//        while (s.startsWith(zero)){
//            s=s.substring(1);
//        }
//        return s;
//    }
//
//    public static String createRequestXml(SortedMap<String, String> parameters) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<xml>");
//        for (Map.Entry<String,String> entry : parameters.entrySet()) {
//            String k = entry.getKey();
//            String v = entry.getValue();
//            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
//                sb.append("<").append(k).append(">").append("<![CDATA[").append(v).append("]]></").append(k).append(">");
//            } else {
//                sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
//            }
//        }
//        sb.append("</xml>");
//        return sb.toString();
//    }
//
//    public static String createSign(String characterEncoding, SortedMap<String, String> packageParams, String apiKey) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String,String> entry : packageParams.entrySet()) {
//            String k = entry.getKey();
//            String v = entry.getValue();
//            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
//                sb.append(k).append("=").append(v).append("&");
//            }
//        }
//        sb.append("key=").append(apiKey);
//        return md5Encode(sb.toString(), characterEncoding).toUpperCase();
//    }
//
////    public static Map<String, String> processResponseXml(String xmlStr) throws Exception {
////        String returnCodeKey = "return_code";
////        String returnCode;
////        Map<String, String> respData = xmlToMap(xmlStr);
////        if (respData.containsKey(returnCodeKey)) {
////            returnCode = respData.get(returnCodeKey);
////        }
////        else {
////            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
////        }
////
////        if (returnCode.equals(FAIL)) {
////            return respData;
////        }
////        else if (returnCode.equals(SUCCESS)) {
////            if (isResponseSignatureValid(respData)) {
////                return respData;
////            }
////            else {
////                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
////            }
////        }
////        else {
////            throw new Exception(String.format("return_code value %s is invalid in XML: %s", returnCode, xmlStr));
////        }
////    }
//
////    private static Map<String, String> xmlToMap(String strXml) throws Exception {
////        try {
////            Map<String, String> data = new HashMap<>(32);
////            DocumentBuilder documentBuilder = WxPayXmlUtil.newDocumentBuilder();
////            InputStream stream = new ByteArrayInputStream(strXml.getBytes(StandardCharsets.UTF_8));
////            org.w3c.dom.Document doc = documentBuilder.parse(stream);
////            doc.getDocumentElement().normalize();
////            NodeList nodeList = doc.getDocumentElement().getChildNodes();
////            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
////                Node node = nodeList.item(idx);
////                if (node.getNodeType() == Node.ELEMENT_NODE) {
////                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
////                    data.put(element.getNodeName(), element.getTextContent());
////                }
////            }
////            try {
////                stream.close();
////            } catch (Exception ex) {
////                // do nothing
////            }
////            return data;
////        } catch (Exception ex) {
////            log.warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXml);
////            throw ex;
////        }
////
////    }
//
//    private static boolean isResponseSignatureValid(Map<String, String> data) throws Exception {
//        if (!data.containsKey(FIELD_SIGN) ) {
//            return false;
//        }
//        String sign = data.get(FIELD_SIGN);
//        return generateSignature(data).equals(sign);
//    }
//
//    private static String generateSignature(final Map<String, String> data) throws Exception {
//        Set<String> keySet = data.keySet();
//        String[] keyArray = keySet.toArray(new String[data.size()]);
//        Arrays.sort(keyArray);
//        StringBuilder sb = new StringBuilder();
//        for (String k : keyArray) {
//            if (k.equals(FIELD_SIGN)) {
//                continue;
//            }
//            // 参数值为空，则不参与签名
//            if (data.get(k).trim().length() > 0) {
//                sb.append(k).append("=").append(data.get(k).trim()).append("&");
//            }
//        }
//        sb.append("key=").append(WxPayConstants.API_KEY);
//        return md5(sb.toString()).toUpperCase();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private static String md5(String data) throws Exception {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
//        StringBuilder sb = new StringBuilder();
//        for (byte item : array) {
//            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
//        }
//        return sb.toString().toUpperCase();
//    }
//
//    private static String md5Encode(String origin, String charsetname) {
//        String resultString = null;
//        try {
//            resultString = origin;
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            if (charsetname == null || "".equals(charsetname)){
//                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
//            }
//            else{
//                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
//            }
//        } catch (Exception e) {
//            L.error("MD5编码失败",e);
//        }
//        return resultString;
//    }
//
//    private static String byteArrayToHexString(byte[] b) {
//        StringBuilder resultSb = new StringBuilder();
//        for (byte value : b) {
//            resultSb.append(byteToHexString(value));
//        }
//        return resultSb.toString();
//    }
//
//    private static String byteToHexString(byte b) {
//        int n = b;
//        if (n < 0){
//            n += 256;
//        }
//        int d1 = n / 16;
//        int d2 = n % 16;
//        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
//    }
//
//    private static final String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
//
//}
