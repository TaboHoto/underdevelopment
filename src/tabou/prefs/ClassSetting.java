/* Copyright(c) 2014 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.prefs;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.prefs.Preferences;

import static tabou.log.TabouLog.Log;
import java.lang.reflect.Type;

/**
 */
public class ClassSetting {
    public Object parseType(String str,Object object) {
        if(object instanceof Integer){
            return Integer.parseInt(str);
        }
        if(object instanceof String){
            return str;
        }
        return null;
    }
    public void init(Class clazz) {
//        System.setProperty("user.home",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        Preferences preferences = Preferences.userNodeForPackage(clazz);
        for(Field field : clazz.getFields()){
            Annotation annotation = field.getAnnotation(Prefs.class);
            if(annotation == null){
                continue;
            }
            try {
				Object value = preferences.get(field.getName(), field.get(null).toString());
                Log.info(field.getName() + ":" + value);
				field.set(null,parseType(value.toString(),field.get(null)));
			} catch (IllegalAccessException e) {
                Log.warning(e.toString());
            } catch (IllegalArgumentException e) {
                Log.warning(e.toString());
            }
        }
    }
}
