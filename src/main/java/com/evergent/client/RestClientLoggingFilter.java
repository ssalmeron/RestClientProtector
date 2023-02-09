package com.evergent.client;

import jakarta.annotation.Priority;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Priority(Integer.MIN_VALUE)
public class RestClientLoggingFilter implements ClientRequestFilter, ClientResponseFilter, WriterInterceptor {

    private static final Logger logger = LogManager.getLogger("AppLogger");
    private static final String ENTITY_STREAM_PROPERTY = "EntityLoggingFilter.entityStream";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final int maxEntitySize = 1024 * 8;

    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {

        logger.debug("************* Calling Service: ************");
        logger.debug("Uri:"+ clientRequestContext.getMethod()+ " " + clientRequestContext.getUri());
        logger.debug("Headers:");
        clientRequestContext.getHeaders().forEach((s,o)->logger.debug("h: " +s +", v:" + o));



        logger.debug("Acceptable Languages:");
        clientRequestContext.getAcceptableLanguages().forEach(l->logger.debug(l.getDisplayLanguage()));

        logger.debug("************* END Calling Service: ************");



    }

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        logger.debug("Response status: " + clientResponseContext.getStatus());
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        final LoggingStream stream = (LoggingStream) writerInterceptorContext.getProperty(ENTITY_STREAM_PROPERTY);
        writerInterceptorContext.proceed();
        if (stream != null) {
            log(stream.getStringBuilder(DEFAULT_CHARSET));
        }
    }

    private class LoggingStream extends FilterOutputStream {

        private final StringBuilder sb = new StringBuilder();
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LoggingStream(OutputStream out) {
            super(out);
        }

        StringBuilder getStringBuilder(Charset charset) {
            // write entity to the builder
            final byte[] entity = baos.toByteArray();

            sb.append(new String(entity, 0, entity.length, charset));
            if (entity.length > maxEntitySize) {
                sb.append("...more...");
            }
            sb.append('\n');

            return sb;
        }

        @Override
        public void write(final int i) throws IOException {
            if (baos.size() <= maxEntitySize) {
                baos.write(i);
            }
            out.write(i);
        }
    }

    private void log(StringBuilder sb) {
        logger.info(sb.toString());
    }


}
