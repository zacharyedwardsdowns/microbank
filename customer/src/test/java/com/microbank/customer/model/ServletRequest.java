package com.microbank.customer.model;

import jakarta.servlet.*;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@SuppressWarnings("deprecation")
public class ServletRequest implements HttpServletRequest {
  private final String requestUri;
  private HttpHeaders headers;
  private HttpMethod method;

  public ServletRequest(final String requestUri) {
    this.requestUri = requestUri;
  }

  @Override
  public String getAuthType() {
    return null;
  }

  @Override
  public Cookie[] getCookies() {
    return new Cookie[0];
  }

  @Override
  public long getDateHeader(String name) {
    return 0;
  }

  @Override
  public String getHeader(String name) {
    return headers.getFirst(name);
  }

  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return null;
  }

  @Override
  public int getIntHeader(String name) {
    return 0;
  }

  @Override
  public String getMethod() {
    return this.method.name();
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  @Override
  public String getPathInfo() {
    return null;
  }

  @Override
  public String getPathTranslated() {
    return null;
  }

  @Override
  public String getContextPath() {
    return null;
  }

  @Override
  public String getQueryString() {
    return null;
  }

  @Override
  public String getRemoteUser() {
    return null;
  }

  @Override
  public boolean isUserInRole(String role) {
    return false;
  }

  @Override
  public Principal getUserPrincipal() {
    return null;
  }

  @Override
  public String getRequestedSessionId() {
    return null;
  }

  @Override
  public String getRequestURI() {
    return requestUri;
  }

  @Override
  public StringBuffer getRequestURL() {
    return null;
  }

  @Override
  public String getServletPath() {
    return null;
  }

  @Override
  public HttpSession getSession(boolean create) {
    return null;
  }

  @Override
  public HttpSession getSession() {
    return null;
  }

  @Override
  public String changeSessionId() {
    return null;
  }

  @Override
  public boolean isRequestedSessionIdValid() {
    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  @Override
  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  @Override
  public boolean authenticate(HttpServletResponse response) {
    return false;
  }

  @Override
  public void login(String username, String password) {}

  @Override
  public void logout() {}

  @Override
  public Collection<Part> getParts() {
    return null;
  }

  @Override
  public Part getPart(String name) {
    return null;
  }

  @Override
  public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) {
    return null;
  }

  @Override
  public Object getAttribute(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getAttributeNames() {
    return null;
  }

  @Override
  public String getCharacterEncoding() {
    return null;
  }

  @Override
  public void setCharacterEncoding(String env) {}

  @Override
  public int getContentLength() {
    return 0;
  }

  @Override
  public long getContentLengthLong() {
    return 0;
  }

  @Override
  public String getContentType() {
    return null;
  }

  @Override
  public ServletInputStream getInputStream() {
    return null;
  }

  @Override
  public String getParameter(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return null;
  }

  @Override
  public String[] getParameterValues(String name) {
    return new String[0];
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    return null;
  }

  @Override
  public String getProtocol() {
    return null;
  }

  @Override
  public String getScheme() {
    return null;
  }

  @Override
  public String getServerName() {
    return null;
  }

  @Override
  public int getServerPort() {
    return 0;
  }

  @Override
  public BufferedReader getReader() {
    return null;
  }

  @Override
  public String getRemoteAddr() {
    return null;
  }

  @Override
  public String getRemoteHost() {
    return null;
  }

  @Override
  public void setAttribute(String name, Object o) {}

  @Override
  public void removeAttribute(String name) {}

  @Override
  public Locale getLocale() {
    return null;
  }

  @Override
  public Enumeration<Locale> getLocales() {
    return null;
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public RequestDispatcher getRequestDispatcher(String path) {
    return null;
  }

  @Override
  public int getRemotePort() {
    return 0;
  }

  @Override
  public String getLocalName() {
    return null;
  }

  @Override
  public String getLocalAddr() {
    return null;
  }

  @Override
  public int getLocalPort() {
    return 0;
  }

  @Override
  public ServletContext getServletContext() {
    return null;
  }

  @Override
  public AsyncContext startAsync() throws IllegalStateException {
    return null;
  }

  @Override
  public AsyncContext startAsync(
      jakarta.servlet.ServletRequest servletRequest, ServletResponse servletResponse)
      throws IllegalStateException {
    return null;
  }

  @Override
  public boolean isAsyncStarted() {
    return false;
  }

  @Override
  public boolean isAsyncSupported() {
    return false;
  }

  @Override
  public AsyncContext getAsyncContext() {
    return null;
  }

  @Override
  public DispatcherType getDispatcherType() {
    return null;
  }

  @Override
  public String getRequestId() {
    return null;
  }

  @Override
  public String getProtocolRequestId() {
    return null;
  }

  @Override
  public ServletConnection getServletConnection() {
    return null;
  }
}
