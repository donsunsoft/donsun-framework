package info.donsun.app.activator;

import static org.junit.Assert.assertEquals;
import info.donsun.activator.model.Application;
import info.donsun.core.security.Digests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private XmlMapper mapper = new XmlMapper();

    private URL getResource(String file) {
        return this.getClass().getClassLoader().getResource(file);
    }

    @Test
    public void readXml() throws JsonParseException, JsonMappingException, IOException {
        Application tea = mapper.readValue(getResource("applicationContext.xml"), Application.class);

        tea.setProperty("fdafsa", "fwef");
        String a = tea.getProperty("fdafsa","d");
        assertEquals(1, tea.getModules().size());
    }

    @Test
    public void md5File() throws IOException {
        InputStream data = new FileInputStream(new File("E:\\wamp\\www\\moduletest\\ext\\test.txt"));
        String str = new String(Hex.encodeHex(Digests.md5(data)));

        System.out.println(str);
    }

    public void printProperty() {
        Enumeration<Object> en = System.getProperties().keys();
        while (en.hasMoreElements()) {
            Object obj = en.nextElement();
            System.out.print(obj + " ");
            System.out.println(System.getProperty(obj.toString()));
        }
    }
}
