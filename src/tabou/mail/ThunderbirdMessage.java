/* Copyright(c) 2014 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.mail;

import javax.mail.Folder;
import javax.mail.Flags;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.*;
import java.util.*;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

public class ThunderbirdMessage extends MimeMessage{
    public ThunderbirdMessage(Folder folder, java.io.InputStream is,int msgnum) throws javax.mail.MessagingException{
        super(folder,is,msgnum);
    }
}
