//========================================================================
//Copyright 2006 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package org.mortbay.jetty.example;

import java.io.IOException;

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
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.HandlerCollection;

public class ManyContexts
{
    public static void main(String[] args)
        throws Exception
    {
        Server server = new Server();
        Connector connector=new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        ContextHandler context0 = new ContextHandler();
        context0.setContextPath("/zero");
        Handler handler0=new HelloHandler();
        context0.setHandler(handler0);

        ContextHandler context1 = new ContextHandler();
        context1.setContextPath("/one");
        Handler handler1=new HelloHandler();
        context1.setHandler(handler1);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{context0,context1});

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{contexts});

        server.setHandler(handlers);

        server.start();
        server.join();
    }

    public static class HelloHandler extends AbstractHandler
    {
        static int h=0;
        int hello=h++;

        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException
        {
            Request base_request = (request instanceof Request) ? (Request)request:HttpConnection.getCurrentConnection().getRequest();
            base_request.setHandled(true);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/html");
            response.getWriter().println("<h1>Hello OneContext "+hello+"</h1>");
        }
    }

}
