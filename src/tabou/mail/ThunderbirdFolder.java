/* Copyright(c) 2014 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.mail;

import javax.mail.Folder;
import javax.mail.Flags;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.*;
import java.util.*;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;

public class ThunderbirdFolder extends Folder{
    File inboxFile;
    List<Message> messages;
    private ThunderbirdFolder(){
        super(null);
    }
    public ThunderbirdFolder(Store store,String name){
        super(store);
        this.inboxFile = new File(name);
    }
/**
    Constructor that takes a Store object.

    Parameters:
        store - the Store that holds this folder
 */
    protected ThunderbirdFolder(Store store) {
        super(store);
    }
/**
    Returns the name of this Folder.

    Returns:
        name of the Folder
 */
@Override
public String getName(){
    return inboxFile.getName();
}

/**
    Returns the full name of this Folder.
    Returns:
        full name of the Folder
 */
@Override
public String getFullName(){
    return inboxFile.getName();
}

/**
    Returns the parent folder of this folder.
    Returns:
        Parent folder
    Throws:
        MessagingException
 */
@Override
public Folder getParent() throws MessagingException{
    return null;
}

/**

    Tests if this folder physically exists on the Store.

    Returns:
        true if the folder exists, otherwise false
    Throws:
        MessagingException - typically if the connection to the server is lost.
    See Also:
        create(int)
 */
@Override
public boolean exists() throws MessagingException{
    return inboxFile.exists();
}

/**

    Returns a list of Folders belonging to this Folder's namespace that match the specified pattern.
    Parameters:
        pattern - the match pattern
    Returns:
        array of matching Folder objects. An empty array is returned if no matching Folders exist.
    Throws:
        FolderNotFoundException - if this folder does not exist.
        MessagingException
    See Also:
        listSubscribed(java.lang.String)
 */
@Override
public Folder[] list(String pattern) throws MessagingException{
    return null;
}


/**

    Return the delimiter character that separates this Folder's pathname from the names of immediate subfolders.
    Returns:
        Hierarchy separator character
    Throws:
        FolderNotFoundException - if the implementation requires the folder to exist, but it does not
        MessagingException
 */
@Override
public char getSeparator() throws MessagingException{
    return ' ';
}

/**

    turns the type of this Folder, that is, whether this folder can hold messages or subfolders or both.

    Returns:
        integer with appropriate bits set
    Throws:
        FolderNotFoundException - if this folder does not exist.
        MessagingException
    See Also:
        HOLDS_FOLDERS, HOLDS_MESSAGES
 */
@Override
public int getType() throws MessagingException{
    return 0;
}

/**

    Create this folder on the Store.

    Parameters:
        type - The type of this folder.
    Returns:
        true if the creation succeeds, else false.
    Throws:
        MessagingException
    See Also:
        HOLDS_FOLDERS, HOLDS_MESSAGES, FolderEvent
 */
@Override
public boolean create(int type) throws MessagingException{
    return false;
}

/**

    Returns true if this Folder has new messages since the last time this indication was reset.
    Returns:
        true if the Store has new Messages
    Throws:
        FolderNotFoundException - if this folder does not exist.
        MessagingException
 */
@Override
public boolean hasNewMessages() throws MessagingException{
    return false;
}

/**

    Return the Folder object corresponding to the given name.
    Parameters:
        name - name of the Folder
    Returns:
        Folder object
    Throws:
        MessagingException
 */
@Override
public Folder getFolder(String name) throws MessagingException{
    ThunderbirdFolder folder = new ThunderbirdFolder(null);
    folder.inboxFile = new File(name);
    return folder;
}

/**

    Delete this Folder.
    Returns:
        true if the Folder is deleted successfully
    Throws:
        FolderNotFoundException - if this folder does not exist
        IllegalStateException - if this folder is not in the closed state.
        MessagingException
    See Also:
        FolderEvent
 */
@Override
public boolean delete(boolean recurse) throws MessagingException{
    return false;
}

/**

    Rename this Folder.

    Parameters:
        f - a folder representing the new name for this Folder
    Returns:
        true if the Folder is renamed successfully
    Throws:
        FolderNotFoundException - if this folder does not exist
        IllegalStateException - if this folder is not in the closed state.
        MessagingException
    See Also:
        FolderEvent
 */
@Override
public boolean renameTo(Folder f) throws MessagingException{
    return false;
}

/**

    Open this Folder.

    Parameters:
        mode - open the Folder READ_ONLY or READ_WRITE
    Throws:
        FolderNotFoundException - if this folder does not exist.
        IllegalStateException - if this folder is not in the closed state.
        MessagingException
    See Also:
        READ_ONLY, READ_WRITE, getType(), ConnectionEvent
 */
    @Override
    public void open(int mode) throws MessagingException{
        try{
            this.messages = readMessages();
        }catch(IOException e){
            throw new MessagingException("",e);
        }
    }

