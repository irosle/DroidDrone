package com.chakravarty.httpServer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class httpServer {
	int port;
	ArrayList<pageHandler> list;
	private ServerSocket serverSocket;
	boolean isRunning = true;
	private BasicHttpProcessor httpproc;
	private BasicHttpContext httpContext;
	private HttpService httpService;
	private HttpRequestHandlerRegistry handlers;
	httpServer(int prt){
		port = prt;
		httpproc = new BasicHttpProcessor();
		httpContext = new BasicHttpContext();

		httpproc.addInterceptor(new ResponseDate());
		httpproc.addInterceptor(new ResponseServer());
		httpproc.addInterceptor(new ResponseContent());
		httpproc.addInterceptor(new ResponseConnControl());

		httpService = new HttpService(httpproc,
		    new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());

		handlers = new HttpRequestHandlerRegistry();
	}
	public void onRoute(String str, Object parent,Method method){
		pageHandler page = new pageHandler(parent, method);
		handlers.register(str, page);
	}
	public void start(){
		httpService.setHandlerResolver(handlers);
		
		//Store server in new thread
		Thread t = new Thread(new Runnable() {
	         public void run()
	         {
	        	 try {
	        		 //initiallize socket on port
	     			 serverSocket = new ServerSocket(port);

	     			 serverSocket.setReuseAddress(true);

	     			 while (isRunning) {
	     				try {
	     					final Socket socket = serverSocket.accept();

	     					DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();

	     					serverConnection.bind(socket, new BasicHttpParams());

	     					httpService.handleRequest(serverConnection, httpContext);

	     					serverConnection.shutdown();
	     				} catch (IOException e) {
	     					e.printStackTrace();
	     				} catch (HttpException e) {
	     					e.printStackTrace();
	     				}
	     			}

	     			serverSocket.close();
	     		} catch (SocketException e) {
	     			e.printStackTrace();
	     		} catch (IOException e) {
	     			e.printStackTrace();
	     		}

	     		isRunning = false;
	         }
		});
		t.start();
	}
	public synchronized void stopServer() {
		isRunning = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
