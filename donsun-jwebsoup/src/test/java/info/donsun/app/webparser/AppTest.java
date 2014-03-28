package info.donsun.app.webparser;

import static org.junit.Assert.assertTrue;
import info.donsun.app.webparser.vo.AppOptionVo;
import info.donsun.app.webparser.vo.UserVo;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.JWebsoup;
import info.donsun.jwebsoup.parser.HtmlDocumentParser;
import info.donsun.jwebsoup.parser.JsonNodeParser;
import info.donsun.jwebsoup.parser.JsonObjectParser;
import info.donsun.jwebsoup.parser.XmlDocumentParser;
import info.donsun.jwebsoup.util.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Test
    public void testCookies() throws IOException{
        Map<String, String> cookies = Utils.parseCookies("__mgjuuid=f0fa3013-0255-ef45-f6f8-002885caeb2b, __ud_=12e7wz4, __utma=1.573878161.1395668670.1395668670.1395668670.1, __utmc=1, __mgj_i_n=1, __utmb=1.0.10.1395668670, __mogujie=aIY1v3QA1hXTYQHGYBvQODMkpKey01BPFPusPBwI8zfpqWqfdnl7%2BzwTQg9%2FtXXXo6anNqt%2Fwn8Txl%2Fnek2jaw%3D%3D");
        Response r = JWebsoup.connect("http://www.mogujie.com/settings/personal").cookies(cookies).get();
        System.out.println(r);
    }
    
    @Test
    public void getImage() throws IOException {
        Response r = JWebsoup.connect("http://gtms04.alicdn.com/tps/i4/T1ocm.FrxcXXcI6BY_-280-280.png").get();
        byte[] b = r.bodyAsBytes();
        assertTrue(b.length == r.contentLength());
    }

    @Test
    public void testget() throws IOException {
        Document d = JWebsoup.connect("http://item.taobao.com/item.htm?spm=a230r.1.14.188.20DLgN&id=36698752305").get().parse(HtmlDocumentParser.create());

        System.out.println(d);
    }

    @Test
    public void chapiao_12306() throws IOException {
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/lcxxcx/query")
                .data("purpose_codes", "ADULT", "queryDate", "2014-01-19", "from_station", "SZQ", "to_station", "WHN").get();

        System.out.println(r);
    }

    @Test
    public void user_json() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));

        AppOptionVo[] user = mapper.reader(AppOptionVo[].class).readValue(AppTest.class.getResource("options.json"));
        System.out.println(user);
    }

    @Test
    public void jwebsoup_html() throws IOException {
        Response response = JWebsoup.connect("http://www.oschina.net/question/232352_65562").userAgent("IE").get();
        System.out.println(response.statusCode());

        Document d = response.parse(HtmlDocumentParser.create());

        Element e = d.body();
        Elements es = e.select("a[href*=com]");

        System.out.println(d);

    }

    @Test
    public void jwebsoup_post() throws IOException {
        List<NameValuePair> ll = new ArrayList<NameValuePair>();
        ll.add(new BasicNameValuePair("login_name", "sandwind"));
        ll.add(new BasicNameValuePair("password", "8755120805204e65176c0d217929fd04"));
        UrlEncodedFormEntity en = new UrlEncodedFormEntity(ll, "UTF-8");

        Response r = JWebsoup.connect("http://localhost:8080/orziiserver/api/user/login.do").entity(en).timeout(10000).post();
        System.out.println(r);

        Response r1 = JWebsoup.connect("http://localhost:8080/orziiserver/api/user/get_user.do").timeout(10000).cookies(r.cookies()).get();
        System.out.println(r1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));

        UserVo jn = r1.parse(new JsonObjectParser<UserVo>() {
        }.mapper(mapper));

        System.out.println(jn);

    }

    @Test
    public void jwebsoup_proxy() throws IOException {
        Response r = JWebsoup.connect("http://donsun.info/test3.php").proxy("116.112.66.102", 808).get();
        System.out.println(r);
    }

    @Test
    public void jwebsoup_12306() throws IOException {
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/index/init").proxy("116.112.66.102", 808).get();
        System.out.println(r);
    }

    @Test
    public void jwebsoup_xml() throws IOException {
        Response r = JWebsoup.connect("http://cn.wordpress.org/feed/").get();

        Document d = r.parse(XmlDocumentParser.create());

        Elements es = d.getAllElements();
        for (Element e : es) {

            System.out.println(e.tagName());
        }
    }

    @Test
    public void jwebsoup_json() throws IOException {
        Response r = JWebsoup.connect("http://localhost:8080/orziiserver/api/app/get_all_options.do").get();
        System.out.println(r.body());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));

        JsonNode jn = r.parse(JsonNodeParser.create().mapper(mapper));
        JsonNode apts = jn.get("app_options");
        JsonNode first = apts.get(0);
        System.out.println(first.get("option_value").asText());
    }

    @Test
    public void jwebsoup_json_object() throws IOException {
        Response r = JWebsoup.connect("http://localhost:8080/orziiserver/api/app/get_all_options.do").get();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));

        AppOptionVo vo = r.parse(new JsonObjectParser<AppOptionVo>() {
        }.mapper(mapper));

        System.out.println(vo);
    }

    @Test
    public void jsoup_html() throws IOException {

        Document dd = Jsoup.connect("http://www.oschina.net/question/232352_65562").userAgent("Mozilla").get();

        Element e = dd.body();
        Elements es = e.select("a[href*=com]");

        System.out.println(dd);
    }

    @Test
    public void jsoup_xml() throws IOException {
        Document d = Jsoup.connect("http://cn.wordpress.org/feed/").parser(Parser.xmlParser()).get();

        Elements es = d.getAllElements();
        for (Element e : es) {

            System.out.println(e.tagName());
        }
    }

    @Test
    public void jsoup_json() throws IOException {
        try {
            Document r = Jsoup.connect("http://localhost:8080/orziiserver/api/app/get_all_options.do").get();
            System.out.println(r.body());
        } catch (Exception e) {
            System.out.println("jsoup can not parse json.");
        }

    }

}
