package tabou.http;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

public class ServletOutputStreamImpl extends ServletOutputStream {
    private OutputStream out;
    @override
    public ServletOutputStreamImpl(OutputStream out) {
        this.out = out;
    }
    @override
    public void write(int b) throws IOException {
        out.write(b);
    }
    @override
    public void flush() throws IOException {
        out.flush();
    }
}
