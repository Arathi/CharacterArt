package org.beh.charart.gui;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.beh.charart.ImageLoader;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CharacterArtMain {

	private JFrame frame;
	private JTextField textInputPic;
	private ImageLoader loader;
	private JTextField textTileLength;
	private JTextField textOutput;
	private ButtonGroup btnGroup;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CharacterArtMain window = new CharacterArtMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CharacterArtMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 257);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textInputPic = new JTextField();
		textInputPic.setText("");
		textInputPic.setBounds(68, 10, 278, 21);
		frame.getContentPane().add(textInputPic);
		textInputPic.setColumns(10);

		JLabel label_inputImage = new JLabel("\u8F93\u5165\u56FE\u7247");
		label_inputImage.setBounds(10, 13, 54, 15);
		frame.getContentPane().add(label_inputImage);

		JButton btnOpen = new JButton("\u6253\u5F00");
		btnOpen.setBounds(356, 9, 68, 59);
		frame.getContentPane().add(btnOpen);
		
		JLabel label_tileLength = new JLabel("\u56FE\u5757\u8FB9\u957F");
		label_tileLength.setBounds(10, 43, 54, 15);
		frame.getContentPane().add(label_tileLength);
		
		textTileLength = new JTextField();
		textTileLength.setText("5");
		textTileLength.setBounds(68, 41, 76, 21);
		frame.getContentPane().add(textTileLength);
		textTileLength.setColumns(10);
		
		final JLabel lblImageInfo = new JLabel("\u56FE\u7247\u4FE1\u606F\uFF1A");
		lblImageInfo.setBounds(10, 72, 414, 28);
		frame.getContentPane().add(lblImageInfo);
		
		textOutput = new JTextField();
		textOutput.setText("\u56FE\u7247\u8F6C\u5F69\u8272\u6587\u5B57\u5DE5\u5177");
		textOutput.setBounds(68, 115, 356, 21);
		frame.getContentPane().add(textOutput);
		textOutput.setColumns(10);
		
		JLabel label_outputString = new JLabel("\u8F93\u51FA\u6587\u672C");
		label_outputString.setBounds(10, 118, 54, 15);
		frame.getContentPane().add(label_outputString);
		
		JButton btnPic = new JButton("\u8F93\u51FA\u56FE\u7247");
		btnPic.setBounds(331, 185, 93, 23);
		frame.getContentPane().add(btnPic);
		
		JButton btnHtml = new JButton("\u8F93\u51FAHTML");
		btnHtml.setBounds(331, 152, 93, 23);
		frame.getContentPane().add(btnHtml);
		
		final JSlider slider = new JSlider();
		slider.setValue(12);
		slider.setMaximum(32);
		slider.setMinimum(8);
		slider.setBounds(91, 149, 230, 26);
		frame.getContentPane().add(slider);
		
		final JLabel labelFontSize = new JLabel("\u8F93\u51FA\u5B57\u53F7("+slider.getValue()+")");
		labelFontSize.setBounds(10, 156, 82, 15);
		frame.getContentPane().add(labelFontSize);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				labelFontSize.setText("\u8F93\u51FA\u5B57\u53F7("+slider.getValue()+")");
			}
		});
		
		btnGroup = new ButtonGroup();
		
		JRadioButton rdbtnPng = new JRadioButton("png");
		rdbtnPng.setSelected(true);
		rdbtnPng.setBounds(6, 185, 54, 23);
		rdbtnPng.setActionCommand("png");
		frame.getContentPane().add(rdbtnPng);
		
		JRadioButton rdbtnJpg = new JRadioButton("jpg");
		rdbtnJpg.setBounds(62, 185, 54, 23);
		rdbtnJpg.setActionCommand("jpg");
		frame.getContentPane().add(rdbtnJpg);
		
		JRadioButton rdbtnBmp = new JRadioButton("bmp");
		rdbtnBmp.setBounds(118, 185, 54, 23);
		rdbtnBmp.setActionCommand("bmp");
		frame.getContentPane().add(rdbtnBmp);
		
		JRadioButton rdbtnGif = new JRadioButton("gif");
		rdbtnGif.setBounds(174, 185, 54, 23);
		rdbtnGif.setActionCommand("gif");
		frame.getContentPane().add(rdbtnGif);
		
		btnGroup.add(rdbtnPng);
		btnGroup.add(rdbtnJpg);
		btnGroup.add(rdbtnBmp);
		btnGroup.add(rdbtnGif);
		
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					// 把获取到的文件的绝对路径显示在文本编辑框中
					textInputPic.setText(fileChooser.getSelectedFile().getAbsolutePath());
					int length = Integer.parseInt(textTileLength.getText());
					loader = new ImageLoader(textInputPic.getText(), length);
					loader.setDir( fileChooser.getSelectedFile().getParent() );
					String imageInfo = "图片信息："+"Tile数量 "+loader.getSizeX()+"*"+loader.getSizeY()
							+" 边角Tile "+loader.getRemainderX()+"*"+loader.getRemainderY();
					lblImageInfo.setText(imageInfo);
				}
			}
		});
		btnHtml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loader.split();
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showSaveDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					// 把获取到的文件的绝对路径显示在文本编辑框中
					String optPath = fileChooser.getSelectedFile().getAbsolutePath();
					loader.saveHTML(optPath, textOutput.getText());
					JOptionPane.showMessageDialog(frame, "页面保存完成！");
				}
			}
		});
		btnPic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loader.split();
				ButtonModel formatBtn = btnGroup.getSelection();
				String format = formatBtn.getActionCommand();
				JFileChooser fileChooser = new JFileChooser(loader.getDir());
				
				int result = fileChooser.showSaveDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					// 把获取到的文件的绝对路径显示在文本编辑框中
					String optPath = fileChooser.getSelectedFile().getAbsolutePath();
					loader.savePicture(optPath, format, textOutput.getText(), slider.getValue());
					JOptionPane.showMessageDialog(frame, "图片保存完成！");
				}
			}
		});
	}
}
