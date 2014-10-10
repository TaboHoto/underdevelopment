package tabou.http;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;

public class ServletInputStreamImpl extends ServletInputStream {
    private final InputStream input;
    public ServletInputStreamImpl(final InputStream input) {
        this.input = input;
    }
    @Override
    public int read() throws IOException {
        return input.read();
    }
}
