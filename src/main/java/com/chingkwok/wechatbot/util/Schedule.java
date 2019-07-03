package com.chingkwok.wechatbot.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class Schedule {
    private static final String hmbbUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=d1d23d26-f975-4971-a46d-faaa481510f3";
    private static final String pdxUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=eb637d36-a939-4323-ba10-350fee64ca37";
    private static final String widUrl = "http://apis.juhe.cn/simpleWeather/wids";

    @Scheduled(cron = "0 0 8,18 * * ?")
    public static void BotSpeakWeather() {
        String url = "http://apis.juhe.cn/simpleWeather/query?city=广州&key=c751c5271d41cd4f021af8fc1ca55867";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()
        ) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 E ahh时", Locale.CHINA);
            String nowtime = simpleDateFormat.format(new Date());
            nowtime = nowtime.replaceAll("PM", "下午");
            nowtime = nowtime.replaceAll("AM", "上午");
            HttpGet httpGet = new HttpGet(url);
            final CloseableHttpResponse res = httpClient.execute(httpGet);
            final HttpEntity entity = res.getEntity();
            if (entity != null) {
                final String s = EntityUtils.toString(entity, "utf-8");
                final JSONObject jsonObject = JSONObject.parseObject(s);
                final JSONObject result = jsonObject.getJSONObject("result");
                final String city = result.getString("city");
                final JSONObject realtime = result.getJSONObject("realtime");
                final String temperature = realtime.getString("temperature");
                final String humidity = realtime.getString("humidity");
                final String info = realtime.getString("info");
                final String direct = realtime.getString("direct");
                final String power = realtime.getString("power");
                final String aqi = realtime.getString("aqi");

                StringBuilder content = new StringBuilder("派带星天气时间~\n");
                content.append(nowtime + "\n");
                content.append(city + "天气 : " + info + "\n");

                if (!StringUtils.isBlank(temperature)) {
                    content.append("温度 : " + temperature + "°C" + "\n");
                }
                if (!StringUtils.isBlank(humidity)) {
                    content.append("湿度 : " + humidity + "%\n");
                }
                if (!StringUtils.isBlank(direct)) {
                    content.append("风向 : " + direct + "\n");
                }
                if (!StringUtils.isBlank(power)) {
                    content.append("风力 : " + power + "\n");
                }
                if (!StringUtils.isBlank(aqi)) {
                    content.append("空气指数 : " + aqi + " ");
                    int aqiInt = Integer.parseInt(aqi);
                    if (aqiInt >= 0 && aqiInt <= 50) {
                        content.append("良好 非常适合户外运动哦~~");
                    } else if (aqiInt <= 100) {
                        content.append("中等 可以多出去走走~~");
                    } else if (aqiInt <= 150) {
                        content.append("轻度污染 敏感人群建议不要外出哦~");
                    } else if (aqiInt <= 200) {
                        content.append("中度污染 空气质量不太好哦~");
                    } else if (aqiInt <= 300) {
                        content.append("重度污染 空气质量很差哦~没事就别外出了~");
                    } else {
                        content.append("严重污染 有钱搬家~没钱就买净化器吧~");
                    }
                }
                System.out.println(content.toString());
                SendMessage.execCurl(pdxUrl, content.toString(), SendMessage.TYPE_TEXT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0,30 12,18 * *")
    public void botTellEat()throws IOException{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ahh时mm分", Locale.CHINA);
        String nowtime = simpleDateFormat.format(new Date());
        String content = "海冕宝宝恰饭时间~~\n" +
                "现在时间" + nowtime + "\n"+
                "我准备好了你准备好了吗大伙们?\n" +
                "一起去吃蟹黄堡吧!!!\n";
        SendMessage.execCurl(hmbbUrl, content, SendMessage.TYPE_TEXT);
    }
}
