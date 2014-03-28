/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package info.donsun.app.webparser;

import info.donsun.jwebsoup.Connection.Cookie;
import info.donsun.jwebsoup.Connection.KeyVal;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.JWebsoup;
import info.donsun.jwebsoup.impl.CookieImpl;
import info.donsun.jwebsoup.impl.KeyValImpl;
import info.donsun.jwebsoup.parser.HtmlDocumentParser;
import info.donsun.jwebsoup.parser.JsonNodeParser;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Steven Deng
 * @version 1.0
 * @since 1.0
 * @date 2013-12-31
 * 
 */
public class QiangpiaoTest {

    // 用户名
    private static final String USERNAME = "dd_whu";
    // 密码
    private static final String PASSWORD = "19850430";

    // 乘车日期
    private static final String TRAIN_DATE = "2014-01-21";

    // 返程日期
    private static final String RETURN_TRAIN_DATE = "2014-01-02";

    // 出发城市
    private static final String FROM_CITY = "武汉";

    // 触发城市代码
    private static final String FROM_CITY_CODE = "WHN";

    // 到达城市
    private static final String TO_CITY = "长沙";

    // 到达城市代码
    private static final String TO_CITY_CODE = "CSQ";

    // 我要订的车次
    private static final String TRAIN_CODE = "T89";

    // 乘车人
    private static final String PASSENGER_NAME = "邓冬冬";

    // 如果你已经保存了cookie，可以跳过登录
    private static final String CACHED_COOKIE = "";

    private static Collection<Cookie> cookies = null;

    public static void main(String[] args) throws IOException {
        QiangpiaoTest t = new QiangpiaoTest();
        // 登录
        if (CACHED_COOKIE == null || CACHED_COOKIE.length() == 0) {
            t.login();
        }

        // 订票
        t.order();
    }

    private void login() throws IOException {
        // 开始登陆
        login_init();
        // 获取验证码
        show_checkcode();

        // 看清楚图片，在控制台输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入验证码:");
        String code = br.readLine();
        System.out.println(code);

        // 检查验证码
        if (!check_code(code)) {
            System.out.println("验证码错了");
            System.exit(0);
        }

        // 登录请求
        if (!request_login(USERNAME, PASSWORD, code)) {
            System.out.println("登录失败");
            System.exit(0);
        }

        System.out.println("哈哈，登录成功！");

        // 查看个人信息，看cookie是否有效果
        // my_profile();

    }

    /**
     * 订票
     * 
     * @throws IOException
     */
    private void order() throws IOException {
        List<TrainInfo> result = search_ticket();
        if (result == null) {
            System.out.println("没有票");
            System.exit(0);
        }

        // 检查用户
        if (!check_user()) {
            System.out.println("检查用户失败");
            System.exit(0);
        }

        String secretStr = null;
        for (TrainInfo info : result) {
            if (TRAIN_CODE.equals(info.getTrainCode())) {
                secretStr = info.getSecretStr();
                break;
            }
        }

        if (secretStr == null) {
            System.out.println("没有我需要的车次");
            System.exit(0);
        }

        // 订票请求
        if (!request_order(secretStr, TRAIN_DATE, RETURN_TRAIN_DATE, FROM_CITY, TO_CITY)) {
            System.out.println("订票请求失败");
            // System.exit(0);
        }

        // 验证乘车人
        String randCode = confirmPassenger();

        // 获取乘车人列表
        List<Passenger> passengers = search_passengers(randCode);
        if (passengers == null) {
            System.out.println("没有维护乘车人信息");
            System.exit(0);
        }

        Passenger pp = null;
        for (Passenger p : passengers) {
            if (PASSENGER_NAME.equalsIgnoreCase(p.getPassengerName())) {
                pp = p;
                break;
            }
        }

        if (pp == null) {
            System.out.println("没有找到指定乘车人：" + PASSENGER_NAME);
            System.exit(0);
        }

        // 显示订票验证码
        show_order_checkcode();

        // 看清楚图片，在控制台输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入验证码:");
        String code = br.readLine();
        System.out.println(code);

        // 检查验证码
        if (!check_order_checkcode(code, randCode)) {
            System.out.println("验证码错了");
            System.exit(0);
        }

    }

