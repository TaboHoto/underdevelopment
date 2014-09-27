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


    // ----------------------------------------------------- Instance Variables


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
     * Are we currently processing inside a RequestDispatcher.include()?
     */
    protected boolean included = false;


    /**
     * The Locale associated with this Response.
     */
    protected Locale locale = Locale.getDefault();


    /**
     * The output stream associated with this Response.
     */
    protected OutputStream output = null;


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


    // ------------------------------------------------------------- Properties



    /**
     * Return the number of bytes actually written to the output stream.
     */
    public int getContentCount() {

        return (this.contentCount);

    }




    /**
     * Return the output stream associated with this Response.
     */
    public OutputStream getStream() {

        return (this.output);

    }


    /**
     * Set the output stream associated with this Response.
     *
     * @param stream The new output stream
     */
    public void setStream(OutputStream stream) {

        this.output = stream;

    }



    // --------------------------------------------------------- Public Methods


    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {

        //return (new ResponseStream(this));
        return new BufferedServletOutputStream();
    }


    /**
     * Perform whatever actions are required to flush and close the output
     * stream or writer, in a single operation.
     *
     * @exception IOException if an input/output error occurs
     */
    public void finishResponse() throws IOException {

        // If no stream has been requested yet, get one so we can
        // flush the necessary headers
        if (this.stream == null) {
            ServletOutputStream sos = getOutputStream();
            sos.flush();
            sos.close();
            return;
        }

        // If our stream is closed, no action is necessary
/*        
        if ( ((ResponseStream) stream).closed() )
            return;
*/

        // Flush and close the appropriate output mechanism
        if (writer != null) {
            writer.flush();
            writer.close();
        } else {
            stream.flush();
            stream.close();
        }

        // The underlying output stream (perhaps from a socket)
        // is not our responsibility

    }


    /**
     * Return the content length that was set or calculated for this Response.
     */
    public int getContentLength() {

        return (this.contentLength);

    }


    /**
     * Return the content type that was set or calculated for this response,
     * or <code>null</code> if no content type was set.
     */
    public String getContentType() {

        return (this.contentType);

    }



    // -------------------------------------------------------- Package Methods



    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {

        committed = true;
        if (bufferCount > 0) {
            try {
                output.write(buffer, 0, bufferCount);
            } finally {
                bufferCount = 0;
            }
        }

    }


    /**
     * Return the actual buffer size used for this Response.
     */
    public int getBufferSize() {

        return (buffer.length);

    }


    /**
     * Return the character encoding used for this Response.
     */
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
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null) {
            throw new IllegalStateException( "getWriter() has already been called" );
        }

        if (stream == null)
            stream = createOutputStream();
/*        
        ((ResponseStream) stream).setCommit(true);
*/
        return (stream);

    }


    /**
     * Return the Locale assigned to this response.
     */
    public Locale getLocale() {

        return (locale);

    }


    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException if <code>getOutputStream</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (writer != null)
            return (writer);

        if (stream != null) {
            throw new IllegalStateException( "getOutputStream() has already been called" );
        }

        stream = createOutputStream();
        
        // a slight hack which slightly breaks the Servlet contract...
        // see commented out section below for what it should be...
        writer =  new PrintWriter( new OutputStreamWriter(stream, getCharacterEncoding()) );
        return writer;
        
/*        
        ((ResponseStream) stream).setCommit(false);
        OutputStreamWriter osr =
          new OutputStreamWriter(stream, getCharacterEncoding());
        writer = new ResponseWriter(osr, (ResponseStream) stream);
        return (writer);
*/
    }


    /**
     * Has the output of this response already been committed?
     */
    public boolean isCommitted() {

        return (committed);

    }


    /**
     * Clear any content written to the buffer.
     *
     * @exception IllegalStateException if this response has already
     *  been committed
     */
    public void reset() {

        if (committed) {
            throw new IllegalStateException( "response has already been committed" );
        }
        
        if (included)
            return;     // Ignore any call from an included servlet

/*        
        if (stream != null)
            ((ResponseStream) stream).reset();
*/
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
    public void setContentLength(int length) {

        if (isCommitted())
            return;

        if (included)
            return;     // Ignore any call from an included servlet

        this.contentLength = length;

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

        this.contentType = type;
/*
        if (type.indexOf(';') >= 0) {
            encoding = RequestUtil.parseCharacterEncoding(type);
            if (encoding == null)
                encoding = "ISO-8859-1";
        }
*/
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

        this.locale = locale;
/*        
        if ((this.encoding == null) && (this.context != null)) {
            CharsetMapper mapper = context.getCharsetMapper();
            this.encoding = mapper.getCharset(locale);
        }
*/
    }


}
