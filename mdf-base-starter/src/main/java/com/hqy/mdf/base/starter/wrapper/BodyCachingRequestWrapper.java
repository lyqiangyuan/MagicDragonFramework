package com.hqy.mdf.base.starter.wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * body缓存请求包装器，把请求体的内容缓存下来
 * @author hqy
 */
public class BodyCachingRequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody;
    private BufferedReader reader;

    public BodyCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public byte[] getRequestBody() throws IOException {
        requestBody = StreamUtils.copyToByteArray(super.getInputStream());
        return requestBody;
    }

    @Override
    public BufferedReader getReader() throws IOException{
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(getInputStream(), super.getCharacterEncoding()));
        }
        return reader;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (requestBody == null) {
            requestBody = StreamUtils.copyToByteArray(super.getInputStream());
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        //参照springmvc  org.springframework.web.servlet.function.DefaultServerRequestBuilder.BodyInputStream
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return bais.read(b, off, len);
            }

            @Override
            public int read(byte[] b) throws IOException {
                return bais.read(b);
            }

            @Override
            public long skip(long n) throws IOException {
                return bais.skip(n);
            }

            @Override
            public int available() throws IOException {
                return bais.available();
            }

            @Override
            public void close() throws IOException {
                bais.close();
            }

            @Override
            public synchronized void mark(int readlimit) {
                bais.mark(readlimit);
            }

            @Override
            public synchronized void reset() throws IOException {
                bais.reset();
            }

            @Override
            public boolean markSupported() {
                return bais.markSupported();
            }
        };
    }

}