    /**
     * @param code
     * @return
     * @throws IOException
     */
    private boolean check_order_checkcode(String code, String token) throws IOException {
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn").cookies(cookies)
                .data("randCode", code, "rand", "randp", "_json_att", "", "REPEAT_SUBMIT_TOKEN", token).referrer("https://kyfw.12306.cn/otn/confirmPassenger/initDc")
                .timeout(10000).post();
        System.out.println(r);
        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("data")) {
                return "Y".equalsIgnoreCase(node.get("data").asText());
            }
        }
        return false;
    }

    private List<Passenger> search_passengers(String randCode) throws IOException {
        Map<String, String> data = new HashMap<String, String>();
        data.put("_json_att", "");
        data.put("REPEAT_SUBMIT_TOKEN", randCode);

        // headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Origin", "https://kyfw.12306.cn");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

        Response r = JWebsoup.connect("https://kyfw.12306.cn//otn/confirmPassenger/getPassengerDTOs").headers(headers).cookies(cookies)
                .referrer("https://kyfw.12306.cn/otn/leftTicket/init").timeout(10000).data(data).post();

        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("data")) {
                JsonNode jn = node.get("data");
                if (jn != null && jn.has("normal_passengers")) {
                    JsonNode np = jn.get("normal_passengers");
                    if (np.isArray()) {
                        List<Passenger> list = new ArrayList<Passenger>();
                        Iterator<JsonNode> it = np.elements();
                        while (it.hasNext()) {
                            JsonNode p = it.next();
                            list.add(new Passenger(p.get("passenger_name").asText(), p.get("passenger_id_no").asText()));
                        }

                        return list;
                    }
                }
            }

        }

        return null;

    }

    /**
     * @return
     * @throws IOException
     */
    private String confirmPassenger() throws IOException {
        Map<String, String> data = new HashMap<String, String>();
        data.put("_json_att", "");

        // headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Origin", "https://kyfw.12306.cn");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/confirmPassenger/initDc").headers(headers).cookies(cookies).referrer("https://kyfw.12306.cn/otn/leftTicket/init")
                .timeout(10000).data(data).post();
        if (r.statusCode() == 200) {
            Document doc = r.parse(HtmlDocumentParser.create());
            // 从head中取出globalRepeatSubmitToken
            Element element = doc.head();
            Element es = element.select("head > script").get(0);

            String str = es.toString();
            int pos = str.indexOf("globalRepeatSubmitToken");
            if (pos > 0) {
                int start = str.indexOf("\'", pos);
                int end = str.indexOf("\'", start + 1);
                if (end > start) {
                    return str.substring(start + 1, end);
                }
            }

        }

        return null;
    }

    /**
     * 订票请求
     * 
     * @throws IOException
     */
    private boolean request_order(String secretStr, String trainDate, String backTrainDate, String from, String to) throws IOException {

        List<KeyVal> data = new ArrayList<KeyVal>();
        data.add(KeyValImpl.create("secretStr", URLDecoder.decode(secretStr, "UTF-8")));
        data.add(KeyValImpl.create("train_date", trainDate));
        data.add(KeyValImpl.create("back_train_date", backTrainDate));
        data.add(KeyValImpl.create("tour_flag", "dc"));
        data.add(KeyValImpl.create("purpose_codes", "ADULT"));
        data.add(KeyValImpl.create("query_from_station_name", from));
        data.add(KeyValImpl.create("query_to_station_name", to));
        data.add(KeyValImpl.create("undefined", ""));

        // headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Origin", "https://kyfw.12306.cn");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest").headers(headers).cookies(cookies)
                .referrer("https://kyfw.12306.cn/otn/leftTicket/init").timeout(10000).data(data).post();
        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("messages")) {
                JsonNode jn = node.get("messages");
                return jn.isNull();
            }
        }

        return false;

    }

    /**
     * 登录验证码
     * 
     * @throws IOException
     */
    private void show_checkcode() throws IOException {
        // 获取验证码
        double rand = Math.random();
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew.do?module=login&rand=sjrand&" + rand).cookies(cookies).timeout(10000).get();

        if (r.statusCode() != 200) {
            System.out.println("请求失败");
            System.exit(0);
        }

        // 用swing显示图片
        ImageIcon img = new ImageIcon(r.bodyAsBytes());
        img.setImage(img.getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT));

        JFrame frame = new JFrame();
        int w = (Toolkit.getDefaultToolkit().getScreenSize().width) / 2;
        int h = (Toolkit.getDefaultToolkit().getScreenSize().height) / 2;
        frame.setLocation(w, h);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel();
        label.setIcon(img);
        frame.add(label);
        frame.setSize(400, 300);
        frame.pack();
        frame.setVisible(true);
    }

    private void show_order_checkcode() throws IOException {
        // 获取验证码
        double rand = Math.random();
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn").cookies(cookies).timeout(10000).get();

        if (r.statusCode() != 200) {
            System.out.println("请求失败");
            System.exit(0);
        }

        // 用swing显示图片
        ImageIcon img = new ImageIcon(r.bodyAsBytes());
        img.setImage(img.getImage().getScaledInstance(63, 30, Image.SCALE_DEFAULT));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel();
        label.setIcon(img);
        frame.add(label);
        frame.setSize(400, 300);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * 登陆前发一次请求
     * 
     * @throws IOException
     */
    private void login_init() throws IOException {
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/login/init").referrer("https://kyfw.12306.cn/otn/index/init")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").timeout(10000).execute();

        cookies = r.cookies();

        System.out.println("login init:" + r.statusCode());
    }

    /**
     * 检查验证码
     * 
     * @param code
     * @throws IOException
     */
    private boolean check_code(String code) throws IOException {

        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn").cookies(cookies).data("randCode", code, "rand", "sjrand").timeout(10000).post();
        System.out.println(r);
        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("data")) {
                return "Y".equalsIgnoreCase(node.get("data").asText());
            }
        }
        return false;
    }

    /**
     * 发起登录请求
     * 
     * @throws IOException
     */
    private boolean request_login(String username, String password, String code) throws IOException {
        // loginUserDTO.user_name=dd_whu&userDTO.password=19850430&randCode=rkmk
        Map<String, String> data = new HashMap<String, String>();
        data.put("loginUserDTO.user_name", username);
        data.put("userDTO.password", password);
        data.put("randCode", code);

        // headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Origin", "https://kyfw.12306.cn");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/login/loginAysnSuggest").timeout(10000).cookies(cookies).referrer("https://kyfw.12306.cn/otn/login/init")
                .headers(headers).data(data).post();
        System.out.println(r);

        // cookies = r.cookies();

        // {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"data":{"loginCheck":"Y"},"messages":[],"validateMessages":{}}

        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("data")) {
                JsonNode jn = node.get("data");
                if (jn.has("loginCheck")) {
                    return "Y".equalsIgnoreCase(jn.get("loginCheck").asText());
                }

            }
        }

        return false;
    }

    /**
     * 查看个人信息
     * 
     * @throws IOException
     */
    private void my_profile() throws IOException {

        // JSESSIONID=FA759A7E6BF4A60989D3A2C961232C15;
        // BIGipServerotn=2698248458.38945.0000

        // Cookie c1 = CookieImpl.create("JSESSIONID",
        // "FA759A7E6BF4A60989D3A2C961232C15", "kyfw.12306.cn", "/", null);
        // Cookie c2 = CookieImpl.create("BIGipServerotn",
        // "2698248458.38945.0000", "kyfw.12306.cn", "/", null);
        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/modifyUser/initQueryUserInfo").timeout(10000).cookies(cookies).get();
        System.out.println(r);
    }

    /**
     * 检查用户
     * 
     * @throws IOException
     */
    private boolean check_user() throws IOException {
        // headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Origin", "https://kyfw.12306.cn");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");

        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/login/checkUser").cookies(cookies).data("_json_att", "").referrer("https://kyfw.12306.cn/otn/leftTicket/init")
                .timeout(10000).post();
        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("data")) {
                JsonNode data = node.get("data");
                if (data.has("flag")) {
                    System.out.println("check user pass");
                    return data.get("flag").asBoolean();
                }
            }
        }

        return false;
    }

    /**
     * 查询余票
     * 
     * @throws IOException
     */
    private List<TrainInfo> search_ticket() throws IOException {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("If-Modified-Since", "0");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        headers.put("Cache-Control", "no-cache");
        headers.put("Connection", "keep-alive");

        cookies.add(CookieImpl.create("_jc_save_fromStation", "%u6DF1%u5733%2CSZQ"));
        cookies.add(CookieImpl.create("_jc_save_toStation", "%u6B66%u6C49%2CHKN"));
        cookies.add(CookieImpl.create("_jc_save_fromDate", TRAIN_DATE));
        cookies.add(CookieImpl.create("_jc_save_toDate", RETURN_TRAIN_DATE));
        cookies.add(CookieImpl.create("_jc_save_wfdc_flag", "dc"));

        // Cookie:JSESSIONID=8733F3082957937A801670901644D088;
        // BIGipServerotn=2144600330.50210.0000;
        // _jc_save_fromStation=%u6DF1%u5733%2CSZQ;
        // _jc_save_toStation=%u6B66%u6C49%2CHKN; _jc_save_fromDate=2014-01-19;
        // _jc_save_toDate=2013-12-31; _jc_save_wfdc_flag=dc

        List<KeyVal> data = new ArrayList<KeyVal>();
        data.add(KeyValImpl.create("leftTicketDTO.train_date", TRAIN_DATE));
        data.add(KeyValImpl.create("leftTicketDTO.from_station", FROM_CITY_CODE));
        data.add(KeyValImpl.create("leftTicketDTO.to_station", TO_CITY_CODE));
        data.add(KeyValImpl.create("purpose_codes", "ADULT"));

        Response r = JWebsoup.connect("https://kyfw.12306.cn/otn/leftTicket/query").data(data).headers(headers).cookies(cookies)
                .referrer("https://kyfw.12306.cn/otn/leftTicket/init").timeout(10000).get();

        if (r.statusCode() == 200) {
            JsonNode node = r.parse(JsonNodeParser.create());
            if (node.has("data")) {
                JsonNode jn = node.get("data");
                if (jn.isArray()) {
                    List<TrainInfo> result = new ArrayList<TrainInfo>();
                    Iterator<JsonNode> it = jn.elements();
                    while (it.hasNext()) {
                        JsonNode n = it.next();
                        String secretStr = n.get("secretStr").asText();

                        JsonNode dto = n.get("queryLeftNewDTO");
                        String trainNo = dto.get("station_train_code").asText();

                        result.add(new TrainInfo(trainNo, secretStr));
                    }

                    return result;
                }
            }
        }

        return null;

    }

    public static class TrainInfo {

        private String trainCode;
        private String secretStr;

        /**
         * @param trainCode
         * @param secretStr
         */
        public TrainInfo(String trainCode, String secretStr) {
            this.trainCode = trainCode;
            this.secretStr = secretStr;
        }

        public String getTrainCode() {
            return trainCode;
        }

        public void setTrainCode(String trainCode) {
            this.trainCode = trainCode;
        }

        public String getSecretStr() {
            return secretStr;
        }

        public void setSecretStr(String secretStr) {
            this.secretStr = secretStr;
        }

    }

    public static class Passenger {
        private String passengerName;
        private String passengerIdNo;

        public Passenger(String passengerName, String passengerIdNo) {
            this.passengerName = passengerName;
            this.passengerIdNo = passengerIdNo;
        }

        public String getPassengerName() {
            return passengerName;
        }

        public void setPassengerName(String passengerName) {
            this.passengerName = passengerName;
        }

        public String getPassengerIdNo() {
            return passengerIdNo;
        }

        public void setPassengerIdNo(String passengerIdNo) {
            this.passengerIdNo = passengerIdNo;
        }

    }
}
