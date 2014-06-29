package hr.fer.crypto;


import hr.fer.aes.Key.KeySize;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.awt.Color;

public class GUI {

	private JFrame frame;
	private JTextField aesKeyPath;
	private JTextField aesInPath;
	private JTextField aesOutPath;
	private JComboBox aesSize;
	private JTextField rsaPublicPath;
	private JTextField rsaPrivatePath;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton aesEncryptButton;
	private JRadioButton aesDecryptButton;
	private JTextField rsaSize;
	private JTextField shaInPath;
	private JTextField shaOutPath;
	private JTextField envInPath;
	private JTextField envPublicKeyPath;
	private JTextField envPath;
	private JTextField envOutPath;
	private JTextField envPrivateKeyPath;
	private JTextField sigInPath;
	private JTextField sigPrivKeyPath;
	private JTextField sigPath;
	private JTextField sigPubKeyPath;
	private JTextField pecatSigPath;
	private JTextField pecatEnvPath;
	private JTextField pecatOutPath;
	private JTextField pecatPrivateKeyPath;
	private JTextField pecatPublicKeyPath;
	private JTextField pecatPath;
	private JComboBox encryptionMethod;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() {
		initialize();
	}
	

	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 657, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		tabbedPane.setBounds(5, 20, 645, 365);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 175, 175));
		tabbedPane.addTab("AES", null, panel, null);
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("Odaberi");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aesKeyPath.setText(chooseFile(aesKeyPath.getText()));
			}
		});
		btnNewButton.setBounds(197, 37, 93, 29);
		panel.add(btnNewButton);
		
		aesKeyPath = new JTextField();
		aesKeyPath.setText("aes_kljuc.txt");
		aesKeyPath.setBounds(66, 36, 119, 28);
		panel.add(aesKeyPath);
		aesKeyPath.setColumns(10);
		
		JLabel lblKlju = new JLabel("Klju\u010D:");
		lblKlju.setBounds(19, 42, 46, 16);
		panel.add(lblKlju);
		
		JButton btnPregledaj = new JButton("Pregledaj");
		btnPregledaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + aesKeyPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnPregledaj.setBounds(302, 37, 93, 29);
		panel.add(btnPregledaj);
		
		JButton btnGeneriraj = new JButton("Generiraj");
		btnGeneriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeySize size = KeySize.AES_128;
				
				if (aesSize.getSelectedItem().equals("192 b")){
					size = KeySize.AES_192;
				}
				if (aesSize.getSelectedItem().equals("256 b")){
					size = KeySize.AES_256;
				}
				Helper.generateAESKey(aesKeyPath.getText(), size);
			}
		});
		btnGeneriraj.setBounds(523, 37, 93, 29);
		panel.add(btnGeneriraj);
		
		JLabel lblUlaznaDatoteka = new JLabel("Ulazna datoteka:");
		lblUlaznaDatoteka.setBounds(19, 78, 119, 16);
		panel.add(lblUlaznaDatoteka);
		
		aesInPath = new JTextField();
		aesInPath.setText("ulaz.txt");
		aesInPath.setColumns(10);
		aesInPath.setBounds(136, 72, 259, 28);
		panel.add(aesInPath);
		
		JButton button = new JButton("Odaberi");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aesInPath.setText(chooseFile(aesInPath.getText()));
			}
		});
		button.setBounds(418, 73, 93, 29);
		panel.add(button);
		
		JButton button_1 = new JButton("Pregledaj");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + aesInPath.getText() );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.setBounds(523, 73, 93, 29);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Pregledaj");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + aesOutPath.getText() );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_2.setBounds(523, 107, 93, 29);
		panel.add(button_2);
		
		JLabel lblIzlaznaDatoteka = new JLabel("Izlazna datoteka:");
		lblIzlaznaDatoteka.setBounds(19, 112, 119, 16);
		panel.add(lblIzlaznaDatoteka);
		
		aesOutPath = new JTextField();
		aesOutPath.setText("aes_izlaz.txt");
		aesOutPath.setColumns(10);
		aesOutPath.setBounds(136, 106, 259, 28);
		panel.add(aesOutPath);
		
		JButton button_3 = new JButton("Odaberi");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aesOutPath.setText(chooseFile(aesOutPath.getText()));
			}
		});
		button_3.setBounds(418, 107, 93, 29);
		panel.add(button_3);
		
		aesEncryptButton = new JRadioButton("Kriptiranje");
		aesEncryptButton.setSelected(true);
		buttonGroup.add(aesEncryptButton);
		aesEncryptButton.setBounds(136, 183, 105, 23);
		panel.add(aesEncryptButton);
		
		aesDecryptButton = new JRadioButton("Dekriptiranje");
		buttonGroup.add(aesDecryptButton);
		aesDecryptButton.setBounds(276, 183, 119, 23);
		panel.add(aesDecryptButton);
		
		JButton aes_execute = new JButton("Obavi");
		aes_execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (aesEncryptButton.isSelected()) {
					Helper.AESEncrypt(aesInPath.getText(), aesOutPath.getText(), aesKeyPath.getText(), encryptionMethod.getSelectedItem().toString());
				} else {
					Helper.AESDecrypt(aesInPath.getText(), aesOutPath.getText(), aesKeyPath.getText(), encryptionMethod.getSelectedItem().toString());
				}
			}
		});
		aes_execute.setBounds(499, 247, 117, 29);
		panel.add(aes_execute);
		
		aesSize = new JComboBox();
		aesSize.setBounds(418, 38, 93, 27);
		aesSize.addItem("128 b");
		aesSize.addItem("192 b");
		aesSize.addItem("256 b");
		panel.add(aesSize);
		
		JButton btnNewButton_1 = new JButton("Zamijeni");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tmp = aesInPath.getText();
				aesInPath.setText(aesOutPath.getText());
				aesOutPath.setText(tmp);
				
				if (aesEncryptButton.isSelected())
					aesDecryptButton.setSelected(true);
				else
					aesEncryptButton.setSelected(true);
			}
		});
		btnNewButton_1.setBounds(278, 142, 117, 29);
		panel.add(btnNewButton_1);
		
		encryptionMethod = new JComboBox();
		encryptionMethod.setModel(new DefaultComboBoxModel(new String[] {"ECB", "OFB", "AESJava"}));
		encryptionMethod.setBounds(394, 248, 93, 27);
		panel.add(encryptionMethod);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.PINK);
		panel_3.setLayout(null);
		tabbedPane.addTab("RSA", null, panel_3, null);
		
		JButton button_6 = new JButton("Generiraj");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helper.RSAKeyGenerate(rsaPublicPath.getText(), rsaPrivatePath.getText(), Integer.parseInt(rsaSize.getText()));
			}
		});
		button_6.setBounds(423, 100, 93, 29);
		panel_3.add(button_6);
		
		JLabel lblJavni = new JLabel("Javni:");
		lblJavni.setBounds(20, 30, 105, 16);
		panel_3.add(lblJavni);
		
		rsaPublicPath = new JTextField();
		rsaPublicPath.setText("rsa_a_javni.txt");
		rsaPublicPath.setColumns(10);
		rsaPublicPath.setBounds(137, 24, 169, 28);
		panel_3.add(rsaPublicPath);
		
		JButton button_7 = new JButton("Odaberi");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rsaPublicPath.setText(chooseFile(rsaPublicPath.getText()));
			}
		});
		button_7.setBounds(318, 25, 93, 29);
		panel_3.add(button_7);
		
		JButton button_8 = new JButton("Pregledaj");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + rsaPublicPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_8.setBounds(423, 25, 93, 29);
		panel_3.add(button_8);
		
		JButton button_9 = new JButton("Pregledaj");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + rsaPrivatePath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_9.setBounds(423, 59, 93, 29);
		panel_3.add(button_9);
		
		JLabel lblPrivatni = new JLabel("Tajni:");
		lblPrivatni.setBounds(20, 64, 105, 16);
		panel_3.add(lblPrivatni);
		
		rsaPrivatePath = new JTextField();
		rsaPrivatePath.setText("rsa_a_tajni.txt");
		rsaPrivatePath.setColumns(10);
		rsaPrivatePath.setBounds(137, 58, 169, 28);
		panel_3.add(rsaPrivatePath);
		
		JButton button_10 = new JButton("Odaberi");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rsaPrivatePath.setText(chooseFile(rsaPrivatePath.getText()));
			}
		});
		button_10.setBounds(318, 59, 93, 29);
		panel_3.add(button_10);
		
		JLabel lblVeliina = new JLabel("Veli\u010Dina:");
		lblVeliina.setBounds(20, 107, 105, 16);
		panel_3.add(lblVeliina);
		
		rsaSize = new JTextField();
		rsaSize.setText("512");
		rsaSize.setColumns(10);
		rsaSize.setBounds(137, 101, 169, 28);
		panel_3.add(rsaSize);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.PINK);
		tabbedPane.addTab("SHA-1", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("Ulazna datoteka:");
		label.setBounds(6, 30, 119, 16);
		panel_1.add(label);
		
		JLabel label_1 = new JLabel("Izlazna datoteka:");
		label_1.setBounds(6, 64, 119, 16);
		panel_1.add(label_1);
		
		shaInPath = new JTextField();
		shaInPath.setText("sha_ulaz.txt");
		shaInPath.setColumns(10);
		shaInPath.setBounds(123, 24, 259, 28);
		panel_1.add(shaInPath);
		
		shaOutPath = new JTextField();
		shaOutPath.setText("sha_izlaz.txt");
		shaOutPath.setColumns(10);
		shaOutPath.setBounds(123, 58, 259, 28);
		panel_1.add(shaOutPath);
		
		JButton button_4 = new JButton("Odaberi");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shaInPath.setText(chooseFile(shaInPath.getText()));
			}
		});
		button_4.setBounds(405, 25, 93, 29);
		panel_1.add(button_4);
		
		JButton button_5 = new JButton("Odaberi");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shaOutPath.setText(chooseFile(shaOutPath.getText()));
			}
		});
		button_5.setBounds(405, 59, 93, 29);
		panel_1.add(button_5);
		
		JButton button_11 = new JButton("Pregledaj");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + shaInPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_11.setBounds(510, 25, 93, 29);
		panel_1.add(button_11);
		
		JButton button_12 = new JButton("Pregledaj");
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + shaOutPath.getText() );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_12.setBounds(510, 59, 93, 29);
		panel_1.add(button_12);
		
		JButton btnSami = new JButton("Sa\u017Emi");
		btnSami.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String hash = Helper.SHADigest(shaInPath.getText(), shaOutPath.getText());
				JOptionPane.showMessageDialog(null, "Hash: " + hash);
				
			}
		});
		btnSami.setBounds(510, 100, 93, 29);
		panel_1.add(btnSami);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.PINK);
		tabbedPane.addTab("Omotnica", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel label_2 = new JLabel("Ulazna datoteka:");
		label_2.setBounds(19, 23, 162, 16);
		panel_2.add(label_2);
		
		envInPath = new JTextField();
		envInPath.setText("ulaz.txt");
		envInPath.setColumns(10);
		envInPath.setBounds(178, 17, 259, 28);
		panel_2.add(envInPath);
		
		JButton button_13 = new JButton("Odaberi");
		button_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				envInPath.setText(chooseFile(envInPath.getText()));
			}
		});
	
		button_13.setBounds(435, 17, 93, 29);
		panel_2.add(button_13);
		
		JButton button_14 = new JButton("Pregledaj");
		button_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + envInPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_14.setBounds(523, 18, 93, 29);
		panel_2.add(button_14);
		
		JLabel lblJavniKljuPrimatelja = new JLabel("Javni klju\u010D primatelja:");
		lblJavniKljuPrimatelja.setBounds(19, 57, 162, 16);
		panel_2.add(lblJavniKljuPrimatelja);
		
		envPublicKeyPath = new JTextField();
		envPublicKeyPath.setText("rsa_b_javni.txt");
		envPublicKeyPath.setColumns(10);
		envPublicKeyPath.setBounds(178, 51, 259, 28);
		panel_2.add(envPublicKeyPath);
		
		JButton button_15 = new JButton("Odaberi");
		button_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				envPublicKeyPath.setText(chooseFile(envPublicKeyPath.getText()));
			}
		});
	
		button_15.setBounds(435, 51, 93, 29);
		panel_2.add(button_15);
		
		JButton button_16 = new JButton("Pregledaj");
		button_16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + envPublicKeyPath.getText() );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_16.setBounds(523, 52, 93, 29);
		panel_2.add(button_16);
		
		JLabel lblDigitalnaOmotnica = new JLabel("Digitalna omotnica:");
		lblDigitalnaOmotnica.setBounds(19, 91, 162, 16);
		panel_2.add(lblDigitalnaOmotnica);
		
		envPath = new JTextField();
		envPath.setText("omotnica.txt");
		envPath.setColumns(10);
		envPath.setBounds(178, 85, 259, 28);
		panel_2.add(envPath);
		
		JButton button_17 = new JButton("Odaberi");
		button_17.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				envPath.setText(chooseFile(envPath.getText()));
			}
		});
		button_17.setBounds(435, 85, 93, 29);
		panel_2.add(button_17);
		
