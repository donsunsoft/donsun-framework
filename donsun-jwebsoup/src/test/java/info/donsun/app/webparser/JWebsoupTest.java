package info.donsun.app.webparser;

import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.JWebsoup;
import info.donsun.jwebsoup.impl.CookieImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JWebsoupTest {
    @Test
    public void testPost() throws IOException{
       Response r= JWebsoup.connect("http://localhost:9080/service/order/match_logistics_carriers_batch").data("orderIds[]", "7","orderIds[]", "8","logisticCarrier","1","logisticType","2").post();
    System.out.println(r);
    }

    @Test
    public void testMogujie() throws IOException {
        String url = "http://www.mogujie.com/settings/personal";

        Map<String, String> h = new HashMap<String, String>();
        h.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        h.put("Accept-Encoding", "gzip,deflate,sdch");
        h.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,en-US;q=0.4");
        h.put("Cache-Control", "max-age=0");
        h.put("Connection", "keep-alive");
//        h.put("Cookie", "__mgj_i_n=1; __mogujie=44Of36Jp3LH77IJzyp7adnnVjTlup%2BRmbTaVWDOZk%2BMN5vRHq2HFhdQLFT2S3c%2BREJdQCVkDKpH52c7ruiR%2Bug%3D%3D; __ud_=12e7wz4; __utma=1.496774242.1395486504.1395486504.1395497315.2; __utmb=1.3.10.1395497315; __utmc=1; __utmz=1.1395486504.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __mgjuuid=e36d56d5-dd5a-a00e-a63b-323a70f853be");
        h.put("Referer", "http://www.mogujie.com/");
        h.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");

        List<Cookie> cookies = new ArrayList<Cookie>();
        cookies.add(CookieImpl.create("__mgj_i_n", "1" ));
        cookies.add(CookieImpl.create("__mogujie", "44Of36Jp3LH77IJzyp7adnnVjTlup%2BRmbTaVWDOZk%2BMN5vRHq2HFhdQLFT2S3c%2BREJdQCVkDKpH52c7ruiR%2Bug%3D%3D" ));
        cookies.add(CookieImpl.create("__ud_", "12e7wz4" ));
        cookies.add(CookieImpl.create("__utma", "1.496774242.1395486504.1395486504.1395497315.2" ));
        cookies.add(CookieImpl.create("__utmb", "1.3.10.1395497315" ));
        cookies.add(CookieImpl.create("__utmc", "1" ));
        cookies.add(CookieImpl.create("__utmz", "1.1395486504.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)" ));
        cookies.add(CookieImpl.create("__mgjuuid", "e36d56d5-dd5a-a00e-a63b-323a70f853be" ));
        
        Response r = JWebsoup.connect(url).headers(h).cookies(cookies).get();
        Cookie cookie = r.cookie("__mgjuuid");
        System.out.println(r.body());
    }
}
