package edu.luc.etl.javame.restful;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.log.Log;

public class Restful {

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		Connector connector = new SocketConnector();
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });

		ContextHandler context = new ContextHandler();
		context.setContextPath("/");
		Handler handler = new RestfulHandler();
		context.setHandler(handler);

		server.setHandler(context);

		server.start();
		server.join();
	}

	public static class RestfulHandler extends AbstractHandler {

		private Set set = new TreeSet();

		public void handle(String target, HttpServletRequest request,
				HttpServletResponse response, int dispatch) throws IOException,
				ServletException {
			Log.info(request.getMethod() + " " + request.getPathInfo() + " " + request.getHeader("Accept"));
			try {
				final Integer number = Integer.valueOf(request.getPathInfo()
						.substring(1));
				final String method = request.getMethod();
				if ("GET".equals(method)) {
					if (set.contains(number)) {
						response.setStatus(HttpServletResponse.SC_OK);
					} else {
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}
				} else if ("PUT".equals(method)) {
					set.add(number);
					response.setStatus(HttpServletResponse.SC_ACCEPTED);
				} else if ("POST".equals(method)) {
				} else if ("DELETE".equals(method)) {
				}
				Request base_request = (request instanceof Request) ? (Request) request
						: HttpConnection.getCurrentConnection().getRequest();
				base_request.setHandled(true);
				response.setContentType("text/html");
				response.getWriter().println("<h1>Hello</h1>");
				response.getWriter()
						.println(
								"<p>request method was " + request.getMethod()
										+ "</p>");
			} catch (final NumberFormatException ex) {
				Request base_request = (request instanceof Request) ? (Request) request
						: HttpConnection.getCurrentConnection().getRequest();
				base_request.setHandled(true);
				response.setContentType("text/html");
				response.getWriter().println("<h1>Goodbye</h1>");
				response.getWriter()
						.println(
								"<p>request method was " + request.getMethod()
										+ "</p>");
			}
		}
	}

}
