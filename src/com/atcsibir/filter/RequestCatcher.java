package com.atcsibir.filter;

import java.io.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class RequestCatcher implements Filter
	{
		public static final String filterResourcesPath = System.getProperty("catalina.base") + "/conf/adpFilter";
		public static JsonObject filterCfg;

		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
			{
				HttpServletRequest req = (HttpServletRequest) request;

				String requestedMethod = req.getMethod().toUpperCase(); // Фильтруем мы только http post запросы
				String requestedAdapter = req.getRequestURI().replace("/adapter-web/ws/", ""); // тащим мнемонику адаптера

				if (requestedMethod.equals("POST") && !requestedAdapter.equals("pgu") && !requestedAdapter.equals("dynamic") && isActiveAdapter(requestedAdapter)) // провреряем, что вызван метод метод POST и вызывается нужный сервис
					{
						PrintWriter writer = response.getWriter();
						writer.print(XmlTools.buildCoolSoap(requestedAdapter, request.getInputStream())); // на вход подаем мнемонику адаптера и полученный соап
					}
				else
					{
						chain.doFilter(request, response); // отдаем оригинальный ответ
					}
			}

		private boolean isActiveAdapter(String requestedAdapter)
			{
				boolean isActive = false;

				try
					{
						JsonReader jsonReader = Json.createReader(new FileInputStream(new File(filterResourcesPath + "/conf.json"))); // Читаем файл с конфигом фильтра
						filterCfg = jsonReader.readObject();
						jsonReader.close();

						isActive = filterCfg.getBoolean(requestedAdapter);
					}
				catch (IOException e) {e.printStackTrace();} catch (NullPointerException e) {}

				return isActive;
			}

		// http://127.0.0.1/adapter-web/ws/minzdravsocrazvitia-373-no_posob
		// http://127.0.0.1/dictionary/edit

		public void init(FilterConfig config) throws ServletException {}
		public void destroy() {}
	}