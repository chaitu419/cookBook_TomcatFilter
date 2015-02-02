package com.atcsibir.filter;

import org.apache.commons.io.IOUtils;

import org.xml.sax.SAXException;

import javax.servlet.ServletInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.w3c.dom.Document;

public class XmlTools
	{
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
	}
