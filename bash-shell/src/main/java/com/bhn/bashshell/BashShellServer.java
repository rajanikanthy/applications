package com.bhn.bashshell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class BashShellServer extends AbstractHandler {

	private static final String CONF_FILE = "conf.properties";

	private enum ConfProperties {
		JETTY_PORT("jetty.port");

		String propKey;

		ConfProperties(String propKey) {
			this.propKey = propKey;
		}

		String getPropKey() {
			return this.propKey;
		}
	}

	public void handle(String target, Request request,
			HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) throws IOException,
			ServletException {
		String cmd = (String) servletRequest.getParameter("cmd");
		String args = (String) servletRequest.getParameter("args");

		servletResponse.setContentType("text/html;charset=utf-8");
		servletResponse.setStatus(HttpServletResponse.SC_OK);
		request.setHandled(true);
		servletResponse.getWriter().println("<hr/>");
		servletResponse.getWriter().println(
				"Command: " + cmd + "<br/>" + "Args: " + args);
		servletResponse.getWriter().println("<hr/>");
		servletResponse.getWriter().println(executeCommand(cmd, args));
	}

	private String executeCommand(String command, String args) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command + " " + args);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line).append("<br/>");
			}
		} catch (Exception e) {
			output.delete(0, output.length());
			output.append(e.getMessage());
		}
		return output.toString();
	}

	public static void main(String[] args) throws Exception {

		Properties configuration = new Properties();
		configuration.load(BashShellServer.class.getClassLoader()
				.getResourceAsStream(CONF_FILE));
		Server server = new Server(Integer.parseInt(configuration
				.getProperty(ConfProperties.JETTY_PORT.getPropKey())));
		server.setHandler(new BashShellServer());

		server.start();
		server.join();
	}

}
