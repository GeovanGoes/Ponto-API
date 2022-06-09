package br.com.geovan.Ponto.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



class HttpLoggingFilter implements Filter {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpLoggingFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
			BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);
			logRequest(httpServletRequest, bufferedRequest);
			filterChain.doFilter(bufferedRequest, bufferedResponse);
			logResponse(httpServletResponse, bufferedResponse);
		} catch (Throwable a) {
			LOG.error(a.getMessage());
		}
	}
	
	@Override
	public void destroy() {
		
	}
	
	private static void logRequest(HttpServletRequest httpServletRequest, BufferedRequestWrapper bufferedRequest) {
		try {
			try {
				LOG.info(LoggerUtils.getLogRequest(httpServletRequest.getServletPath(), httpServletRequest.getMethod(), LoggerUtils.getHeaders(httpServletRequest), LoggerUtils.getParameters(httpServletRequest), bufferedRequest.getRequestBody()));
			} catch (Exception e) {
				LOG.info(LoggerUtils.getLogRequest(httpServletRequest.getServletPath(), httpServletRequest.getMethod(), LoggerUtils.getHeaders(httpServletRequest), LoggerUtils.getParameters(httpServletRequest), bufferedRequest.getRequestBody()));
			}
		} catch (Exception e) {
			LOG.error("Problemas ao gerar request log", e);
		}
	}
	
	private static void logResponse(HttpServletResponse httpServletResponse, BufferedResponseWrapper bufferedResponse) {
		try {
			try {
				LOG.info(LoggerUtils.getLogResponse(httpServletResponse.getStatus(), LoggerUtils.getHeaders(httpServletResponse), bufferedResponse.getContent()));
			} catch (Exception e) {
				LOG.info(LoggerUtils.getLogResponse(httpServletResponse.getStatus(), LoggerUtils.getHeaders(httpServletResponse), bufferedResponse.getContent()));
			}
		} catch (Exception e) {
			LOG.error("Problemas ao gerar request log", e);
		}
	}
	
	private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {
		
		private ByteArrayInputStream byteArrayInputStream = null;
		private ByteArrayOutputStream byteArrayOutputStream = null;
		private BufferedServletInputStream bufferedServletInputStream = null;
		private byte[] buffer = null;
		
		public BufferedRequestWrapper(HttpServletRequest httpServletRequest) throws IOException {
			super(httpServletRequest);
			// Read InputStream and store its content in a buffer.
			InputStream inputStream = httpServletRequest.getInputStream();
			this.byteArrayOutputStream = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int read;
			while ((read = inputStream.read(buf)) > 0) {
				this.byteArrayOutputStream.write(buf, 0, read);
			}
			this.buffer = this.byteArrayOutputStream.toByteArray();
		}
		
		@Override
		public ServletInputStream getInputStream() {
			this.byteArrayInputStream = new ByteArrayInputStream(this.buffer);
			this.bufferedServletInputStream = new BufferedServletInputStream(this.byteArrayInputStream);
			return this.bufferedServletInputStream;
		}
		
		String getRequestBody() throws IOException {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getInputStream()));
			String line = null;
			StringBuilder inputBuffer = new StringBuilder();
			do {
				line = bufferedReader.readLine();
				if (null != line) {
					inputBuffer.append(line.trim());
				}
			} while (line != null);
			bufferedReader.close();
			return inputBuffer.toString().trim();
		}
		
	}
	
	private static final class BufferedServletInputStream extends ServletInputStream {
		
		private ByteArrayInputStream byteArrayInputStream;
		
		public BufferedServletInputStream(ByteArrayInputStream byteArrayInputStream) {
			this.byteArrayInputStream = byteArrayInputStream;
		}
		
		@Override
		public int available() {
			return this.byteArrayInputStream.available();
		}
		
		@Override
		public int read() {
			return this.byteArrayInputStream.read();
		}
		
		@Override
		public int read(byte[] buf, int off, int len) {
			return this.byteArrayInputStream.read(buf, off, len);
		}
		
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
			
		}
		
	}
	
	public class TeeServletOutputStream extends ServletOutputStream {
		
		private final TeeOutputStream teeOutputStream;
		
		public TeeServletOutputStream(OutputStream outputStream1, OutputStream outputStream2) {
			teeOutputStream = new TeeOutputStream(outputStream1, outputStream2);
		}
		
		@Override
		public void write(int arg0) throws IOException {
			this.teeOutputStream.write(arg0);
		}
		
		public void flush() throws IOException {
			super.flush();
			this.teeOutputStream.flush();
		}
		
		public void close() throws IOException {
			super.close();
			this.teeOutputStream.close();
		}
		
		@Override
		public boolean isReady() {
			return false;
		}
		
		@Override
		public void setWriteListener(WriteListener writeListener) {
			
		}
		
	}
	
	public class BufferedResponseWrapper implements HttpServletResponse {
		
		HttpServletResponse httpServletResponse;
		TeeServletOutputStream teeServletOutputStream;
		ByteArrayOutputStream byteArrayOutputStream;
		
		public BufferedResponseWrapper(HttpServletResponse httpServletResponse) {
			this.httpServletResponse = httpServletResponse;
		}
		
		public String getContent() {
			return byteArrayOutputStream != null ? byteArrayOutputStream.toString() : "";
		}
		
		public PrintWriter getWriter() throws IOException {
			return httpServletResponse.getWriter();
		}
		
		public ServletOutputStream getOutputStream() throws IOException {
			if (teeServletOutputStream == null) {
				byteArrayOutputStream = new ByteArrayOutputStream();
				teeServletOutputStream = new TeeServletOutputStream(httpServletResponse.getOutputStream(), byteArrayOutputStream);
			}
			return teeServletOutputStream;
		}
		
		@Override
		public String getCharacterEncoding() {
			return httpServletResponse.getCharacterEncoding();
		}
		
		@Override
		public String getContentType() {
			return httpServletResponse.getContentType();
		}
		
		@Override
		public void setCharacterEncoding(String charset) {
			httpServletResponse.setCharacterEncoding(charset);
		}
		
		@Override
		public void setContentLength(int len) {
			httpServletResponse.setContentLength(len);
		}
		
		@Override
		public void setContentLengthLong(long l) {
			httpServletResponse.setContentLengthLong(l);
		}
		
		@Override
		public void setContentType(String type) {
			httpServletResponse.setContentType(type);
		}
		
		@Override
		public void setBufferSize(int size) {
			httpServletResponse.setBufferSize(size);
		}
		
		@Override
		public int getBufferSize() {
			return httpServletResponse.getBufferSize();
		}
		
		@Override
		public void flushBuffer() throws IOException {
			teeServletOutputStream.flush();
		}
		
		@Override
		public void resetBuffer() {
			httpServletResponse.resetBuffer();
		}
		
		@Override
		public boolean isCommitted() {
			return httpServletResponse.isCommitted();
		}
		
		@Override
		public void reset() {
			httpServletResponse.reset();
		}
		
		@Override
		public void setLocale(Locale loc) {
			httpServletResponse.setLocale(loc);
		}
		
		@Override
		public Locale getLocale() {
			return httpServletResponse.getLocale();
		}
		
		@Override
		public void addCookie(Cookie cookie) {
			httpServletResponse.addCookie(cookie);
		}
		
		@Override
		public boolean containsHeader(String name) {
			return httpServletResponse.containsHeader(name);
		}
		
		@Override
		public String encodeURL(String url) {
			return httpServletResponse.encodeURL(url);
		}
		
		@Override
		public String encodeRedirectURL(String url) {
			return httpServletResponse.encodeRedirectURL(url);
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public String encodeUrl(String url) {
			return httpServletResponse.encodeUrl(url);
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public String encodeRedirectUrl(String url) {
			return httpServletResponse.encodeRedirectUrl(url);
		}
		
		@Override
		public void sendError(int sc, String msg) throws IOException {
			httpServletResponse.sendError(sc, msg);
		}
		
		@Override
		public void sendError(int sc) throws IOException {
			httpServletResponse.sendError(sc);
		}
		
		@Override
		public void sendRedirect(String location) throws IOException {
			httpServletResponse.sendRedirect(location);
		}
		
		@Override
		public void setDateHeader(String name, long date) {
			httpServletResponse.setDateHeader(name, date);
		}
		
		@Override
		public void addDateHeader(String name, long date) {
			httpServletResponse.addDateHeader(name, date);
		}
		
		@Override
		public void setHeader(String name, String value) {
			httpServletResponse.setHeader(name, value);
		}
		
		@Override
		public void addHeader(String name, String value) {
			httpServletResponse.addHeader(name, value);
		}
		
		@Override
		public void setIntHeader(String name, int value) {
			httpServletResponse.setIntHeader(name, value);
		}
		
		@Override
		public void addIntHeader(String name, int value) {
			httpServletResponse.addIntHeader(name, value);
		}
		
		@Override
		public void setStatus(int sc) {
			httpServletResponse.setStatus(sc);
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void setStatus(int sc, String sm) {
			httpServletResponse.setStatus(sc, sm);
		}
		
		@Override
		public String getHeader(String arg0) {
			return httpServletResponse.getHeader(arg0);
		}
		
		@Override
		public Collection<String> getHeaderNames() {
			return httpServletResponse.getHeaderNames();
		}
		
		@Override
		public Collection<String> getHeaders(String arg0) {
			return httpServletResponse.getHeaders(arg0);
		}
		
		@Override
		public int getStatus() {
			return httpServletResponse.getStatus();
		}
		
	}
	
}
