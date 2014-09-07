/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
/* $Id$ */
package tabou.log;

import java.util.ServiceLoader;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;

/**
import static tabou.log.TabouLog.Log;
 */
public class TabouLog{
    public static Logger Log = Logger.getAnonymousLogger();

    public static void init(){
        StackTraceElement[]  stackTraceElements = Thread.currentThread().getStackTrace();
        String name = stackTraceElements[stackTraceElements.length -1].getClassName(); //class name of the caller
        int index = name.lastIndexOf('.');
        if(index > 0){
            name = name.substring(index +1);
        }
        Log = Logger.getLogger(name);
        ServiceLoader<Handler> codecSetLoader = ServiceLoader.load(Handler.class);
        int count = 0;
        for(Handler handler : codecSetLoader){
            Log.addHandler(handler);
            count++;
        }
        Log.setUseParentHandlers(false);
        if(count == 0){
            Handler handler = new ConsoleHandler();
            handler.setFormatter(new TabouFormatter());
            Log.addHandler(handler);
        }
    }
}
