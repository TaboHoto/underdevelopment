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


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


/**
 * Based on the HttpRequestBase code from Catalina.
 *
 * @author Craig R. McClanahan
 * @author James Strachan
 * @version $Revision$ $Date$
 */

public class HttpServletResponseImpl extends ServletResponseImpl implements HttpServletResponse {

    /**
     * The set of Cookies associated with this Response.
     */
    protected ArrayList<Cookie> cookies = new ArrayList<Cookie>();


    /**
     * The date format we will use for creating date headers.
     */
    protected static final SimpleDateFormat format =
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.US);
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    };


    /**
     * The HTTP headers explicitly added via addHeader(), but not including
     * those to be added with setContentLength(), setContentType(), and so on.
     * This collection is keyed by the header name, and the elements are
     * ArrayLists containing the associated values that have been set.
     */
    protected HashMap<String,List<String>> headers = new HashMap<String,List<String>>();


    /**
     * The HTTP status code associated with this Response.
     */
    protected int status = HttpServletResponse.SC_OK;


    /**
     * The time zone with which to construct date headers.
     */
    protected static final TimeZone zone = TimeZone.getTimeZone("GMT");

    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.  If this is the first output,
     * send the HTTP headers prior to the user data.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
/*
        if (!isCommitted()) {
            sendHeaders();
        }
*/
        super.flushBuffer();

    }


    /**
     * Clear any content written to the buffer.  In addition, all cookies
     * and headers are cleared, and the status is reset.
     *
     * @exception IllegalStateException if this response has already
     *  been committed
     */
    public void reset() {

        super.reset();
        cookies.clear();
        headers.clear();
//        message = null;
        status = HttpServletResponse.SC_OK;

    }


    /**
     * Set the content length (in bytes) for this Response.
     *
     * @param length The new content length
     */
    public void setContentLength(int length) {

        if (isCommitted())
            return;

        super.setContentLength(length);

    }



    /**
     * Set the content type for this Response.
     *
     * @param type The new content type
     */
    public void setContentType(String type) {

        if (isCommitted())
            return;

        super.setContentType(type);

    }


    /**
     * Set the Locale that is appropriate for this response, including
     * setting the appropriate character encoding.
     *
     * @param locale The new locale
     */
    public void setLocale(Locale locale) {

        if (isCommitted())
            return;

        super.setLocale(locale);
        String language = locale.getLanguage();
        if ((language != null) && (language.length() > 0)) {
            String country = locale.getCountry();
            StringBuffer value = new StringBuffer(language);
            if ((country != null) && (country.length() > 0)) {
                value.append('-');
                value.append(country);
            }
            setHeader("Content-Language", value.toString());
        }

    }


    // -------------------------------------------- HttpServletResponse Methods


    /**
     * Add the specified Cookie to those that will be included with
     * this Response.
     *
     * @param cookie Cookie to be added
     */
    public void addCookie(Cookie cookie) {

        if (isCommitted())
            return;

        synchronized (cookies) {
            cookies.add(cookie);
        }

    }


    /**
     * Add the specified date header to the specified value.
     *
     * @param name Name of the header to set
     * @param value Date value to be set
     */
    public void addDateHeader(String name, long value) {

        if (isCommitted())
            return;

        addHeader(name, format.format(new Date(value)));

    }


    /**
     * Add the specified header to the specified value.
     *
     * @param name Name of the header to set
     * @param value Value to be set
     */
    public void addHeader(String name, String value) {

        if (isCommitted())
            return;

        synchronized (headers) {
            List<String> values = headers.get(name);
            if (values == null) {
                values = new ArrayList<String>();
                headers.put(name, values);
            }
            values.add(value);
        }

    }


    /**
     * Add the specified integer header to the specified value.
     *
     * @param name Name of the header to set
     * @param value Integer value to be set
     */
    public void addIntHeader(String name, int value) {

        if (isCommitted())
            return;

        addHeader(name, "" + value);

    }


    /**
     * Has the specified header been set already in this response?
     *
     * @param name Name of the header to check
     */
    public boolean containsHeader(String name) {

        synchronized (headers) {
            return (headers.get(name) != null);
        }

    }


    /**
     * Encode the session identifier associated with this response
     * into the specified redirect URL, if necessary.
     *
     * @param url URL to be encoded
     */
    public String encodeRedirectURL(String url) {

        return (url);

    }


    /**
     * Encode the session identifier associated with this response
     * into the specified redirect URL, if necessary.
     *
     * @param url URL to be encoded
     *
     * @deprecated As of Version 2.1 of the Java Servlet API, use
     *  <code>encodeRedirectURL()</code> instead.
     */
    public String encodeRedirectUrl(String url) {

        return (encodeRedirectURL(url));

    }


    /**
     * Encode the session identifier associated with this response
     * into the specified URL, if necessary.
     *
     * @param url URL to be encoded
     */
    public String encodeURL(String url) {
        
        return (url);
    }


    /**
     * Encode the session identifier associated with this response
     * into the specified URL, if necessary.
     *
     * @param url URL to be encoded
     *
     * @deprecated As of Version 2.1 of the Java Servlet API, use
     *  <code>encodeURL()</code> instead.
     */
    public String encodeUrl(String url) {

        return (encodeURL(url));

    }


    /**
     * Send an error response with the specified status and a
     * default message.
     *
     * @param status HTTP status code to send
     *
     * @exception IllegalStateException if this response has
     *  already been committed
     * @exception IOException if an input/output error occurs
     */
    public void sendError(int status) throws IOException {

//        sendError(status, getStatusMessage(status));

    }


    /**
     * Send an error response with the specified status and message.
     *
     * @param status HTTP status code to send
     * @param message Corresponding message to send
     *
     * @exception IllegalStateException if this response has
     *  already been committed
     * @exception IOException if an input/output error occurs
     */
    public void sendError(int status, String message) throws IOException {

        if (isCommitted()) {
            throw new IllegalStateException( "Cannot send error, already committed" );
        }


        //setError();

        // Record the status code and message.
        this.status = status;
//        this.message = message;

        // Clear any data content that has been buffered
        resetBuffer();

        // Cause the response to be committed
        /* Per spec clarification, no default content type is set
        String contentType = getContentType();
        if ((contentType == null) || "text/plain".equals(contentType))
            setContentType("text/html");
        */

        // Temporarily comment out the following flush so that
        // default error reports can still set the content type
        // FIXME - this stuff needs to be refactored
        /*
        try {
            flushBuffer();
        } catch (IOException e) {
            ;
        }
        */

    }


    /**
     * Send a temporary redirect to the specified redirect location URL.
     *
     * @param location Location URL to redirect to
     *
     * @exception IllegalStateException if this response has
     *  already been committed
     * @exception IOException if an input/output error occurs
     */
    public void sendRedirect(String location) throws IOException {

        if (isCommitted()) {
            throw new IllegalStateException( "Cannot send error, already committed" );
        }
        // Clear any data content that has been buffered
        resetBuffer();

        // Generate a temporary redirect to the specified location
        //String absolute = toAbsolute(location);
        String absolute = location;
        setStatus(SC_MOVED_TEMPORARILY);
        setHeader("Location", absolute);

    }


    /**
     * Set the specified date header to the specified value.
     *
     * @param name Name of the header to set
     * @param value Date value to be set
     */
    public void setDateHeader(String name, long value) {

        if (isCommitted())
            return;

        setHeader(name, format.format(new Date(value)));

    }


    /**
     * Set the specified header to the specified value.
     *
     * @param name Name of the header to set
     * @param value Value to be set
     */
    public void setHeader(String name, String value) {

        if (isCommitted())
            return;

        ArrayList<String> values = new ArrayList<String>();
        values.add(value);
        synchronized (headers) {
            headers.put(name, values);
        }

        String match = name.toLowerCase();
        if (match.equals("content-length")) {
            int contentLength = -1;
            try {
                contentLength = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                ;
            }
            if (contentLength >= 0)
                setContentLength(contentLength);
        } else if (match.equals("content-type")) {
            setContentType(value);
        }

    }


    /**
     * Set the specified integer header to the specified value.
     *
     * @param name Name of the header to set
     * @param value Integer value to be set
     */
    public void setIntHeader(String name, int value) {

        if (isCommitted())
            return;

        setHeader(name, "" + value);

    }


    /**
     * Set the HTTP status to be returned with this response.
     *
     * @param status The new HTTP status
     */
    public void setStatus(int status) {

//        setStatus(status, getStatusMessage(status));

    }


    /**
     * Set the HTTP status and message to be returned with this response.
     *
     * @param status The new HTTP status
     * @param message The associated text message
     *
     * @deprecated As of Version 2.1 of the Java Servlet API, this method
     *  has been deprecated due to the ambiguous meaning of the message
     *  parameter.
     */
    public void setStatus(int status, String message) {

        this.status = status;
//        this.message = message;

    }


}
