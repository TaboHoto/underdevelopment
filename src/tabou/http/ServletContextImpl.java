package tabou.http;;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Locale;
import java.util.Set;

public class ServletContextImpl implements ServletContext {

    @Override
    public ServletContext getContext(String uripath) {
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 3;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public String getMimeType(String file) {
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return null;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    @Override
    @Deprecated
    public Servlet getServlet(String name) throws ServletException {
        return null;
    }

    @Override
    @Deprecated
    public Enumeration<Servlet> getServlets() {
        return null;
    }

    @Override
    @Deprecated
    public Enumeration<String> getServletNames() {
        return null;
    }

    @Override
    public void log(String message) {
    }

    @Override
    @Deprecated
    public void log(Exception exception, String message) {
    }

    @Override
    public void log(String message, Throwable throwable) {
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public String getServerInfo() {
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
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
    public void setAttribute(String name, Object object) {
    }

    @Override
    public void removeAttribute(String name) {
    }

    @Override
    public String getServletContextName() {
        return null;
    }

}
