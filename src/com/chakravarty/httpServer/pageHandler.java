package com.chakravarty.httpServer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;


public class pageHandler implements HttpRequestHandler {
	private Method method;
	private Object o;
	pageHandler(Object parent, Method mth){
		method = mth;
		o = parent;
	}
	@Override
	public void handle(HttpRequest arg0, HttpResponse arg1, HttpContext arg2)
			throws HttpException, IOException {
		Object[] parameters = new Object[1];
		parameters[0] = "message";
		try {
			method.invoke(o,parameters[0]);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
