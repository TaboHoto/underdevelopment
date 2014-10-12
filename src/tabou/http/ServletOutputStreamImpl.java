package tabou.http;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

public class ServletOutputStreamImpl extends ServletOutputStream {
    private OutputStream out;
    public ServletOutputStreamImpl(OutputStream out) {
        this.out = out;
    }
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
