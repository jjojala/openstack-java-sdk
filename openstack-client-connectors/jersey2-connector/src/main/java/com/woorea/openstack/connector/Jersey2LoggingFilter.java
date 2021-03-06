/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.woorea.openstack.connector;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * Universal logging filter.
 *
 * Can be used on client or server side. Has the highest priority.
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 * @author Martin Matula (martin.matula at oracle.com)
 */
@PreMatching
@Priority(Integer.MIN_VALUE)
@SuppressWarnings("ClassWithMultipleLoggers")
public class Jersey2LoggingFilter implements ContainerRequestFilter, ClientRequestFilter, ContainerResponseFilter,
        ClientResponseFilter, WriterInterceptor {

    private static final Logger LOGGER = Logger.getLogger(Jersey2LoggingFilter.class.getName());
    private static final String NOTIFICATION_PREFIX = "* ";
    private static final String REQUEST_PREFIX = "> ";
    private static final String RESPONSE_PREFIX = "< ";
    private static final String ENTITY_LOGGER_PROPERTY = Jersey2LoggingFilter.class.getName() + ".entityLogger";
    //
    @SuppressWarnings("NonConstantLogger")
    private final Logger logger;
    private final AtomicLong _id = new AtomicLong(0);
    private final boolean printEntity;
    private final int maxEntitySize;

    /**
     * Create a logging filter logging the request and response to a default JDK
     * logger, named as the fully qualified class name of this class. Entity
     * logging is turned off by default.
     */
    public Jersey2LoggingFilter() {
        this(LOGGER, false);
    }

    /**
     * Create a logging filter with custom logger and custom settings of entity
     * logging.
     *
     * @param logger the logger to log requests and responses.
     * @param printEntity if true, entity will be logged as well up to the default maxEntitySize, which is 10KB
     */
    public Jersey2LoggingFilter(Logger logger, boolean printEntity) {
        this.logger = logger;
        this.printEntity = printEntity;
        this.maxEntitySize = 10 * 1024;
    }

    /**
     * Creates a logging filter with custom logger and entity logging turned on, but potentially limiting the size
     * of entity to be buffered and logged.
     *
     * @param logger the logger to log requests and responses.
     * @param maxEntitySize maximum number of entity bytes to be logged (and buffered) - if the entity is larger,
     *                      logging filter will print (and buffer in memory) only the specified number of bytes
     *                      and print "...more..." string at the end.
     */
    public Jersey2LoggingFilter(Logger logger, int maxEntitySize) {
        this.logger = logger;
        this.printEntity = true;
        this.maxEntitySize = maxEntitySize;
    }

    private void log(StringBuilder b) {
        if (logger != null) {
            logger.info(b.toString());
        }
    }

    private StringBuilder prefixId(StringBuilder b, long id) {
        b.append(Long.toString(id)).append(" ");
        return b;
    }

    private void printRequestLine(StringBuilder b, long id, String method, URI uri) {
        prefixId(b, id).append(NOTIFICATION_PREFIX).append("LoggingFilter - Request received on thread ").append(Thread.currentThread().getName()).append("\n");
        prefixId(b, id).append(REQUEST_PREFIX).append(method).append(" ").
                append(uri.toASCIIString()).append("\n");
    }

    private void printResponseLine(StringBuilder b, long id, int status) {
        prefixId(b, id).append(NOTIFICATION_PREFIX).
                append("LoggingFilter - Response received on thread ").append(Thread.currentThread().getName()).append("\n");
        prefixId(b, id).append(RESPONSE_PREFIX).
                append(Integer.toString(status)).
                append("\n");
    }

    private void printPrefixedHeaders(StringBuilder b, long id, final String prefix, MultivaluedMap<String, String> headers) {
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            List<?> val = e.getValue();
            String header = e.getKey();

            if (val.size() == 1) {
                prefixId(b, id).append(prefix).append(header).append(": ").append(val.get(0)).append("\n");
            } else {
                StringBuilder sb = new StringBuilder();
                boolean add = false;
                for (Object s : val) {
                    if (add) {
                        sb.append(',');
                    }
                    add = true;
                    sb.append(s);
                }
                prefixId(b, id).append(prefix).append(header).append(": ").append(sb.toString()).append("\n");
            }
        }
    }

    private InputStream logInboundEntity(StringBuilder b, InputStream stream) throws IOException {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        stream.mark(maxEntitySize + 1);
        byte[] entity = new byte[maxEntitySize + 1];
        int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize)));
        if (entitySize > maxEntitySize) {
            b.append("...more...");
        }
        b.append('\n');
        stream.reset();
        return stream;
    }

    @Override
    public void filter(ClientRequestContext context) throws IOException {
        long id = this._id.incrementAndGet();
        StringBuilder b = new StringBuilder();

        printRequestLine(b, id, context.getMethod(), context.getUri());
        // TODO: change to context.getStringHeaders() once the method is added to the API
        printPrefixedHeaders(b, id, REQUEST_PREFIX, asStringHeaders(context.getHeaders()));

        if (printEntity && context.hasEntity()) {
            OutputStream stream = new LoggingStream(b, context.getEntityStream());
            context.setEntityStream(stream);
            context.setProperty(ENTITY_LOGGER_PROPERTY, stream);
            // not calling log(b) here - it will be called by the interceptor
        } else {
            log(b);
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        long id = this._id.incrementAndGet();
        StringBuilder b = new StringBuilder();

        printResponseLine(b, id, responseContext.getStatus());
        printPrefixedHeaders(b, id, RESPONSE_PREFIX, responseContext.getHeaders());

        if (printEntity && responseContext.hasEntity()) {
            responseContext.setEntityStream(logInboundEntity(b, responseContext.getEntityStream()));
        }

        log(b);
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        long id = this._id.incrementAndGet();
        StringBuilder b = new StringBuilder();

        printRequestLine(b, id, context.getMethod(), context.getUriInfo().getRequestUri());
        printPrefixedHeaders(b, id, REQUEST_PREFIX, context.getHeaders());

        if (printEntity && context.hasEntity()) {
            context.setEntityStream(logInboundEntity(b, context.getEntityStream()));
        }

        log(b);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        long id = this._id.incrementAndGet();
        StringBuilder b = new StringBuilder();

        printResponseLine(b, id, responseContext.getStatus());
        // TODO: change to context.getStringHeaders() once the method is added to the API
        printPrefixedHeaders(b, id, RESPONSE_PREFIX, asStringHeaders(responseContext.getHeaders()));

        if (printEntity && responseContext.hasEntity()) {
            OutputStream stream = new LoggingStream(b, responseContext.getEntityStream());
            responseContext.setEntityStream(stream);
            requestContext.setProperty(ENTITY_LOGGER_PROPERTY, stream);
            // not calling log(b) here - it will be called by the interceptor
        } else {
            log(b);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        LoggingStream stream = (LoggingStream) writerInterceptorContext.getProperty(ENTITY_LOGGER_PROPERTY);
        writerInterceptorContext.proceed();
        if (stream != null) {
            log(stream.getStringBuilder());
        }
    }

    private static MultivaluedMap<String, String> asStringHeaders(
            final MultivaluedMap<String, Object> headers) {

        // NOTE: This method becomes obsolete when JAX-RS API contain
        //       getStringHeaders() method (and it's widely in use)

        if (headers == null) {
            return null;
        }

        final MultivaluedMap<String, String> result =
                new MultivaluedHashMap<String, String>();
        final RuntimeDelegate rd = RuntimeDelegate.getInstance();

        for (final Map.Entry<String, List<Object>> e: headers.entrySet()) {
            if (e.getValue() == null || e.getValue().isEmpty()) {
                result.add(e.getKey(), "[null]");
            } else {
                for (final Object header: e.getValue()) {
                    final RuntimeDelegate.HeaderDelegate delegate =
                            rd.createHeaderDelegate(header.getClass());
                    final String headerValue = delegate == null
                            ? header.toString() : delegate.toString(header);
                    result.add(e.getKey(), headerValue);
                }
            }
        }

        return result;
    }

    private class LoggingStream extends OutputStream {
    	private static final String PASSWORD_PATTERN = "\"password\".*:.*\"(.*)\"";
        private final StringBuilder b;
        private final OutputStream inner;
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LoggingStream(StringBuilder b, OutputStream inner) {
            this.b = b;
            this.inner = inner;
        }

        StringBuilder getStringBuilder() {
            // write entity to the builder
            byte[] entity = baos.toByteArray();
            String entityString = new String(entity);
            entityString = entityString.replaceAll(PASSWORD_PATTERN, "\"password\" : \"******\"");
            b.append(entityString).append('\n');
            return b;
        }

        @Override
        public void write(int i) throws IOException {
            if (baos.size() <= maxEntitySize) {
                baos.write(i);
            }
            inner.write(i);
        }
    }
}
