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


package org.apache.commons.messagelet.impl;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    protected ArrayList cookies = new ArrayList();


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
    protected HashMap headers = new HashMap();



    /**
     * The error message set by <code>sendError()</code>.
     */
    protected String message = getStatusMessage(HttpServletResponse.SC_OK);


    /**
     * The HTTP status code associated with this Response.
     */
    protected int status = HttpServletResponse.SC_OK;


    /**
     * The time zone with which to construct date headers.
     */
    protected static final TimeZone zone = TimeZone.getTimeZone("GMT");



    // --------------------------------------------------------- Public Methods


    /**
     * Return an array of all cookies set for this response, or
     * a zero-length array if no cookies have been set.
     */
    public Cookie[] getCookies() {

        synchronized (cookies) {
            return ((Cookie[]) cookies.toArray(new Cookie[cookies.size()]));
        }

    }


    /**
     * Return the value for the specified header, or <code>null</code> if this
     * header has not been set.  If more than one value was added for this
     * name, only the first is returned; use getHeaderValues() to retrieve all
     * of them.
     *
     * @param name Header name to look up
     */
    public String getHeader(String name) {

        ArrayList values = null;
        synchronized (headers) {
            values = (ArrayList) headers.get(name);
        }
        if (values != null)
            return ((String) values.get(0));
        else
            return (null);

    }


    /**
     * Return an array of all the header names set for this response, or
     * a zero-length array if no headers have been set.
     */
    public String[] getHeaderNames() {

        synchronized (headers) {
            String results[] = new String[headers.size()];
            return ((String[]) headers.keySet().toArray(results));
        }

    }


    /**
     * Return an array of all the header values associated with the
     * specified header name, or an zero-length array if there are no such
     * header values.
     *
     * @param name Header name to look up
     */
    public String[] getHeaderValues(String name) {

        ArrayList values = null;
        synchronized (headers) {
            values = (ArrayList) headers.get(name);
        }
        if (values == null)
            return (new String[0]);
        String results[] = new String[values.size()];
        return ((String[]) values.toArray(results));

    }


    /**
     * Return the error message that was set with <code>sendError()</code>
     * for this Response.
     */
    public String getMessage() {

        return (this.message);

    }


    /**
     * Return the HTTP status code associated with this Response.
     */
    public int getStatus() {

        return (this.status);

    }


    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    public void recycle() {
        //super.recycle();
        cookies.clear();
        headers.clear();
        message = getStatusMessage(HttpServletResponse.SC_OK);
        status = HttpServletResponse.SC_OK;

    }


    /**
     * Reset this response, and specify the values for the HTTP status code
     * and corresponding message.
     *
     * @exception IllegalStateException if this response has already been
     *  committed
     */
    public void reset(int status, String message) {

        reset();
        setStatus(status, message);

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Returns a default status message for the specified HTTP status code.
     *
     * @param status The status code for which a message is desired
     */
    protected String getStatusMessage(int status) {

        switch (status) {
        case SC_OK:
            return ("OK");
        case SC_ACCEPTED:
            return ("Accepted");
        case SC_BAD_GATEWAY:
            return ("Bad Gateway");
        case SC_BAD_REQUEST:
            return ("Bad Request");
        case SC_CONFLICT:
            return ("Conflict");
        case SC_CONTINUE:
            return ("Continue");
        case SC_CREATED:
            return ("Created");
        case SC_EXPECTATION_FAILED:
            return ("Expectation Failed");
        case SC_FORBIDDEN:
            return ("Forbidden");
        case SC_GATEWAY_TIMEOUT:
            return ("Gateway Timeout");
        case SC_GONE:
            return ("Gone");
        case SC_HTTP_VERSION_NOT_SUPPORTED:
            return ("HTTP Version Not Supported");
        case SC_INTERNAL_SERVER_ERROR:
            return ("Internal Server Error");
        case SC_LENGTH_REQUIRED:
            return ("Length Required");
        case SC_METHOD_NOT_ALLOWED:
            return ("Method Not Allowed");
        case SC_MOVED_PERMANENTLY:
            return ("Moved Permanently");
        case SC_MOVED_TEMPORARILY:
            return ("Moved Temporarily");
        case SC_MULTIPLE_CHOICES:
            return ("Multiple Choices");
        case SC_NO_CONTENT:
            return ("No Content");
        case SC_NON_AUTHORITATIVE_INFORMATION:
            return ("Non-Authoritative Information");
        case SC_NOT_ACCEPTABLE:
            return ("Not Acceptable");
        case SC_NOT_FOUND:
            return ("Not Found");
        case SC_NOT_IMPLEMENTED:
            return ("Not Implemented");
        case SC_NOT_MODIFIED:
            return ("Not Modified");
        case SC_PARTIAL_CONTENT:
            return ("Partial Content");
        case SC_PAYMENT_REQUIRED:
            return ("Payment Required");
        case SC_PRECONDITION_FAILED:
            return ("Precondition Failed");
        case SC_PROXY_AUTHENTICATION_REQUIRED:
            return ("Proxy Authentication Required");
        case SC_REQUEST_ENTITY_TOO_LARGE:
            return ("Request Entity Too Large");
        case SC_REQUEST_TIMEOUT:
            return ("Request Timeout");
        case SC_REQUEST_URI_TOO_LONG:
            return ("Request URI Too Long");
        case SC_REQUESTED_RANGE_NOT_SATISFIABLE:
            return ("Requested Range Not Satisfiable");
        case SC_RESET_CONTENT:
            return ("Reset Content");
        case SC_SEE_OTHER:
            return ("See Other");
        case SC_SERVICE_UNAVAILABLE:
            return ("Service Unavailable");
        case SC_SWITCHING_PROTOCOLS:
            return ("Switching Protocols");
        case SC_UNAUTHORIZED:
            return ("Unauthorized");
        case SC_UNSUPPORTED_MEDIA_TYPE:
            return ("Unsupported Media Type");
        case SC_USE_PROXY:
            return ("Use Proxy");
        case 207:       // WebDAV
            return ("Multi-Status");
        case 422:       // WebDAV
            return ("Unprocessable Entity");
        case 423:       // WebDAV
            return ("Locked");
        case 507:       // WebDAV
            return ("Insufficient Storage");
        default:
            return ("HTTP Response Status " + status);
        }

    }



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

        if (included)
            return;     // Ignore any call from an included servlet

        super.reset();
        cookies.clear();
        headers.clear();
        message = null;
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

        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if (values == null) {
                values = new ArrayList();
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

        if (included)
            return;     // Ignore any call from an included servlet

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

        sendError(status, getStatusMessage(status));

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

        if (included) {
            return;     // Ignore any call from an included servlet
        }

        //setError();

        // Record the status code and message.
        this.status = status;
        this.message = message;

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
        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

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

        if (included)
            return;     // Ignore any call from an included servlet

        ArrayList values = new ArrayList();
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

        if (included)
            return;     // Ignore any call from an included servlet

        setHeader(name, "" + value);

    }


    /**
     * Set the HTTP status to be returned with this response.
     *
     * @param status The new HTTP status
     */
    public void setStatus(int status) {

        setStatus(status, getStatusMessage(status));

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

        if (included)
            return;     // Ignore any call from an included servlet

        this.status = status;
        this.message = message;

    }


}
