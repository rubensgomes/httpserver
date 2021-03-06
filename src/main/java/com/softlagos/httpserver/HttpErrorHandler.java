/**
 * Copyright (C) 1999-2016 Rubens Gomes <rubens.s.gomes@gmail.com>.
 * All Rights Reserved.
 *
 * File: HttpErrorHandler.java
 *
 * Author: Rubens Gomes
 */
package com.softlagos.httpserver;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An Error Handler responsible for handling
 * either client or server related exceptions
 * raised in the HTTP server.
 *
 * @author Rubens Gomes
 */
public final class HttpErrorHandler
{
    /** The logger. */
    private static final Logger logger =
            LogManager.getLogger(HttpErrorHandler.class);

    /**
     * Instantiates a new http error handler.
     *
     * @param ex the exception raised
     * @param writer the writer to send HTTP response
     */
    public HttpErrorHandler(Exception ex, OutputStreamWriter writer)
    {
        if(ex == null)
        {
            throw new IllegalArgumentException("ex cannot be null.");
        }

        if(writer == null)
        {
            throw new IllegalArgumentException("writer cannot be null.");
        }

        v_writer = writer;
        HttpStatusLine status_line =
                HttpStatusLine.makeHttpStatusLine(ex);

        if(logger.isDebugEnabled())
        {
            logger.debug("HTTP response status_line: " + status_line);
        }

        HttpResponseHeader header =
                new HttpResponseHeader(status_line);

        HttpMessageBody body = new HttpMessageBody();
        body.add(ex.getMessage());

        if(logger.isDebugEnabled())
        {
            logger.debug("HTTP message body: " + body);
        }

        v_response = new HttpResponse(header, body);
    }

    /**
     * Send HTTP response.
     */
    public void sendResponse() throws IOException
    {
        if( (v_response == null) || (v_response.toString() == null) )
        {
            throw new IllegalStateException("v_response cannot be empty.");
        }

        String resp = v_response.toString();
        v_writer.write(resp, 0, resp.length());
        v_writer.flush();
    }

    /** The v_status_line. */
    // ------ >>> Private <<< ------
    private final OutputStreamWriter v_writer;
    private final HttpResponse v_response;
}
