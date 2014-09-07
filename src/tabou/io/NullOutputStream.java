/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.io;
import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream
{
    @Override
    public void write(int c) throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void write(byte[] arg0, int arg1, int arg2) throws IOException {
    }

    @Override
    public void write(byte[] arg0) throws IOException {
    }
}
