package com.chingkwok.wechatbot.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class SendMessage {
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_MARKDOWN = "markdown";

    public enum Type {
        TEXT, MARKDOWN
    }

    public static String execCurl(String url, String content, String type) throws IOException {
//        HttpHost proxy = new HttpHost("localhost",8888);
//        RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(10000).setSocketTimeout(15000).build();

        CloseableHttpResponse response = null;
        try (CloseableHttpClient client = HttpClients.createDefault();
        ) {

            HttpPost post = new HttpPost(url);
            post.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
            String json = "   {\n" +
                    "        \"msgtype\": \""+type+"\",\n" +
                    "        \"text\": {\n" +
                    "            \"content\": \"" + content + "\"" +
                    "        }\n" +
                    "   }";

            StringEntity se = new StringEntity(json, Charset.forName("utf-8"));
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");//发送json需要设置contentType
            post.setEntity(se);
            response = client.execute(post);
//            return "!";
            //解析返回结果
            HttpEntity entity = response.getEntity();
//            System.out.println(response.getStatusLine().getStatusCode());
            if (entity != null) {
                String resStr = EntityUtils.toString(entity, "UTF-8");
                System.out.println(resStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "posted";

    }
}
