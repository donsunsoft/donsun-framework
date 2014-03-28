package info.donsun.core.security.rsa;

/**
 * 作者：张伟
 * 利用DES算法将流的数据进行加密
 */
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;



public class DESInputStream extends java.io.InputStream
{
    /** DES 加密对象 */
    private DES des = null;
    /** 一次读入的流 */
    private byte[] p = new byte[8];
    /** InputStream字节流的读入 */
    private InputStream in;
    /** 初始化时需要从新读入 */
    private int loc = 9;
    /** 结尾标记 */
    public static final byte  ENDEDCODE =(byte)100;
    /** 在变换意义的字节后 */
    private boolean changMeaning=false;
    /** 在8 直接块内 */
    private boolean inBlock=false;
/**
 * 按照desSet对流进行加密
 *
 * 
 */
public DESInputStream(InputStream inSet,DES desSet)
{
    super();
    des=desSet;
    in=inSet;
}
/**
 * 关闭本流
 */
public void close()throws IOException
{
    super.close();
    in.close();
}
/**
 * 根据输出流的协议读入字节
 * read 方法注释。
 */
public int read() throws java.io.IOException {
    while (true) {
        if (loc >= 8) {
            //////////////////////
            //
            //    读入字节，解密，
            ///////////////////////
            int len = in.read(p);
            if (len == -1)
                throw new EOFException("文件末尾");
            for (; len < 8; len++)
                p[len] = (byte) in.read();
            long s = des.bytes2long(p);
            long l = des.decrypt(s);
            des.long2bytes(l, p);
            loc = 0;
        }
        //////////////////////
        //
        //    判断是否是变换意义的字符100，
        //    如果是0表示本块结束，需要抛出异常结束
        //  read(byte[],int,int)函数
        ///////////////////////
        if (changMeaning) {
            changMeaning = false;
            if (p[loc] == 0) {
                loc = 8;
                if (inBlock) {
                    throw new IOException("END OF THIS BLOCK");
                }
                else
                    continue;
            }
            break;
        }
        else
            if (p[loc] == ENDEDCODE) {
                changMeaning = true;
                loc++;
                continue;
            }
            else
                break;
    }
    byte pb = p[loc++];
    int pi = pb & 0xff;
    return pi;
}
public int read(byte b[]) throws IOException
{
    return read(b, 0, b.length);
}
/**
 *
 *    @see java.io.InputStream
 *
 */
public int read(byte b[], int off, int len) throws IOException {
    if (len <= 0) {
        return 0;
    }
    int c = read();
    if (c == -1) {
        return -1;
    }
    inBlock = true;
    b[off] = (byte) c;
    int i = 1;
    try {
        for (; i < len; i++) {
            c = read();
            if (c == -1) {
                break;
            }
            if (b != null) {
                b[off + i] = (byte) c;
            }
        }
    }
    catch (IOException ee) {
    }
    inBlock = false;
    return i;
}
}