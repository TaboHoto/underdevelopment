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

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.apache.commons.collections.iterators.IteratorEnumeration;

/**
 * Based on the HttpRequestBase code from Catalina.
 *
 * @author Craig R. McClanahan
 * @author James Strachan
 * @version $Revision$ $Date$
 */

public class HttpServletRequestImpl extends ServletRequestImpl implements HttpServletRequest {


    /**
     * The authentication type used for this request.
     */
    protected String authType = null;


    /**
     * The context path for this request.
     */
    protected String contextPath = "";


    /**
     * The set of cookies associated with this Request.
     */
    protected ArrayList<Cookie> cookies = new ArrayList<Cookie>();


    /**
     * The set of SimpleDateFormat formats to use in getDateHeader().
     */
    protected SimpleDateFormat formats[] = {
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
        new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
        new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
    };



    /**
     * The HTTP headers associated with this Request, keyed by name.  The
     * values are ArrayLists of the corresponding header values.
     */
    protected HashMap<String,List<String>> headers = new HashMap<String,List<String>> ();



    /**
     * The request method associated with this Request.
     */
    protected String method = null;


    /**
     * The path information for this request.
     */
    protected String pathInfo = null;


    /**
     * The query string for this request.
     */
    protected String queryString = null;


    /**
     * Was the requested session ID received in a cookie?
     */
    protected boolean requestedSessionCookie = false;


    /**
     * The requested session ID (if any) for this request.
     */
    protected String requestedSessionId = null;


    /**
     * Was the requested session ID received in a URL?
     */
    protected boolean requestedSessionURL = false;


    /**
     * The request URI associated with this request.
     */
    protected String requestURI = null;


    /**
     * The servlet path for this request.
     */
    protected String servletPath = null;


    /**
     * The currently active session for this request.
     */
    protected HttpSessionImpl session = null;


    /**
     * The Principal who has been authenticated for this Request.
     */
    protected Principal userPrincipal = null;

    
    /**
     * The parameters 
     */
    private Map parameters = null;


    // --------------------------------------------------------- Public Methods

    public HttpServletRequestImpl(ServletContext servletContext) {
        super( servletContext );
    }


    // --------------------------------------------- HttpServletRequest Methods


    /**
     * Return the authentication type used for this Request.
     */
    @Override
    public String getAuthType() {

        return (authType);

    }


    /**
     * Return the portion of the request URI used to select the Context
     * of the Request.
     */
    @Override
    public String getContextPath() {

        return (contextPath);

    }


    /**
     * Return the set of Cookies received with this Request.
     */
    @Override
    public Cookie[] getCookies() {
            if (cookies.size() < 1)
                return (null);
            Cookie results[] = new Cookie[cookies.size()];
            return cookies.toArray(results);
    }


    /**
     * Return the value of the specified date header, if any; otherwise
     * return -1.
     *
     * @param name Name of the requested date header
     *
     * @exception IllegalArgumentException if the specified header value
     *  cannot be converted to a date
     */
    @Override
    public long getDateHeader(String name) {

        String value = getHeader(name);
        if (value == null)
            return (-1L);

        // Work around a bug in SimpleDateFormat in pre-JDK1.2b4
        // (Bug Parade bug #4106807)
        value += " ";

        // Attempt to convert the date header in a variety of formats
        for (int i = 0; i < formats.length; i++) {
            try {
                Date date = formats[i].parse(value);
                return date.getTime();
            } catch (ParseException e) {
                ;
            }
        }
        throw new IllegalArgumentException(value);

    }


    /**
     * Return the first value of the specified header, if any; otherwise,
     * return <code>null</code>
     *
     * @param name Name of the requested header
     */
    @Override
    public String getHeader(String name) {
        name = name.toLowerCase();
            List<String> values = headers.get(name);
            if (values != null){
                return values.get(0);
            }else{
                return null;
            }
    }

    /**
     * Return all of the values of the specified header, if any; otherwise,
     * return an empty enumeration.
     *
     * @param name Name of the requested header
     */
    @Override
    public Enumeration getHeaders(String name) {
        name = name.toLowerCase();
            List<String> values = headers.get(name);
            if (values != null)
//                return (new IteratorEnumeration( values.iterator() ));
return null;
            else
//                return (new IteratorEnumeration( Collections.EMPTY_LIST.iterator() ));
return null;
    }

    /**
     * Return the names of all headers received with this request.
     */
    @Override
    public Enumeration getHeaderNames() {
//            return (new IteratorEnumeration( headers.keySet().iterator() ));
return null;
    }

