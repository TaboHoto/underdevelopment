/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package tabou.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

/**
 * Based on the RequestBase code from Catalina.
 *
 * @author Craig R. McClanahan
 * @author James Strachan
 * @version $Revision$ $Date$
 */

public class ServletRequestImpl implements ServletRequest {
    // ----------------------------------------------------- Instance Variables

    /**
     * The attributes associated with this Request, keyed by attribute name.
     */
    protected HashMap<String,Object> attributes = new HashMap<String,Object>();

    /**
     * The authorization credentials sent with this Request.
     */
    protected String authorization = null;

    /**
     * The character encoding for this Request.
     */
    protected String characterEncoding = null;

    /**
     * The content length associated with this request.
     */
    protected int contentLength = -1;

    /**
     * The content type associated with this request.
     */
    protected String contentType = null;

    /**
     * The default Locale if none are specified.
     */
    protected static Locale defaultLocale = Locale.getDefault();

    /**
     * The input stream associated with this Request.
     */
    protected InputStream input = null;

    /**
     * The preferred Locales assocaited with this Request.
     */
    protected ArrayList<Locale> locales = new ArrayList<Locale>();

    /**
     * The protocol name and version associated with this Request.
     */
    protected String protocol = null;

    /**
     * The reader that has been returned by <code>getReader</code>, if any.
     */
    protected BufferedReader reader = null;

    /**
     * The remote address associated with this request.
     */
    protected String remoteAddr = null;

    /**
     * The fully qualified name of the remote host.
     */
    protected String remoteHost = null;

    /**
     * The scheme associated with this Request.
     */
    protected String scheme = null;

    /**
     * Was this request received on a secure connection?
     */
    protected boolean secure = false;

    /**
     * The server name associated with this Request.
     */
    protected String serverName = null;

    /**
     * The server port associated with this Request.
     */
    protected int serverPort = -1;

    /**
     * The ServletInputStream that has been returned by
     * <code>getInputStream()</code>, if any.
     */
    protected ServletInputStream stream = null;

    /**
     * The ServletContext which is used to dispatch further requests
     */
    protected ServletContext servletContext;
    
    public ServletRequestImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    // ------------------------------------------------- ServletRequest Methods

    /**
     * Return the specified request attribute if it exists; otherwise, return
     * <code>null</code>.
     *
     * @param name Name of the request attribute to return
     */
    @Override
    public Object getAttribute(String name) {
        synchronized (attributes) {
            return attributes.get(name);
        }
    }

    /**
     * Return the names of all request attributes for this Request, or an
     * empty <code>Enumeration</code> if there are none.
     */
    @Override
    public Enumeration getAttributeNames() {
        synchronized (attributes) {
//            return new IteratorEnumeration(attributes.keySet().iterator());
return null; //TODO
        }
    }

    /**
     * Return the character encoding for this Request.
     */
    @Override
    public String getCharacterEncoding() {
        if (characterEncoding== null) {
            characterEncoding= "ISO-8859-1";
        }
        return this.characterEncoding;
    }

    /**
     * Return the content length for this Request.
     */
    @Override
    public int getContentLength() {
        return this.contentLength;
    }

    /**
     * Return the content type for this Request.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * Return the servlet input stream for this Request.  The default
     * implementation returns a servlet input stream created by
     * <code>createInputStream()</code>.
     *
     * @exception IllegalStateException if <code>getReader()</code> has
     *  already been called for this request
     * @exception IOException if an input/output error occurs
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (reader != null) {
            throw new IllegalStateException( "getReader() has already been called" );
        }
//        if (stream == null)
//            stream = createInputStream();
        return stream;
    }

    /**
     * Return the preferred Locale that the client will accept content in,
     * based on the value for the first <code>Accept-Language</code> header
     * that was encountered.  If the request did not specify a preferred
     * language, the server's default Locale is returned.
     */
    @Override
    public Locale getLocale() {
        synchronized (locales) {
            if (locales.size() > 0)
                return (Locale) locales.get(0);
            else
                return defaultLocale;
        }
    }

    /**
     * Return the set of preferred Locales that the client will accept
     * content in, based on the values for any <code>Accept-Language</code>
     * headers that were encountered.  If the request did not specify a
     * preferred language, the server's default Locale is returned.
     */
    @Override
    public Enumeration getLocales() {
        synchronized (locales) {
            if (locales.size() > 0) {
//                return new IteratorEnumeration( locales.iterator() );
return null; //TODO
            }
        }
//        return new IteratorEnumeration( new SingletonIterator( defaultLocale ) );
return null; //TODO
    }

