/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.io;
import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer
{
    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
    }
}
