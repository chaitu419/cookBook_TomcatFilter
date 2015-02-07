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
		private static JLabel soapsFolderLabel; // Лейбл с путем до директории с соапами

		public FrameTools()
			{
				JFrame rootFrame = new JFrame();
				rootFrame.setTitle("SignXML JavaApp");
				rootFrame.setSize(610, 360);
				rootFrame.setLocationRelativeTo(null); // делаем появление апплета по центру экрана
				rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				rootFrame.setResizable(false); // делаем размер окна фиксированным

				try {UIManager.setLookAndFeel(new NimbusLookAndFeel());} catch (UnsupportedLookAndFeelException e) {}

				/* Создаем основные элементы апплета */

				JPanel rootPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // создаем основную панель
				JPanel labelsPanel = new JPanel(); // панелька для лейблов (слева в фрейме)
				JPanel controlsPanel = new JPanel(); // панелька для контролов с параметрами (справа в фрейме)
				folderChooser = new JButton("Выбрать.."); // кнопка ФайлЧузера (Указываем путь до директории с соапами)
				soapsFolderLabel = new JLabel(); // сюда будет принтится путь до папки с соапами

				/* Собираем элементы в интерфейс */

				rootFrame.add(rootPanel);

				rootPanel.add(labelsPanel);
				rootPanel.add(controlsPanel);

				controlsPanel.add(folderChooser);
				controlsPanel.add(soapsFolderLabel);


				/* Блок дебага */

				labelsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
				controlsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.green));




				// initListeners(); // TODO какого-то хуя не инициализируется окно с чузером (сыпится рендер апплета)

				rootFrame.setVisible(true);
			}

		private void initListeners()
			{
				folderChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileopen = new JFileChooser();
						fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // опция позволяющая указывать только путь по директории

						int ret = fileopen.showDialog(null, "Выбрать");
						if (ret == JFileChooser.APPROVE_OPTION) {
							File file = fileopen.getSelectedFile();
							soapsFolderLabel.setText(file.getAbsolutePath());
						}
					}
				});
			}
	}
