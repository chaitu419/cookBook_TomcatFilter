package com.atcsibir.filter;

import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;

public class MassXmlSign
	{
		public static Canonicalizer canon = null;
		public static void main(String[] args)
			{
				// org.apache.xml.security.Init.init(); // Инициализируем apacheXmlSec. Нужен для методов канонизации
				// try {canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS); /* Выбираем метод канонизации. В текущем варианте "exclusive c14n without comments" */} catch (InvalidCanonicalizerException e) {e.printStackTrace();}
				new FrameTools(); // создаем фрейм
				// KeyStoreTools.InitKeyStores(); // загружаем кейсторы

			}
	}
