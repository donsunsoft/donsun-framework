package info.donsun.core.security.rsa;

/**
 * 100 0 表示一个流结束
 * 100 100表示 100
 */
import java.io.IOException;
import java.io.OutputStream;


public class DESOutputStream extends java.io.OutputStream
{
    private DES des = null;
    private OutputStream out;
    private byte p[] = new byte[8];
    private int loc = 0;
/**
 * DESStream 构造子
 * @param outSet OutputStream    输出流
 * @param desSet DES     加密对象
 */
public DESOutputStream(OutputStream outSet, DES desSet) {
    super();
    des = desSet;
    out = outSet;
}
/**
 * 关闭
 */
public void close() throws IOException
{
    writeOUT();
    out.close();
}
/**
 * 本方法由  张伟 创建
 * 创建时间：
 * 完成的功能：????
 * 修改记录：   100 0 表示一个流结束
 */
public void flush() throws IOException {
    p[loc++] = 100;
    if (loc == 8) {
        writeOUT();
    }
    p[loc++] = 0;
    writeOUT();
}
/**
 * 写出一个字节
 * 对变换意义直接进行处理
 * 100出现的概率相对第一些
 * write 方法注释。
 */
public void write(int b) throws java.io.IOException {
    if (b == DESInputStream.ENDEDCODE) {
        p[loc++] = (byte) (b & 0xff);
        if (loc == 8)
            writeOUT();
        p[loc++] = (byte) (b & 0xff);
    }
    else
        p[loc++] = (byte) (b & 0xff);
    if (loc == 8) {
        writeOUT();
        return;
    }
}
/**
 * 对8个字节进行加密写出
 */
public void writeOUT() throws IOException
{
    if (loc < 8)
        p[loc++] = DESInputStream.ENDEDCODE;
    long l = des.encrypt(des.bytes2long(p));
    des.long2bytes(l, p);
    out.write(p);
    loc = 0;
    out.flush();
}
}