/**
    Close this Folder.

    Parameters:
        expunge - expunges all deleted messages if this flag is true
    Throws:
        IllegalStateException - if this folder is not opened
        MessagingException
    See Also:
        ConnectionEvent
 */
    @Override
    public void close(boolean expunge) throws MessagingException{
        this.messages = null;
    }

/**

    Indicates whether this Folder is in the 'open' state.

    Returns:
        true if this Folder is in the 'open' state.
 */
    @Override
    public boolean isOpen(){
        return !(this.messages == null);
    }
/**

    Get the permanent flags supported by this Folder.

    Returns:
        permanent flags, or null if not known
 */
@Override
public Flags getPermanentFlags(){
    return new Flags();
}

/**

    Get total number of messages in this Folder.
    Returns:
        total number of messages. -1 may be returned by certain implementations if this method is invoked on a closed folder.
    Throws:
        FolderNotFoundException - if this folder does not exist.
        MessagingException
 */
    @Override
    public int getMessageCount() throws MessagingException{
        return this.messages.size();
    }
/**

    Get the Message object corresponding to the given message number.

    Parameters:
        msgnum - the message number
    Returns:
        the Message object
    Throws:
        FolderNotFoundException - if this folder does not exist.
        IllegalStateException - if this folder is not opened
        java.lang.IndexOutOfBoundsException - if the message number is out of range.
        MessagingException
    See Also:
        getMessageCount(), fetch(javax.mail.Message[], javax.mail.FetchProfile)
 */
    @Override
    public Message getMessage(int msgnum) throws MessagingException{
        return this.messages.get(msgnum);
    }

/**

    Append given Messages to this folder.
    Parameters:
        msgs - array of Messages to be appended
    Throws:
        FolderNotFoundException - if this folder does not exist.
        MessagingException - if the append failed.
  */
@Override
public void appendMessages(Message[] msgs) throws MessagingException{
}

/**

    Expunge (permanently remove) messages marked DELETED.
    Returns:
        array of expunged Message objects
    Throws:
        FolderNotFoundException - if this folder does not exist
        IllegalStateException - if this folder is not opened.
        MessagingException
    See Also:
        Message#isExpunged, MessageCountEvent
 */
    @Override
    public Message[] expunge() throws MessagingException{
        return this.messages.toArray(new Message[0]);
    }
    private List<Message> readMessages() throws IOException,MessagingException{
        List<Message> messages = new ArrayList<Message>();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(inboxFile));
        int msgnum =0;
        while(true){
            String line = reader.readLine();
            if(line == null){
                if(sb.length() != 0){
                    MimeMessage message = new ThunderbirdMessage(this,new ByteArrayInputStream(sb.toString().getBytes()),msgnum);
//                    MimeMessage message = new MimeMessage(null,new ByteArrayInputStream(sb.toString().getBytes()));
                    messages.add(message);
                    msgnum++;
                }
                break;
            }
            if(line.startsWith("From - ")){
                if(sb.length() != 0){
                    MimeMessage message = new ThunderbirdMessage(this,new ByteArrayInputStream(sb.toString().getBytes()),msgnum);
//                    MimeMessage message = new MimeMessage(null,new ByteArrayInputStream(sb.toString().getBytes()));
                    messages.add(message);
                    msgnum++;
                }
                sb = new StringBuilder();
            }else{
                sb.append(line);
                sb.append("\n");
            }
        }
        return messages;
    }
    public static void main(String args[])throws Exception{
        Session session = Session.getDefaultInstance(new java.util.Properties());
        ThunderbirdStore ThunderbirdStore = new ThunderbirdStore(session,null);
        ThunderbirdFolder thunderbirdFolder = new ThunderbirdFolder(ThunderbirdStore,args[0]);
//        thunderbirdFolder.inboxFile = new File(args[0]);
        thunderbirdFolder.open(0);
        //List<Message> messages = thunderbirdFolder.readMessages();
        int msgnum =1;
        if(args.length > 1){
            msgnum = Integer.parseInt(args[1]);
            thunderbirdFolder.getMessage(msgnum).writeTo(System.out);
            return;
        }

        Message[] messages = thunderbirdFolder.expunge();
        for(Message message:messages){
            System.out.format("%3d %s\n",message.getMessageNumber(),message.getSubject());
//            System.out.format("%3d %s\n",msgnum,message.getSubject());
            msgnum++;
        }
    }
}
