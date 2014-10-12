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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;


/**
 * Based on the ResponseBase code from Catalina.
 *
 * @author Craig R. McClanahan
 * @author James Strachan
 * @version $Revision$ $Date$
 */

public class ServletResponseImpl implements ServletResponse {

    /**
     * The buffer through which all of our output bytes are passed.
     */
    protected byte[] buffer = new byte[1024];


    /**
     * The number of data bytes currently in the buffer.
     */
    protected int bufferCount = 0;


    /**
     * Has this response been committed yet?
     */
    protected boolean committed = false;


    /**
     * The actual number of bytes written to this Response.
     */
    protected int contentCount = 0;


    /**
     * The content length associated with this Response.
     */
    protected int contentLength = -1;


    /**
     * The content type associated with this Response.
     */
    protected String contentType = null;


    /**
     * The character encoding associated with this Response.
     */
    protected String encoding = null;

    /**
     * The Locale associated with this Response.
     */
    protected Locale locale = Locale.getDefault();

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */
    protected ServletOutputStream stream = null;

    /**
     * The PrintWriter that has been returned by
     * <code>getWriter()</code>, if any.
     */
    protected PrintWriter writer = null;


    /**
     * Error flag. True if the response is an error report.
     */
    protected boolean error = false;

    public void setStream(ServletOutputStream stream){
        this.stream = stream;
    }

    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    @Override
    public void flushBuffer() throws IOException {
        stream.flush();
    }


    /**
     * Return the actual buffer size used for this Response.
     */
    @Override
    public int getBufferSize() {

        return (buffer.length);

    }

    /**
     * Return the character encoding used for this Response.
     */
    @Override
    public String getCharacterEncoding() {

        if (encoding == null)
            return ("ISO-8859-1");
        else
            return (encoding);

    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @exception IllegalStateException if <code>getWriter</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return stream;
    }

    /**
     * Return the Locale assigned to this response.
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException if <code>getOutputStream</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    @Override
    public PrintWriter getWriter() throws IOException {

        if (writer != null)
            return (writer);
        if (stream != null) {
            throw new IllegalStateException( "getOutputStream() has already been called" );
        }
        // a slight hack which slightly breaks the Servlet contract...
        // see commented out section below for what it should be...
        return new PrintWriter( new OutputStreamWriter(stream, getCharacterEncoding()) ); 
    }

    /**
     * Has the output of this response already been committed?
     */
    @Override
    public boolean isCommitted() {
        return committed;
    }

    /**
     * Clear any content written to the buffer.
     *
     * @exception IllegalStateException if this response has already
     *  been committed
     */
    @Override
    public void reset() {

        if (committed) {
            throw new IllegalStateException( "response has already been committed" );
        }
        bufferCount = 0;
        contentLength = -1;
        contentType = null;

    }


    /**
     * Reset the data buffer but not any status or header information.
     *
     * @exception IllegalStateException if the response has already
     *  been committed
     */
    @Override
    public void resetBuffer() {

        if (committed) {
            throw new IllegalStateException( "response has already been committed" );
        }
        
        bufferCount = 0;

    }

    /**
     * Set the buffer size to be used for this Response.
     *
     * @param size The new buffer size
     *
     * @exception IllegalStateException if this method is called after
     *  output has been committed for this response
     */
    @Override
    public void setBufferSize(int size) {

        if (committed || (bufferCount > 0)) {
            throw new IllegalStateException( "Output has already been committed" );
        }

        if (buffer.length >= size)
            return;
        buffer = new byte[size];

    }

    /**
     * Set the content length (in bytes) for this Response.
     *
     * @param length The new content length
     */
    @Override
    public void setContentLength(int length) {

        if (isCommitted())
            return;

        this.contentLength = length;

    }

    /**
     * Set the content type for this Response.
     *
     * @param type The new content type
     */
    @Override
    public void setContentType(String type) {

        if (isCommitted())
            return;

        this.contentType = type;
    }

    /**
     * Set the Locale that is appropriate for this response, including
     * setting the appropriate character encoding.
     *
     * @param locale The new locale
     */
    @Override
    public void setLocale(Locale locale) {
        if (isCommitted())
            return;

        this.locale = locale;
    }
}
