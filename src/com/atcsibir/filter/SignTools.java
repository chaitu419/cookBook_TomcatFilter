package com.atcsibir.filter;

import java.io.*;

import org.apache.commons.io.IOUtils;

public class SignTools
	{
		public static InputStream sign(File nextFile) // на вход получаем подписываемый файл
			{
				try
					{
						String soap = IOUtils.toString(new FileInputStream(nextFile));

						soap = soap.replace("REPLACE_TO_MNEMONIC_CODE", FilesHandler.mnemonicCode)
								.replace("REPLACE_TO_MNEMONIC_NAME", FilesHandler.mnemonicName)
								.replace("REPLACE_TO_OKTMO", FilesHandler.oktmo)
								.replace("REPLACE_TO_CURRENT_TIME", XmlTools.getCurrentTime())
								.replace("\t", "").replace("  ", "").replace(" <", "<").replace("\r", "").replace("\n", "");


					}
				catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}


				return null;
			}
	}
