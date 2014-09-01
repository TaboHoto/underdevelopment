/* Copyright(c) 2014 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.mail;

import javax.mail.*;

public class ThunderbirdStore extends Store{

    ThunderbirdStore(Session session, URLName urlname){
        super(session,urlname);
    }

/**
    Returns a Folder object that represents the 'root' of the default namespace presented to the user by the Store.

    Returns:
        the root Folder
    Throws:
        IllegalStateException - if this Store is not connected.
        MessagingException
 */
public Folder getDefaultFolder() throws MessagingException{
    return new ThunderbirdFolder(this,"inbox");
}

/**
    Return the Folder object corresponding to the given name.
    Parameters:
        name - The name of the Folder. In some Stores, name can be an absolute path if it starts with the hierarchy delimiter. Else it is
        interpreted relative to the 'root' of this namespace.
    Returns:
        Folder object
    Throws:
        IllegalStateException - if this Store is not connected.
        MessagingException
    See Also:
        Folder#exists, Folder#create
 */
public Folder getFolder(java.lang.String name) throws MessagingException{
    return new ThunderbirdFolder(this,name);
}

/**
    Return a closed Folder object, corresponding to the given URLName.
    Parameters:
        url - URLName that denotes a folder
    Returns:
        Folder object
    Throws:
        IllegalStateException - if this Store is not connected.
        MessagingException
    See Also:
        URLName
 */
public Folder getFolder(URLName url) throws MessagingException{
    return new ThunderbirdFolder(this,"inbox");
}
}
