/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.io;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

/**
 */
public class IoUtil {
    /**
     * read one line.
     */
    public static String readLine(InputStream in) throws IOException{
        ByteArrayOutputStream sb = new ByteArrayOutputStream();
        while(true){
            int c = in.read();
            if(c < 0){
                if(sb.size() == 0){
                    return null; //end
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
    /**
     * read one line.
     */
    public static String readLine(Reader in) throws IOException{
        StringBuilder sb = new StringBuilder();
        while(true){
            int c = in.read();
            if(c < 0){
                if(sb.toString().equals("")){
                    return null; //end
                }
                break;
            }else if(c == '\r'){
            }else if(c == '\n'){
                break;
            }else{
                sb.append((char)c);
            }
        }
        return sb.toString();
    }
    public static String readAll(Reader in) throws IOException{
        StringBuilder sb = new StringBuilder();
        while(true){
            int c = in.read();
            if(c < 0){
                break;
            }else{
                sb.append((char)c);
            }
        }
        return sb.toString();
    }
    public static void write(OutputStream out,String str) throws IOException{
        out.write(str.getBytes());
    }
    public static void copy(InputStream in,OutputStream out) throws IOException{
        while(true){
            int c = in.read();
            if(c < 0){   /* close */
                break;
            }
            out.write(c);
        }
    }
}