    /**
     * Return the value of the specified request parameter, if any; otherwise,
     * return <code>null</code>.  If there is more than one value defined,
     * return only the first one.
     *
     * @param name Name of the desired request parameter
     */
    @Override
    public String getParameter(String name) {
        String values[] = (String[]) getParameterMap().get(name);
        if (values != null)
            return (values[0]);
        else
            return (null);
    }

    /**
     * Return the defined values for the specified request parameter, if any;
     * otherwise, return <code>null</code>.
     *
     * @param name Name of the desired request parameter
     */
    @Override
    public String[] getParameterValues(String name) {
        String values[] = (String[]) getParameterMap().get(name);
        if (values != null)
            return (values);
        else
            return (null);
    }

    /**
     * Returns a <code>Map</code> of the parameters of this request.
     * Request parameters are extra information sent with the request.
     * For HTTP servlets, parameters are contained in the query string
     * or posted form data.
     *
     * @return A <code>Map</code> containing parameter names as keys
     *  and parameter values as map values.
     */
    @Override
    public Map getParameterMap() {
        return new HashMap();
    }

    /**
     * Return the names of all defined request parameters for this request.
     */
    @Override
    public Enumeration getParameterNames() {
//        return new IteratorEnumeration(getParameterMap().keySet().iterator());
return null; //TODO
    }
    

    /**
     * Return the protocol and version used to make this Request.
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Read the Reader wrapping the input stream for this Request.  The
     * default implementation wraps a <code>BufferedReader</code> around the
     * servlet input stream returned by <code>createInputStream()</code>.
     *
     * @exception IllegalStateException if <code>getInputStream()</code>
     *  has already been called for this request
     * @exception IOException if an input/output error occurs
     */
    @Override
    public BufferedReader getReader() throws IOException {
        if (stream != null) {
            throw new IllegalStateException( "getInputStream() has already been called" );
        }
        if (reader == null) {
            String encoding = getCharacterEncoding();
//            InputStreamReader isr =
//                new InputStreamReader(createInputStream(), encoding);
//            reader = new BufferedReader(isr);
        }
        return reader;
    }

    /**
     * Return the real path of the specified virtual path.
     *
     * @param path Path to be translated
     *
     * @deprecated As of version 2.1 of the Java Servlet API, use
     *  <code>ServletContext.getRealPath()</code>.
     */
    @Override
    public String getRealPath(String path) {
        if (servletContext == null)
            return (null);
        else {
            try {
                return (servletContext.getRealPath(path));
            } catch (IllegalArgumentException e) {
                return (null);
            }
        }
    }

    /**
     * Return the remote IP address making this Request.
     */
    @Override
    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    /**
     * Return the remote host name making this Request.
     */
    @Override
    public String getRemoteHost() {
        return this.remoteHost;
    }

    /**
     * Return a RequestDispatcher that wraps the resource at the specified
     * path, which may be interpreted as relative to the current request path.
     *
     * @param path Path of the resource to be wrapped
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return servletContext.getRequestDispatcher(path);
    }


    /**
     * Return the scheme used to make this Request.
     */
    @Override
    public String getScheme() {
        return this.scheme;
    }

    /**
     * Return the server name responding to this Request.
     */
    @Override
    public String getServerName() {
        return this.serverName;
    }

    /**
     * Return the server port responding to this Request.
     */
    @Override
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * Was this request received on a secure connection?
     */
    @Override
    public boolean isSecure() {
        return this.secure;
    }

    /**
     * Remove the specified request attribute if it exists.
     *
     * @param name Name of the request attribute to remove
     */
    @Override
    public void removeAttribute(String name) {
        synchronized (attributes) {
            attributes.remove(name);
        }
    }

    /**
     * Set the specified request attribute to the specified value.
     *
     * @param name Name of the request attribute to set
     * @param value The associated value
     */
    @Override
    public void setAttribute(String name, Object value) {
        // Name cannot be null
        if (name == null) {
            throw new IllegalArgumentException( "Attribute name cannot be null" );
        }
        // Null value is the same as removeAttribute()
        if (value == null) {
            removeAttribute(name);
            return;
        }
        synchronized (attributes) {
            attributes.put(name, value);
        }
    }

    /**
     * Overrides the name of the character encoding used in the body of
     * this request.  This method must be called prior to reading request
     * parameters or reading input using <code>getReader()</code>.
     *
     * @param enc The character encoding to be used
     *
     * @exception UnsupportedEncodingException if the specified encoding
     *  is not supported
     *
     * @since Servlet 2.3
     */
    @Override
    public void setCharacterEncoding(String enc)
        throws UnsupportedEncodingException {
        // Ensure that the specified encoding is valid
        byte buffer[] = new byte[1];
        buffer[0] = (byte) 'a';
        String dummy = new String(buffer, enc);
        // Save the validated encoding
        this.characterEncoding = enc;
    }
}