    /**
     * Return the value of the specified header as an integer, or -1 if there
     * is no such header for this request.
     *
     * @param name Name of the requested header
     *
     * @exception IllegalArgumentException if the specified header value
     *  cannot be converted to an integer
     */
    @Override
    public int getIntHeader(String name) {
        String value = getHeader(name);
        if (value == null)
            return (-1);
        else
            return (Integer.parseInt(value));
    }


    /**
     * Return the HTTP request method used in this Request.
     */
    @Override
    public String getMethod() {
        return method;
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
        if ( parameters == null ) {
            parameters = new HashMap();
            if ( queryString != null ) {
//                try {
//                    RequestUtil.parseParameters(parameters, queryString, getCharacterEncoding());
//                }
//                catch (UnsupportedEncodingException e) {
//                    servletContext.log( "Could not parse query string: " + queryString, e);
//                }
return null;
            }
        }
        return parameters;
    }

    /**
     * Return the path information associated with this Request.
     */
    @Override
    public String getPathInfo() {

        return (pathInfo);

    }

    /**
     * Return the extra path information for this request, translated
     * to a real path.
     */
    @Override
    public String getPathTranslated() {

        if (pathInfo == null)
            return (null);
        else
            return (servletContext.getRealPath(pathInfo));

    }


    /**
     * Return the query string associated with this request.
     */
    @Override
    public String getQueryString() {

        return (queryString);

    }


    /**
     * Return the name of the remote user that has been authenticated
     * for this Request.
     */
    @Override
    public String getRemoteUser() {

        if (userPrincipal != null)
            return (userPrincipal.getName());
        else
            return (null);

    }


    /**
     * Return the session identifier included in this request, if any.
     */
    @Override
    public String getRequestedSessionId() {

        return (requestedSessionId);

    }


    /**
     * Return the request URI for this request.
     */
    @Override
    public String getRequestURI() {

        return (requestURI);

    }


    /**
     * Reconstructs the URL the client used to make the request.
     * The returned URL contains a protocol, server name, port
     * number, and server path, but it does not include query
     * string parameters.
     * <p>
     * Because this method returns a <code>StringBuffer</code>,
     * not a <code>String</code>, you can modify the URL easily,
     * for example, to append query parameters.
     * <p>
     * This method is useful for creating redirect messages and
     * for reporting errors.
     *
     * @return A <code>StringBuffer</code> object containing the
     *  reconstructed URL
     */
    @Override
    public StringBuffer getRequestURL() {

        StringBuffer url = new StringBuffer();
        String scheme = getScheme();
        int port = getServerPort();
        if (port < 0)
            port = 80; // Work around java.net.URL bug

        url.append(scheme);
        url.append("://");
        url.append(getServerName());
        if ((scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(getRequestURI());

        return (url);

    }


    /**
     * Return the portion of the request URI used to select the servlet
     * that will process this request.
     */
    @Override
    public String getServletPath() {

        return (servletPath);

    }


    /**
     * Return the session associated with this Request, creating one
     * if necessary.
     */
    @Override
    public HttpSession getSession() {

        return (getSession(true));

    }


    /**
     * Return the session associated with this Request, creating one
     * if necessary and requested.
     *
     * @param create Create a new session if one does not exist
     */
    @Override
    public HttpSession getSession(boolean create) {
        // Return the current session if it exists and is valid
        if ((session != null) && !session.isValid())
            session = null;
        
        if ( create && session == null) {
            session = new HttpSessionImpl( servletContext );
        }
        return session;
    }


    /**
     * Return <code>true</code> if the session identifier included in this
     * request came from a cookie.
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {

        if (requestedSessionId != null)
            return (requestedSessionCookie);
        else
            return (false);

    }


    /**
     * Return <code>true</code> if the session identifier included in this
     * request came from the request URI.
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {

        if (requestedSessionId != null)
            return (requestedSessionURL);
        else
            return (false);

    }


    /**
     * Return <code>true</code> if the session identifier included in this
     * request came from the request URI.
     *
     * @deprecated As of Version 2.1 of the Java Servlet API, use
     *  <code>isRequestedSessionIdFromURL()</code> instead.
     */
    @Override
    public boolean isRequestedSessionIdFromUrl() {

        return (isRequestedSessionIdFromURL());

    }


    /**
     * Return <code>true</code> if the session identifier included in this
     * request identifies a valid session.
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        return false;

    }


    /**
     * Return <code>true</code> if the authenticated user principal
     * possesses the specified role name.
     *
     * @param role Role name to be validated
     */
    @Override
    public boolean isUserInRole(String role) {  
        return false;
    }


    /**
     * Return the principal that has been authenticated for this Request.
     */
    @Override
    public Principal getUserPrincipal() {

        return (userPrincipal);

    }
}
