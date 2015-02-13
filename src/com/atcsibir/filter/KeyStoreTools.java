package com.atcsibir.filter;

import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;
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
						catch (KeyStoreException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (CertificateException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (NoSuchAlgorithmException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (IOException e){JOptionPane.showMessageDialog(null, e.getMessage());}
					}
				else
					{
						JOptionPane.showMessageDialog(null, "Не найдены исполняемые классы CryptoPro JCP");
					}
			}

		public static PrivateKey getCryptoProJcpPrivateKey() // закрытый ключ Крипто-ПРО JCP
			{
				String alias = FrameTools.cryptoProJcpTokensList.getSelectedItem().toString();
				PrivateKey privateKey = null;

				try
					{
						privateKey = (PrivateKey) cryptoProJcpKeyStore.getKey(alias, FrameTools.cryptoProJcpTokenPassword.getPassword());
					}
				catch (KeyStoreException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (NoSuchAlgorithmException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (UnrecoverableKeyException e){JOptionPane.showMessageDialog(null, e.getMessage());}

				return privateKey;
			}

		public static String getCryptoProJcpPublicKey () // открытый ключ Крипто-ПРО JCP
			{
				String alias = FrameTools.cryptoProJcpTokensList.getSelectedItem().toString();
				X509Certificate cert;
				String publicKey = null;
				try
					{
						cert = (X509Certificate) cryptoProJcpKeyStore.getCertificate(alias);
						publicKey = DatatypeConverter.printBase64Binary(cert.getEncoded()); // возвращаем base64 строку с открытым сертификатом
					}
				catch (KeyStoreException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (CertificateEncodingException e){JOptionPane.showMessageDialog(null, e.getMessage());}
				return publicKey;
			}

		private static Class hasCryptoProJcp() // Проверяем, установлено ли CryptoPro JCP
			{
				try {return Class.forName("ru.CryptoPro.JCP.JCP");} catch (Exception e) {return null;}
			}
	}
