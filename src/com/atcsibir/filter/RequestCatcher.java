package com.atcsibir.filter;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
						writer.print(buildCoolSoap(requestedAdapter, request.getInputStream())); // на вход подаем мнемонику адаптера и полученный соап
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

		public static String buildCoolSoap(String requestedAdapter, ServletInputStream incomeSoap)
			{
				String response = null;

				try
					{

						DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder builder = builderFactory.newDocumentBuilder();
						Document document = builder.parse(incomeSoap);
						XPath xPath =  XPathFactory.newInstance().newXPath();

						String testMsg = xPath.compile("/*/*/*/*[local-name()='Message']/*[local-name()='TestMsg']").evaluate(document);
						String messageId = xPath.compile("/*/*/*[local-name()='Header']/*[local-name()='MessageId']").evaluate(document);

						if (messageId.length() == 0) messageId = UUID.randomUUID().toString();

						response = IOUtils.toString(new FileInputStream(new File(RequestCatcher.filterResourcesPath + "/xml/" + requestedAdapter + "/" + testMsg)), "UTF-8"); // читаем соап из файла

						response = response.replace("REPLACE_TO_MNEMONIC_CODE", RequestCatcher.filterCfg.getString("mnemonicCode"))
								.replace("REPLACE_TO_MNEMONIC_NAME", RequestCatcher.filterCfg.getString("mnemonicName"))
								.replace("REPLACE_TO_OKTMO", RequestCatcher.filterCfg.getString("oktmo"))
								.replace("REPLACE_TO_CURRENT_TIME", getCurrentTime())
								.replace("REPLACE_TO_SMEV_ID", messageId);
					}
				catch (IOException e) {e.printStackTrace();} catch (ParserConfigurationException e) {e.printStackTrace();} catch (SAXException e) {e.printStackTrace();} catch (XPathExpressionException e) {e.printStackTrace();}

				return response;
			}

		private static String getCurrentTime() // генерит текущее вермя для ноды smev:Message / smev:Date
		{
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		}

		// http://127.0.0.1/adapter-web/ws/minzdravsocrazvitia-373-no_posob
		// http://127.0.0.1/dictionary/edit

		public void init(FilterConfig config) throws ServletException {}
		public void destroy() {}
	}