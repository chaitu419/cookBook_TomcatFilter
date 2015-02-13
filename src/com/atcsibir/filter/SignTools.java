package com.atcsibir.filter;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.*;

import java.security.PrivateKey;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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

						String bodyContent = XmlTools.getSoap2xBody(soap); // достаем содержание тега soap:Body
						String canonicalizedBodyContent = XmlTools.getCanonicalizedXml(bodyContent); // каноникализируем содержание тега soap:Body
						String digestValue = null;
						try {digestValue = getDigestValue(canonicalizedBodyContent.getBytes("UTF-8")); /* Получаем значение хэша сообщения */} catch (UnsupportedEncodingException e) {JOptionPane.showMessageDialog(null, e.getMessage());}

						Document document = null;
						try {document = DocumentHelper.parseText(soap); /* преобразовываем строку в XML */} catch (DocumentException e) {JOptionPane.showMessageDialog(null, e.getMessage());}

						Element wsseSecurity = XmlTools.getWsseSecurity(); // генерим блок подписи

						Element soapHeaderNode = (Element) document.selectSingleNode("/*/*[local-name()='Header']"); // селектим блолк хидер основного соапа
						Element signedInfoNode = (Element) wsseSecurity.selectSingleNode("/*/*/*[local-name()='SignedInfo']"); // селектим блок с хэшем. От него рассчитываемся ЭЦП
						Element digestValueNode = (Element) wsseSecurity.selectSingleNode("/*/*/*/*/*[local-name()='DigestValue']"); // селектим ноду для хэша
						Element signatureValueNode = (Element) wsseSecurity.selectSingleNode("/*/*/*[local-name()='SignatureValue']"); // селектим ноду для значения ЭЦП
						Element binarySecurityTokenNode = (Element) wsseSecurity.selectSingleNode("/*/*[local-name()='BinarySecurityToken']"); // селектим ноду для открытого сертификата ключа подписи

						digestValueNode.addText(digestValue); // втыкаем в ноду значение хэша
						String signatureValue = getSignatureValue(signedInfoNode.asXML()); // получаем значение ЭЦП
						signatureValueNode.addText(signatureValue); // втыкаем значение эцп
						binarySecurityTokenNode.addText(KeyStoreTools.getCryptoProJcpPublicKey()); // втыкаем открытый ключ

						soapHeaderNode.clearContent(); // удаляем предыдущую подпись
						soapHeaderNode.add(wsseSecurity); // втыкаем блок с подписью

					}
				catch (FileNotFoundException e) {JOptionPane.showMessageDialog(null, e.getMessage());} catch (IOException e) {JOptionPane.showMessageDialog(null, e.getMessage());}


				return signedXml;
			}

		private static String getSignatureValue(String soapPart) // метод, рассчитавающий значение ЭЦП
			{
				PrivateKey pk = KeyStoreTools.getCryptoProJcpPrivateKey(); // получаем значение закрытого ключа
				String canonicalizedSoapPart = XmlTools.getCanonicalizedXml(soapPart); // каноникализируем соап
				String signatureValue = null;
				Signature signatureDriver;
				try
					{
						signatureDriver = Signature.getInstance("GOST3411withGOST3410EL");

						signatureDriver.initSign(pk);
						signatureDriver.update(canonicalizedSoapPart.getBytes("UTF-8"));
						signatureValue = DatatypeConverter.printBase64Binary(signatureDriver.sign());
					}
				catch (NoSuchAlgorithmException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (SignatureException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (InvalidKeyException e){JOptionPane.showMessageDialog(null, e.getMessage());}catch (UnsupportedEncodingException e){JOptionPane.showMessageDialog(null, e.getMessage());}

				return signatureValue;
			}

		private static String getDigestValue(byte[] soapMessage) // метод, рассчитывающий хэш сумму сообщения и возвращающий ее в виде base64 строки
			{
				MessageDigest digestDriver = null;
				try
					{
						digestDriver = MessageDigest.getInstance("GOST3411"); // , "CryptoProvider"
					}
				catch (NoSuchAlgorithmException e){JOptionPane.showMessageDialog(null, e.getMessage());} // catch (NoSuchProviderException e){JOptionPane.showMessageDialog(null, e.getMessage());}
				digestDriver.update(soapMessage);
				return DatatypeConverter.printBase64Binary(digestDriver.digest());
			}


		private static String getCurrentTime() // генерит текущее вермя для ноды smev:Message / smev:Date
			{
				return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
			}
	}