//		button_17.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				rsaPublicPath.setText(chooseFile(rsaPublicPath.getText()));
//			}
		
		JButton button_18 = new JButton("Pregledaj");
		button_18.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + envPath.getText() );
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_18.setBounds(523, 86, 93, 29);
		panel_2.add(button_18);
		
		JButton btnNewButton_2 = new JButton("Generiraj digitalnu omotnicu");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helper.generateDigitalEnvelope(envInPath.getText(), aesKeyPath.getText(), envPublicKeyPath.getText(), envPath.getText(), encryptionMethod.getSelectedItem().toString());
			}
		});
		btnNewButton_2.setBounds(178, 119, 259, 29);
		panel_2.add(btnNewButton_2);
		
		JButton btnOtvoriDigitalnuOmotnicu = new JButton("Otvori digitalnu omotnicu");
		btnOtvoriDigitalnuOmotnicu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helper.openDigitalEnvelope(envPath.getText(), envPrivateKeyPath.getText(), envOutPath.getText());
			}
		});
		btnOtvoriDigitalnuOmotnicu.setBounds(178, 240, 259, 29);
		panel_2.add(btnOtvoriDigitalnuOmotnicu);
		
		JLabel lblIzlaznaDatoteka_1 = new JLabel("Izlazna datoteka:");
		lblIzlaznaDatoteka_1.setBounds(19, 212, 162, 16);
		panel_2.add(lblIzlaznaDatoteka_1);
		
		envOutPath = new JTextField();
		envOutPath.setText("izlaz.txt");
		envOutPath.setColumns(10);
		envOutPath.setBounds(178, 206, 259, 28);
		panel_2.add(envOutPath);
		
		JButton button_20 = new JButton("Odaberi");
		button_20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				envOutPath.setText(chooseFile(envOutPath.getText()));
			}
		});
		button_20.setBounds(435, 206, 93, 29);
		panel_2.add(button_20);
		
		JButton button_21 = new JButton("Pregledaj");
		button_21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + envOutPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_21.setBounds(523, 207, 93, 29);
		panel_2.add(button_21);
		
		JButton button_22 = new JButton("Pregledaj");
		button_22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + envPrivateKeyPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_22.setBounds(523, 173, 93, 29);
		panel_2.add(button_22);
		
		JButton button_23 = new JButton("Odaberi");
		button_23.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				envPrivateKeyPath.setText(chooseFile(envPrivateKeyPath.getText()));
			}
		});
		button_23.setBounds(435, 172, 93, 29);
		panel_2.add(button_23);
		
		envPrivateKeyPath = new JTextField();
		envPrivateKeyPath.setText("rsa_b_tajni.txt");
		envPrivateKeyPath.setColumns(10);
		envPrivateKeyPath.setBounds(178, 172, 259, 28);
		panel_2.add(envPrivateKeyPath);
		
		JLabel lblTajniKljuPrimatelja = new JLabel("Tajni klju\u010D primatelja:");
		lblTajniKljuPrimatelja.setBounds(19, 178, 162, 16);
		panel_2.add(lblTajniKljuPrimatelja);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.PINK);
		tabbedPane.addTab("Potpis", null, panel_4, null);
		panel_4.setLayout(null);
		
		JLabel label_3 = new JLabel("Ulazna datoteka:");
		label_3.setBounds(15, 19, 162, 16);
		panel_4.add(label_3);
		
		sigInPath = new JTextField();
		sigInPath.setText("ulaz.txt");
		sigInPath.setColumns(10);
		sigInPath.setBounds(174, 13, 259, 28);
		panel_4.add(sigInPath);
		
		JButton button_19 = new JButton("Odaberi");
		button_19.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigInPath.setText(chooseFile(sigInPath.getText()));
			}
		});
		button_19.setBounds(431, 13, 93, 29);
		panel_4.add(button_19);
		
		JButton button_24 = new JButton("Pregledaj");
		button_24.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + sigInPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_24.setBounds(519, 14, 93, 29);
		panel_4.add(button_24);
		
		JLabel lblTajniKljuPoiljatelja = new JLabel("Tajni klju\u010D po\u0161iljatelja:");
		lblTajniKljuPoiljatelja.setBounds(15, 53, 162, 16);
		panel_4.add(lblTajniKljuPoiljatelja);
		
		sigPrivKeyPath = new JTextField();
		sigPrivKeyPath.setText("rsa_a_tajni.txt");
		sigPrivKeyPath.setColumns(10);
		sigPrivKeyPath.setBounds(174, 47, 259, 28);
		panel_4.add(sigPrivKeyPath);
		
		JButton button_25 = new JButton("Odaberi");
		button_25.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigPrivKeyPath.setText(chooseFile(sigPrivKeyPath.getText()));
			}
		});
		button_25.setBounds(431, 47, 93, 29);
		panel_4.add(button_25);
		
		JButton button_26 = new JButton("Pregledaj");
		button_26.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + sigPrivKeyPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_26.setBounds(519, 48, 93, 29);
		panel_4.add(button_26);
		
		JLabel lblDigitalniPotpis = new JLabel("Digitalni potpis:");
		lblDigitalniPotpis.setBounds(15, 87, 162, 16);
		panel_4.add(lblDigitalniPotpis);
		
		sigPath = new JTextField();
		sigPath.setText("potpis.txt");
		sigPath.setColumns(10);
		sigPath.setBounds(174, 81, 259, 28);
		panel_4.add(sigPath);
		
		JButton button_27 = new JButton("Odaberi");
		button_27.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigPath.setText(chooseFile(sigPath.getText()));
			}
		});
		button_27.setBounds(431, 81, 93, 29);
		panel_4.add(button_27);
		
		JButton button_28 = new JButton("Pregledaj");
		button_28.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + sigPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_28.setBounds(519, 82, 93, 29);
		panel_4.add(button_28);
		
		JButton btnGenerirajDigitalniPotpis = new JButton("Generiraj digitalni potpis");
		btnGenerirajDigitalniPotpis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helper.generateDigitalSignature(sigInPath.getText(), sigPrivKeyPath.getText(), sigPath.getText());
			}
		});
		btnGenerirajDigitalniPotpis.setBounds(174, 115, 259, 29);
		panel_4.add(btnGenerirajDigitalniPotpis);
		
		JButton btnProvjeriDigitalniPotpis = new JButton("Provjeri digitalni potpis");
		btnProvjeriDigitalniPotpis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean r = Helper.validateDigitalSignature(sigInPath.getText(), sigPubKeyPath.getText(), sigPath.getText());
				
				if(r) {
					JOptionPane.showMessageDialog(null, "Potpis je OK!");
				} else {
					JOptionPane.showMessageDialog(null, "Potpis nije valjan!");
				}
			}
		});
		btnProvjeriDigitalniPotpis.setBounds(174, 206, 259, 29);
		panel_4.add(btnProvjeriDigitalniPotpis);
		
		JButton button_33 = new JButton("Pregledaj");
		button_33.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + sigPubKeyPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_33.setBounds(519, 169, 93, 29);
		panel_4.add(button_33);
		
		JButton button_34 = new JButton("Odaberi");
		button_34.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sigPubKeyPath.setText(chooseFile(sigPubKeyPath.getText()));
			}
		});
		button_34.setBounds(431, 168, 93, 29);
		panel_4.add(button_34);
		
		sigPubKeyPath = new JTextField();
		sigPubKeyPath.setText("rsa_a_javni.txt");
		sigPubKeyPath.setColumns(10);
		sigPubKeyPath.setBounds(174, 168, 259, 28);
		panel_4.add(sigPubKeyPath);
		
		JLabel lblJavniKljuPoiljatelja = new JLabel("Javni klju\u010D po\u0161iljatelja:");
		lblJavniKljuPoiljatelja.setBounds(15, 174, 162, 16);
		panel_4.add(lblJavniKljuPoiljatelja);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.PINK);
		tabbedPane.addTab("Pe\u010Dat", null, panel_5, null);
		panel_5.setLayout(null);
		
		JButton btnGenerirajDigitalniPeat = new JButton("Generiraj digitalni pe\u010Dat");
		btnGenerirajDigitalniPeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helper.generateDigitalStamp(pecatEnvPath.getText(), pecatSigPath.getText(), pecatPath.getText());
			}
		});
		btnGenerirajDigitalniPeat.setBounds(178, 138, 259, 29);
		panel_5.add(btnGenerirajDigitalniPeat);
		
		JLabel lblDigitalniPotpis_1 = new JLabel("Digitalni potpis:");
		lblDigitalniPotpis_1.setBounds(19, 74, 146, 16);
		panel_5.add(lblDigitalniPotpis_1);
		
		pecatSigPath = new JTextField();
		pecatSigPath.setText("potpis.txt");
		pecatSigPath.setColumns(10);
		pecatSigPath.setBounds(178, 68, 259, 28);
		panel_5.add(pecatSigPath);
		
		JButton button_39 = new JButton("Odaberi");
		button_39.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pecatSigPath.setText(chooseFile(pecatSigPath.getText()));
			}
		});
		button_39.setBounds(435, 68, 83, 28);
		panel_5.add(button_39);
		
		JButton button_40 = new JButton("Pregledaj");
		button_40.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + pecatSigPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_40.setBounds(523, 69, 93, 29);
		panel_5.add(button_40);
		
		JButton button_41 = new JButton("Pregledaj");
		button_41.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + pecatEnvPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_41.setBounds(523, 32, 93, 29);
		panel_5.add(button_41);
		
		JButton button_42 = new JButton("Odaberi");
		button_42.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pecatEnvPath.setText(chooseFile(pecatEnvPath.getText()));
			}
		});
		button_42.setBounds(435, 32, 83, 29);
		panel_5.add(button_42);
		
		pecatEnvPath = new JTextField();
		pecatEnvPath.setText("omotnica.txt");
		pecatEnvPath.setColumns(10);
		pecatEnvPath.setBounds(178, 32, 259, 28);
		panel_5.add(pecatEnvPath);
		
		JLabel lblDigitalnaOmotnica_1 = new JLabel("Digitalna omotnica:");
		lblDigitalnaOmotnica_1.setBounds(19, 38, 152, 16);
		panel_5.add(lblDigitalnaOmotnica_1);
		
		JLabel lblJavniKljuPoiljatelja_1 = new JLabel("Javni klju\u010D po\u0161iljatelja:");
		lblJavniKljuPoiljatelja_1.setBounds(19, 189, 137, 16);
		panel_5.add(lblJavniKljuPoiljatelja_1);
		
		JButton btnOtvoriDigitalniPecat = new JButton("Otvori digitalni pe\u010Dat");
		btnOtvoriDigitalniPecat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean r = Helper.openDigitalStamp(pecatPath.getText(), pecatPublicKeyPath.getText(), pecatPrivateKeyPath.getText(), pecatOutPath.getText());
				
				if(r) {
					JOptionPane.showMessageDialog(null, "Potpis pecata je OK!");
				} else {
					JOptionPane.showMessageDialog(null, "Potpis pecata nije valjan!");
				}
			}
		});
		
		btnOtvoriDigitalniPecat.setBounds(178, 262, 259, 29);
		panel_5.add(btnOtvoriDigitalniPecat);
		
		JLabel label_10 = new JLabel("Izlazna datoteka:");
		label_10.setBounds(19, 234, 128, 16);
		panel_5.add(label_10);
		
		pecatOutPath = new JTextField();
		pecatOutPath.setText("izlaz.txt");
		pecatOutPath.setColumns(10);
		pecatOutPath.setBounds(178, 228, 259, 28);
		panel_5.add(pecatOutPath);
		
		JButton button_43 = new JButton("Odaberi");
		button_43.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pecatOutPath.setText(chooseFile(pecatOutPath.getText()));
			}
		});
		button_43.setBounds(435, 228, 83, 29);
		panel_5.add(button_43);
		
		JButton button_44 = new JButton("Pregledaj");
		button_44.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + pecatOutPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_44.setBounds(523, 234, 93, 24);
		panel_5.add(button_44);
		
		JButton button_45 = new JButton("Pregledaj");
		button_45.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + pecatPrivateKeyPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_45.setBounds(523, 206, 93, 29);
		panel_5.add(button_45);
		
		JButton button_46 = new JButton("Odaberi");
		button_46.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pecatPrivateKeyPath.setText(chooseFile(pecatPrivateKeyPath.getText()));
			}
		});
		button_46.setBounds(435, 206, 83, 29);
		panel_5.add(button_46);
		
		pecatPrivateKeyPath = new JTextField();
		pecatPrivateKeyPath.setText("rsa_b_tajni.txt");
		pecatPrivateKeyPath.setColumns(10);
		pecatPrivateKeyPath.setBounds(178, 205, 259, 28);
		panel_5.add(pecatPrivateKeyPath);
		
		JLabel label_11 = new JLabel("Tajni klju\u010D primatelja:");
		label_11.setBounds(19, 211, 128, 16);
		panel_5.add(label_11);
		
		JButton button_47 = new JButton("Odaberi");
		button_47.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pecatPublicKeyPath.setText(chooseFile(pecatPublicKeyPath.getText()));
			}
		});
		button_47.setBounds(435, 183, 83, 29);
		panel_5.add(button_47);
		
		JButton button_48 = new JButton("Pregledaj");
		button_48.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + pecatPublicKeyPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_48.setBounds(523, 185, 93, 24);
		panel_5.add(button_48);
		
		pecatPublicKeyPath = new JTextField();
		pecatPublicKeyPath.setText("rsa_a_javni.txt");
		pecatPublicKeyPath.setColumns(10);
		pecatPublicKeyPath.setBounds(178, 183, 259, 28);
		panel_5.add(pecatPublicKeyPath);
		
		JLabel lblDigitalniPeatizlaz = new JLabel("Digitalni pecat (izlaz):");
		lblDigitalniPeatizlaz.setBounds(19, 110, 137, 16);
		panel_5.add(lblDigitalniPeatizlaz);
		
		pecatPath = new JTextField();
		pecatPath.setText("pecat.txt");
		pecatPath.setColumns(10);
		pecatPath.setBounds(178, 104, 259, 28);
		panel_5.add(pecatPath);
		
		JButton button_29 = new JButton("Odaberi");
		button_29.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pecatPath.setText(chooseFile(pecatPath.getText()));
			}
		});
		button_29.setBounds(435, 104, 83, 29);
		panel_5.add(button_29);
		
		JButton button_30 = new JButton("Pregledaj");
		button_30.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec("C:\\Windows\\notepad.exe " + pecatPath.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_30.setBounds(523, 105, 93, 29);
		panel_5.add(button_30);
		
		JLabel lblPrvoGeneriratiOmotnicu = new JLabel("--Potrebno je prvo generirati omotnicu i potpis u prethodnim koracima--");
		lblPrvoGeneriratiOmotnicu.setBounds(19, 6, 597, 16);
		panel_5.add(lblPrvoGeneriratiOmotnicu);
	}
	
	private String chooseFile(String file) {
		JFileChooser chooser = new JFileChooser(new File(""));
		chooser.setApproveButtonText("Open");		
		chooser.showSaveDialog(this.frame);
		if(chooser.getSelectedFile() != null){
			return chooser.getSelectedFile().getAbsolutePath();
		}
		else {
			return file;
		}
	}
	protected JRadioButton getAesEncryptButton() {
		return aesEncryptButton;
	}
	protected JRadioButton getAesDecryptButton() {
		return aesDecryptButton;
	}
	protected JComboBox getEncryptionMethod() {
		return encryptionMethod;
	}
	

}
