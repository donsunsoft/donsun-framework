package info.donsun.browser;

import static org.junit.Assert.assertTrue;
import info.donsun.browser.executor.NativeSwingExecutor;
import info.donsun.jwebsoup.Connection.Response;
import info.donsun.jwebsoup.JWebsoup;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class JBrowserTest {
    @Test
    public void dateCount() throws ParseException {
        Date dd = DateUtils.parseDate("2014-03-27 12:11:11", "yyyy-MM-dd hh:mm:ss");
        long millis = dd.getTime() - System.currentTimeMillis();
        BigDecimal d = new BigDecimal(millis);
        BigDecimal s = new BigDecimal(24 * 60 * 60 * 1000);

        MathContext mc = new MathContext(8, RoundingMode.HALF_DOWN);
        BigDecimal r = d.divide(s, mc);
        BigDecimal rr = r.multiply(s);
        System.out.println(d);
    }

    @Test
    public void testImage() throws IOException {
        Response r = JWebsoup.connect("http://gtms04.alicdn.com/tps/i4/T1ocm.FrxcXXcI6BY_-280-280.png").timeout(30000).executor(NativeSwingExecutor.create()).get();
        byte[] b = r.bodyAsBytes();
        assertTrue(b.length == r.contentLength());
    }

    @Test
    public void testMogujie() throws IOException {
        Response r = JWebsoup.connect("http://localhost:8080/orziiserver/api/user/login.do").timeout(30000).data("login_name", "zara3", "password", "password")
                .executor(NativeSwingExecutor.create()).post();
        System.out.println(r.statusCode());

    }

    @Test
    public void testTaobao() throws IOException {
        long start = System.currentTimeMillis();
        int c = 0;
        for (int i = 0; i < 10; i++) {
            Response r = JWebsoup.connect("https://www.mogujie.com/login/").executor(NativeSwingExecutor.create()).get();
            if(r.statusCode() == 200){
                c++;
            }
            System.out.println(r);
            
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(c);
    }

}
