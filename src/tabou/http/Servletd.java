/* Copyright(c) 2013 M Hata
   This software is released under the MIT License.
   http://opensource.org/licenses/mit-license.php */
package tabou.http;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.net.ServerSocket;
import java.net.Socket;
import tabou.log.TabouLog;
import static tabou.log.TabouLog.Log;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;

public class Servletd {
    static private final int DEFAULT_PROXY_PORT = 8080;
    private ServerSocket serverSocket;

    public static void usage() {
        System.err.println("usage: java tabou.http.EchoHttp [-p port no]");
        System.exit(-1);
    }
    public static void main(String[] args) throws Exception{
        TabouLog.init();
        int localPort = DEFAULT_PROXY_PORT;  /* port no       */
        Servletd servletd = new Servletd();
        int argi = 0;
        for (; argi < args.length; argi++) {
            char[] chars = args[argi].toCharArray();
            if (chars[0] != '-') {
                break;
            }
            if (chars.length != 2) {
                System.err.println("invalid option:" + args[argi]);
                usage();
            }
            char c = chars[1];
            switch (c) {
            case 'p':
                localPort = Integer.parseInt(args[++argi]);  /*port no       */
                break;
            default:
                System.err.println("invalid option:" + c);
                usage();
            }
        }
        servletd.accrpt(localPort);
    }
    public void accrpt(int localPort) throws IOException{
        this.serverSocket = new ServerSocket(localPort);
        while (true) {
            Log.info("wait:*."+ localPort  +" ...");
            Socket requestSocket = this.serverSocket.accept();
            Log.info("accept:" + requestSocket.getInetAddress());
            try{
                request(requestSocket);
            }catch(Exception e){
                Log.log(Level.WARNING,e.toString(),e);
            }finally{
                requestSocket.close();
            }
            Log.info("close");
        }
    }
    public void close() throws IOException{
        this.serverSocket.close();
    }
    public void request(Socket requestSocket) throws ServletException,IOException{
        requestSocket.setSoTimeout(1000 * 100);

        ServletInputStreamImpl servletInputStream = new ServletInputStreamImpl(new BufferedInputStream(requestSocket.getInputStream()));
        ServletOutputStream servletOutputStream = new ServletOutputStreamImpl(requestSocket.getOutputStream());
        String firstLine = servletInputStream.readLine();
        Log.info(firstLine);
        int contentLength = 0;
        while(true){
            String line = servletInputStream.readLine();
            if(line == null){
                break;
            }else if(line.equals("")){
                break;
            }
            Log.info(line);
            int index = line.indexOf(':');
            String tagName = line.substring(0,index).toUpperCase();
            String value   = line.substring(index +1).trim();
            if(tagName.equals("CONTENT-LENGTH")){
                contentLength = Integer.parseInt(value);
            }
        }

        for(int i = 0;i < contentLength;i++){
            int c = servletInputStream.read();
            if(c < 0){
                break;
            }
//            byteArrayOutputStream.write(c);
        }
        HelloServlet httpServlet         = new HelloServlet();
        ServletContext servletContext    = new ServletContextImpl();
        HttpServletRequestImpl request   = new HttpServletRequestImpl(servletContext);
        HttpServletResponseImpl response = new HttpServletResponseImpl();
        response.setHeader("Connection","close");
        response.setStream(servletOutputStream);
        httpServlet.doGet(request,response);
        response.getWriter().close();
        servletOutputStream.close();
        servletInputStream.close();
    }
}
