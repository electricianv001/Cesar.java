package frame;

import main.Cesar;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MainFrame extends JFrame {
    private File rawFile;

    public MainFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 220);
        setResizable(false);
        setTitle("Cesar app");
        setLayout(null);
        setLocation(300, 200);
        setup();
        setVisible(true);
    }

    private void startCrypt(int key, String pathToSave) {
        try {
            String raw = Files.readString(Paths.get(rawFile.getAbsolutePath()));
            String crypted = Cesar.crypt(raw, key);
            Files.writeString(Paths.get(pathToSave), crypted);
            JOptionPane.showMessageDialog(null, "Готово!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void startDecrypt(int key, String pathToSave) {
        try {
            String raw = Files.readString(Paths.get(rawFile.getAbsolutePath()));
            String decrypted = Cesar.decrypt(raw, key);
            try (PrintWriter out = new PrintWriter(pathToSave)) {
                out.println(decrypted);
            }
            JOptionPane.showMessageDialog(null, "Готово!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void startBrutoforceDecrypt(String pathToSave) {
        try {
            String raw = Files.readString(Paths.get(rawFile.getAbsolutePath()));
            HashMap<Integer, String> decryptedTexts = new HashMap<>();

            for (int i = 1; i < Cesar.alphabet.length; i++) {
                String probablyDecrypted = Cesar.decrypt(raw, i);
                int res = Cesar.textAnalyze(probablyDecrypted);
                decryptedTexts.put(res, probablyDecrypted);
            }

            try (PrintWriter out = new PrintWriter(pathToSave)) {
                out.println(decryptedTexts.get(Collections.min(decryptedTexts.keySet())));
            }
            JOptionPane.showMessageDialog(null, "Готово!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void setup() {
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;

                return getExtension(f).equals("txt");
            }

            @Override
            public String getDescription() {
                return "Text files";
            }
        });
        String[] menuItems = {
                "Шифровка текста",
                "Расшифровка с помощью ключа",
                "Расшифровка с помощью bruto force",
        };
        JComboBox<String> mainMenu = new JComboBox<String>(menuItems);
        mainMenu.setLocation(10, 10);
        mainMenu.setSize(300, 30);
        mainMenu.setVisible(true);
        add(mainMenu);

        JLabel keyLabel = new JLabel("Ключ:");
        keyLabel.setLocation(10, 45);
        keyLabel.setSize(100, 30);
        add(keyLabel);

        JTextField keyField = new JTextField();
        keyField.setLocation(10, 75);
        keyField.setSize(100, 30);
        add(keyField);

        JLabel loadRawLabel = new JLabel("Исходный файл");
        loadRawLabel.setLocation(120, 45);
        loadRawLabel.setSize(100, 30);
        add(loadRawLabel);

        JButton loadRaw = new JButton("Загрузить");
        loadRaw.setLocation(120, 75);
        loadRaw.setSize(100, 30);
        loadRaw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(MainFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    rawFile = fc.getSelectedFile();
                }
            }
        });
        add(loadRaw);

        JLabel loadResultLabel = new JLabel("Адрес итогового файла:");
        loadResultLabel.setLocation(230, 45);
        loadResultLabel.setSize(150, 30);
        add(loadResultLabel);

        JTextField resultAddress = new JTextField();
        resultAddress.setText(System.getProperty("user.dir") + "\\result.txt");
        resultAddress.setLocation(230, 75);
        resultAddress.setSize(250, 30);
        add(resultAddress);

        JButton startButton = new JButton("Старт");
        startButton.setLocation(120, 120);
        startButton.setSize(150, 30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((keyField.getText().length() > 0 || mainMenu.getSelectedIndex() == 2) && rawFile != null && resultAddress.getText().length() > 0) {
                    try {
                        int key = 0;

                        if (mainMenu.getSelectedIndex() != 2) {
                            key = Integer.parseInt(keyField.getText());
                        }

                        if (Objects.equals(mainMenu.getSelectedItem(), menuItems[0])) {
                            startCrypt(key, resultAddress.getText());
                        } else if (Objects.equals(mainMenu.getSelectedItem(), menuItems[1])) {
                            startDecrypt(key, resultAddress.getText());
                        } else {
                            startBrutoforceDecrypt(resultAddress.getText());
                        }
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(null, "Ключ должен быть цифровой");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Заполните все поля!");
                }
            }
        });
        add(startButton);
    }

    public static String getExtension(File f) {
        String ext = "";
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
