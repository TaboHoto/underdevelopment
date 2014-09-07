/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.http;
import java.io.IOException;
import java.io.InputStream;
import tabou.io.IoUtil;

public class ChunkInputStream extends InputStream {
    int size  = 0;
    int point = 0;
    InputStream in;
    boolean eof = false;

    public ChunkInputStream(InputStream in){
        this.in = in;
    }
    @Override
    public int read() throws IOException{
        if(eof){
            //System.err.println("eof:");
            return -1;
        }
        if(size == point){
            if(size != 0){ //not first
                IoUtil.readLine(in);
            }
            String sizeString = IoUtil.readLine(in).trim();
            size = Integer.parseInt(sizeString,16);
            //System.err.println(size + ":" + point);
            point = 0;
            if(size == 0){
                eof = true;
                return -1;
            }
        }
        point++;
        return in.read();
    }
    @Override
    public void close() throws IOException {
        in.close();
    }
}
