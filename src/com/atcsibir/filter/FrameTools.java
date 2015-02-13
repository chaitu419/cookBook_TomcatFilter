package com.atcsibir.filter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class FrameTools
	{
		private static JButton folderChooser; // кнопка ФайлЧузера (Указываем путь до директории с соапами)
		private static JButton goSign; // мегабаттон (запускает сценарий подписи)
		public static JLabel soapsFolderLabel; // Лейбл с путем до директории с соапами
		public static JTextField mnemonicCode;
		public static JTextField mnemonicName;
		public static JTextField oktmo;
		public static JComboBox cryptoProJcpTokensList; // Селект со списком токенов Крипто-ПРО
		public static JPasswordField cryptoProJcpTokenPassword;

		public FrameTools()
			{
				JFrame rootFrame = new JFrame();
				rootFrame.setTitle("SignXML JavaApp");
				rootFrame.setSize(540, 270);
				rootFrame.setLocationRelativeTo(null); // делаем появление апплета по центру экрана
				rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				rootFrame.setResizable(false); // делаем размер окна фиксированным

				try {UIManager.setLookAndFeel(new NimbusLookAndFeel());} catch (UnsupportedLookAndFeelException e) {JOptionPane.showMessageDialog(null, e.getMessage());}

				/* Создаем основные элементы апплета */

				JPanel rootPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // создаем основную панель
				JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // панелька для лейблов (слева в фрейме)
				JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // панелька для контролов с параметрами (справа в фрейме)

				folderChooser = new JButton("Выбрать.."); // кнопка ФайлЧузера (Указываем путь до директории с соапами)
				goSign = new JButton("Сделать хорошо");

				soapsFolderLabel = new JLabel(); // сюда будет принтится путь до папки с соапами
				JLabel htmlLabels = new JLabel("<html><table><tr><td align='right' style='margin-bottom: 3px'><b>Папка с соапами: </b></td></tr><tr><td align='right' style='margin: 4px 0px'><b>Код мнемоники: </b></td></tr><tr><td align='right' style='margin: 3px 0px 4px 0px'><b>Наименование мнемоники: </b></td></tr><tr><td align='right' style='margin: 3px 0px'><b>ОКТМО: </b></td></tr><tr><td align='right' style='margin-top: 4px'><b>JCP ключ: </b></td></tr><tr><td align='right' style='margin-top: 5px'><b>JCP ключ (пароль): </b></td></tr></table></html>");

				mnemonicCode = new JTextField();
				mnemonicName = new JTextField();
				oktmo = new JTextField();
				cryptoProJcpTokenPassword = new JPasswordField();

				cryptoProJcpTokensList = new JComboBox(); // Селект со списком токенов Крипто-ПРО

				/* Собираем элементы в интерфейс */

				rootFrame.add(rootPanel);

				rootPanel.add(labelsPanel);
				rootPanel.add(controlsPanel);

				labelsPanel.add(htmlLabels);

				controlsPanel.add(folderChooser);
				controlsPanel.add(soapsFolderLabel);
				controlsPanel.add(mnemonicCode);
				controlsPanel.add(mnemonicName);
				controlsPanel.add(oktmo);
				controlsPanel.add(cryptoProJcpTokensList);
				controlsPanel.add(cryptoProJcpTokenPassword);
				controlsPanel.add(goSign);

				/* Придаем стили элементам */

				labelsPanel.setPreferredSize(new Dimension(200, 230));
				controlsPanel.setPreferredSize(new Dimension(320, 230));
				soapsFolderLabel.setPreferredSize(new Dimension(210, 24));
				mnemonicCode.setPreferredSize(new Dimension(300, 27));
				mnemonicName.setPreferredSize(new Dimension(300, 27));
				oktmo.setPreferredSize(new Dimension(300, 27));
				cryptoProJcpTokensList.setPreferredSize(new Dimension(300, 25));
				cryptoProJcpTokenPassword.setPreferredSize(new Dimension(190, 27));

				/* Блок дебага */

//				labelsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
//				controlsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));
//				soapsFolderLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.blue));

				initListeners();

				mnemonicCode.setText("00000541");
				mnemonicName.setText("MAIS");
				oktmo.setText("50701000");
				cryptoProJcpTokenPassword.setText("1");

				rootFrame.setVisible(true);
			}

		private void initListeners()
			{
				folderChooser.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
						{
							JFileChooser dirOpen = new JFileChooser();
							dirOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // опция позволяющая указывать только путь по директории

							int ret = dirOpen.showDialog(null, "Выбрать");
							if (ret == JFileChooser.APPROVE_OPTION)
								{
									File file = dirOpen.getSelectedFile();
									soapsFolderLabel.setText(file.getAbsolutePath());
								}
						}
				});

				goSign.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
						{
							if (FilesHandler.formValidate())
								{
									FilesHandler.startSigning(new File(FilesHandler.soapsFolderLabel).listFiles()); // погнали подписывать

									JOptionPane.showMessageDialog(null, "Done!");
								}
						}
				});
			}
	}
