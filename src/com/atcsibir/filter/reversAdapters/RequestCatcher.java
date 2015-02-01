package com.atcsibir.filter.reversAdapters;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class RequestCatcher implements Filter
	{
		public void init(FilterConfig config) throws ServletException {}
		public void destroy() {}

		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
			{
				HttpServletRequest req = (HttpServletRequest) request;
				String requestedPath = req.getRequestURI();
				String adapterId = "377";

				if (requestedPath.equals("/adapter-web/ws/" + adapterId))
					{
						PrintWriter writer = response.getWriter();
						writer.print("Write another response");
					}
				else
					{
						chain.doFilter(request, response);
					}
			}
	}