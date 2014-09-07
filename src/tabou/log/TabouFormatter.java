/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.log;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

public class TabouFormatter extends Formatter{
    public String format(LogRecord record){
        //dateString = String.format("%tY%1$tm%1$td %1$tH%1$tM%1$tS",record.getMillis());
        String dateString = String.format("%1$tH%1$tM%1$tS",record.getMillis());
        Throwable thrown = record.getThrown();
        if(thrown == null){
            return String.format("%s %s %.1s/%s\n",dateString,
                record.getLoggerName(),record.getLevel().toString(),record.getMessage());
        }
        String message = String.format("%s %s %.1s/%s\n",dateString,
            record.getLoggerName(),record.getLevel().toString(),record.getMessage());
        for(Object stack : thrown.getStackTrace()){
            message += stack.toString() + "\n";
        }
        return message;
    }

}
