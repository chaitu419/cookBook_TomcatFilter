package com.atcsibir.filter;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;

import javax.swing.JOptionPane;

public class SignTools
	{
		public static InputStream sign(File nextFile) // на вход получаем подписываемый файл
			{
				InputStream signedXml = null;

				try
					{
						String soap = IOUtils.toString(new FileInputStream(nextFile));

						soap = soap.replace("REPLACE_TO_MNEMONIC_CODE", FilesHandler.mnemonicCode)
								.replace("REPLACE_TO_MNEMONIC_NAME", FilesHandler.mnemonicName)
								.replace("REPLACE_TO_OKTMO", FilesHandler.oktmo)
								.replace("REPLACE_TO_CURRENT_TIME", getCurrentTime())
								.replace("\t", "").replace("  ", "").replace(" <", "<").replace("\r", "").replace("\n", "");


					}
				catch (FileNotFoundException e) {JOptionPane.showMessageDialog(null, e.getMessage());} catch (IOException e) {JOptionPane.showMessageDialog(null, e.getMessage());}


				return signedXml;
			}



		private static String getCurrentTime() // генерит текущее вермя для ноды smev:Message / smev:Date
			{
				return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
			}
	}
