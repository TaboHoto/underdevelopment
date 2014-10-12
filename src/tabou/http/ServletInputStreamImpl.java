package tabou.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
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
    /**
     * read line
     */
    public String readLine() throws IOException{
        ByteArrayOutputStream sb = new ByteArrayOutputStream();
        while(true){
            int c = read();
            if(c < 0){
                if(sb.size() == 0){
                    return null; //end of file
                }
                break;
            }else if(c == '\r'){
            }else if(c == '\n'){
                break;
            }else{
                sb.write(c);
            }
        }
        return sb.toString();
    }
}
