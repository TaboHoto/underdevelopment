/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.log;
import java.util.logging.*;
import android.util.Log;

public class AndlordLogHandler extends Handler{
    public void publish(LogRecord record){
        if(record.getLevel().equals(Level.SEVERE)){
            Log.e(record.getLoggerName(), record.getMessage(),record.getThrown()); // Send a DEBUG log message and log the exception.
        }else{
            Log.d(record.getLoggerName(), record.getMessage(),record.getThrown()); // Send a DEBUG log message and log the exception.
        }
    }
    public void flush(){
    }
    public void close() throws SecurityException{
    }
}
