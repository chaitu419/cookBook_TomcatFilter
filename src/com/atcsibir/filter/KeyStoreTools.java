package com.atcsibir.filter;

import javax.swing.JOptionPane;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.Enumeration;

public class KeyStoreTools
	{
		private static KeyStore cryptoProJcpKeyStore; // кейстор для ключей Крипто-ПРО

		public static void InitKeyStores() // Загружаем криптоПрошный кейстор
			{
				FrameTools ft = null;

				if (hasCryptoProJcp() != null) // проверяем, установлено ли Крипто-ПРО JCP
					{
						ft.cryptoProJcpTokensList.addItem("Загружаем..");
						try
							{
								cryptoProJcpKeyStore = KeyStore.getInstance("HDImageStore"); // загружаем Крипто-ПРО-шный кейстор
								cryptoProJcpKeyStore.load(null, null);
								Enumeration cryptoProJcpEnum = cryptoProJcpKeyStore.aliases(); // получаем список токенов

								ft.cryptoProJcpTokensList.removeItemAt(0); // удаляем строку с загрузкой

								while (cryptoProJcpEnum.hasMoreElements())
									{
										Object currentToken = cryptoProJcpEnum.nextElement();
										ft.cryptoProJcpTokensList.addItem(currentToken);
									}
							}
						catch (KeyStoreException e){JOptionPane.showMessageDialog(null, "KeyStoreTools.InitKeyStores(). CryproPro JCP. KeyStoreException.");}catch (CertificateException e){JOptionPane.showMessageDialog(null, "KeyStoreTools.InitKeyStores(). CryproPro JCP. CertificateException.");}catch (NoSuchAlgorithmException e){JOptionPane.showMessageDialog(null, "KeyStoreTools.InitKeyStores(). CryproPro JCP. NoSuchAlgorithmException.");}catch (IOException e){JOptionPane.showMessageDialog(null, "KeyStoreTools.InitKeyStores(). CryproPro JCP. IOException.");}
					}
				else
					{
						JOptionPane.showMessageDialog(null, "Не найдены исполняемые классы CryptoPro JCP");
					}
			}

		private static Class hasCryptoProJcp() // Проверяем, установлено ли CryptoPro JCP
			{
				try {return Class.forName("ru.CryptoPro.JCP.JCP");} catch (Exception e) {return null;}
			}
	}
