package com.atcsibir.filter;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CfgEditor extends HttpServlet
	{
		public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
			{

			}

		public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
			{
				String currentCfg = IOUtils.toString(new FileInputStream(new File(System.getProperty("catalina.base") + "/conf/adpFilter/conf.json")), "UTF-8");;
				PrintWriter rsp = resp.getWriter();
				rsp.print("<html><head><title>Filter Cfg Editor</title><script type=\"text/javascript\" src=\"/radpf/js/jquery.js\"></script><script type=\"text/javascript\" src=\"/radpf/js/cfg.js\"></script></head><body><div id=\"currentCfg\"></div><div><input type=\"button\" value=\"Save\" id=\"saveCfg\" /> Status: <div id='status'></div></div><script type=\"text/javascript\">var currentCfg = " + currentCfg + ";</script></body></html>");
			}
	}
