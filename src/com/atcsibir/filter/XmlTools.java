package com.atcsibir.filter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.swing.JOptionPane;

public class XmlTools
	{
		public static Element getWsseSecurity() // Метод, генерирующий скелет для электронной подписи по методическим рекомендациям 2.х
		{
			Document xmlDoc = DocumentHelper.createDocument();

				/* Генерируем корневой элемент */

			Element wsseSecurity = xmlDoc.addElement("wsse:Security");

			wsseSecurity.addAttribute("soap:actor", "http://smev.gosuslugi.ru/actors/smev");
			wsseSecurity.addAttribute("xmlns:soap", "soap");

				/* Добавляем основные неймспейсы */

			wsseSecurity.addNamespace("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
			wsseSecurity.addNamespace("ds", "http://www.w3.org/2000/09/xmldsig#");

				/* Генерируем блок, в котором будет размещаться открытый сертификат токена, закодированный в base64 */

			Element wsseBinarySecurityToken = wsseSecurity.addElement("wsse:BinarySecurityToken");

			wsseBinarySecurityToken.addAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
			wsseBinarySecurityToken.addAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
			wsseBinarySecurityToken.addAttribute("wsu:Id", "CertId-1");
			wsseBinarySecurityToken.addAttribute("xmlns:wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
			wsseBinarySecurityToken.addAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

				/* Генерируем блок, в котором будет размещаться значение ХЭШа и ЭЦП */

			Element dsSignature = wsseSecurity.addElement("ds:Signature");

			dsSignature.addAttribute("Id", "Signature-1");

				/* Генерируем блок для Хэша и ЭЦП */

			Element dsSignedInfo = dsSignature.addElement("ds:SignedInfo");
			Element dsCanonicalizationMethod = dsSignedInfo.addElement("ds:CanonicalizationMethod");
			Element dsSignatureMethod = dsSignedInfo.addElement("ds:SignatureMethod");
			Element dsReference = dsSignedInfo.addElement("ds:Reference");

			Element dsTransforms = dsReference.addElement("ds:Transforms");
			Element dsTransform = dsTransforms.addElement("ds:Transform");
			Element dsDigestMethod = dsReference.addElement("ds:DigestMethod");
			dsReference.addElement("ds:DigestValue"); // В этой ноде будет значение ХЭШа

			dsCanonicalizationMethod.addAttribute("Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#");
			dsSignatureMethod.addAttribute("Algorithm", "http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411");
			dsReference.addAttribute("URI", "#body");
			dsTransform.addAttribute("Algorithm", "http://www.w3.org/2001/10/xml-exc-c14n#");
			dsDigestMethod.addAttribute("Algorithm", "http://www.w3.org/2001/04/xmldsig-more#gostr3411");
			dsSignature.addElement("ds:SignatureValue"); // В этой ноде будет значение ЭЦП

				/* Добавляем ноду с информацией о токене */

			Element dsKeyInfo = dsSignature.addElement("ds:KeyInfo");
			Element wsseSecurityTokenReference = dsKeyInfo.addElement("wsse:SecurityTokenReference");
			Element wsseReference = wsseSecurityTokenReference.addElement("wsse:Reference");

			dsKeyInfo.addAttribute("Id", "KeyId-1");
			wsseSecurityTokenReference.addAttribute("wsu:Id", "STRId-1");
			wsseSecurityTokenReference.addAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
			wsseReference.addAttribute("URI", "#CertId-1");
			wsseReference.addAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");

			return wsseSecurity;
		}

		public static String getCanonicalizedXml (String uncanonicalizedXml) // метод каноникализирующий соап пакет
			{
				String canonicalizedXml = null;
				try {canonicalizedXml = new String(MassXmlSign.canon.canonicalize(uncanonicalizedXml.getBytes("UTF-8")), "UTF-8");} catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
				return canonicalizedXml;
			}

		public static String getSoap2xBody (String soapMessage) // данный метод достает из соапа тег soap:Body и его содержимое
			{
				Document document = null;
				try {document = DocumentHelper.parseText(soapMessage); /* преобразовываем строку в XML */} catch (DocumentException e) {JOptionPane.showMessageDialog(null, e.getMessage());}
				Element soap2xBody = (Element) document.selectSingleNode("/*/*[local-name()='Body']"); // достаем значение ноды soap:Body
				return  soap2xBody.asXML();
			}
	}
