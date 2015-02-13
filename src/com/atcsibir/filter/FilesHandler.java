package com.atcsibir.filter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.JOptionPane;
import java.io.*;

public class FilesHandler
	{
		public static String soapsFolderLabel;
		public static String mnemonicCode;
		public static String mnemonicName;
		public static String oktmo;
		public static String cryptoProJcpTokenPassword;

		public static void startSigning(File[] filesMassive) // первый вызов из лиссенера FrameTools.initListeners() для кнопы goSign
			{
				for (File nextFile : filesMassive)
					{
						if (nextFile.isFile())
							{
								try
									{
										if (FilenameUtils.getExtension(nextFile.getAbsolutePath()).equals("xml"))
											{
												InputStream is = SignTools.sign(nextFile);
												if (is != null)
													{
														FileOutputStream fos = new FileOutputStream(nextFile);
														IOUtils.copy(is, fos);
														fos.close();
													}
												is.close();
											}
									}
								catch (FileNotFoundException e) {JOptionPane.showMessageDialog(null, e.getMessage());} catch (IOException e) {JOptionPane.showMessageDialog(null, e.getMessage());}
							}
						else if (nextFile.isDirectory())
							{
								startSigning(nextFile.listFiles());
							}
					}
			}

		public static boolean formValidate() // проверяем, что все поляшечки на форме заполнены
			{
				FrameTools ft = null;

				boolean isValid = true; // флаг того, что все поля заполнены..перед запуском процедуры сайна
				String errorMessage = "Заполни следующее инфо:\n\n";

				soapsFolderLabel = ft.soapsFolderLabel.getText();
				mnemonicCode = ft.mnemonicCode.getText();
				mnemonicName = ft.mnemonicName.getText();
				oktmo = ft.oktmo.getText();
				cryptoProJcpTokenPassword = ft.cryptoProJcpTokenPassword.getText();

				if (soapsFolderLabel.equals("")) {errorMessage = errorMessage + "Папка с соапами\n"; isValid = false;}
				//if (mnemonicCode.equals("")) {errorMessage = errorMessage + "Код мнемоники\n"; isValid = false;}
				//if (mnemonicName.equals("")) {errorMessage = errorMessage + "Наименование мнемоники\n"; isValid = false;}
				//if (oktmo.equals("")) {errorMessage = errorMessage + "ОКТМО\n"; isValid = false;}
				//if (cryptoProJcpTokenPassword.equals("")) {errorMessage = errorMessage + "JCP ключ (пароль)\n"; isValid = false;}

				if (!isValid) JOptionPane.showMessageDialog(null, errorMessage);

				return isValid;
			}
	}
