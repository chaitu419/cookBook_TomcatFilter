package com.atcsibir.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

public class CfgEditor extends HttpServlet
	{
		private static final String cfgPath = System.getProperty("catalina.base") + "/conf/adpFilter/conf.json";

		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
			{
				InputStream is = req.getInputStream(); // входящий пост с json'ом с параметрами
				FileOutputStream fs = new FileOutputStream(new File(cfgPath));
				IOUtils.copy(is,fs);
				is.close();
				fs.close();
				resp.getWriter().print("Saved");
			}

		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
			{
				String currentCfg = IOUtils.toString(new FileInputStream(new File(cfgPath)), "UTF-8");
				resp.getWriter().print("<html><head><title>Filter Cfg Editor</title><script type=\"text/javascript\" src=\"/radpf/js/jquery.js\"></script><script type=\"text/javascript\" src=\"/radpf/js/cfg.js\"></script><style type=\"text/css\">.ctrl{width:210px;margin-bottom:7px;}</style></head><body><div id=\"currentCfg\"></div><div><input type=\"button\" value=\"Save\" id=\"saveCfg\" /> Status: <span id='status'></span></div><script type=\"text/javascript\">var currentCfg = " + currentCfg + ";</script></body></html>");
			}
	}
