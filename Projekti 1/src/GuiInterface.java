import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GuiInterface {
    
    int width;
    int height;
    FreqAnalyzer alg;
    JFrame frame;
    boolean isEncrypt = true;
    Pair<String, LinkedHashMap<Character, Character>> encryptResult;
    ArrayList<Pair<String, LinkedHashMap<Character, Pair<Character, Double>>>> decryptResult;
    int cycleIndex;
    JButton newFrameButton;
    JFrame tableFrame;

    public GuiInterface(int width, int height, FreqAnalyzer alg){
        this.width = width;
        this.height = height;
        this.alg = alg;
        frame = new JFrame("Encription by Substitution");
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        populateFrame();
    }

    private void populateFrame(){
        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap (true);
        textArea1.setWrapStyleWord (true);
        textArea1.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollableTextArea1 = new JScrollPane(textArea1);
        GridBagConstraints ta1 = new GridBagConstraints();
        ta1.fill = GridBagConstraints.BOTH;
        ta1.gridx = 0;
        ta1.gridy = 1;
        ta1.weightx = 1;
        ta1.weighty = 1;
        ta1.insets = new Insets (20, 50, 20, 50);
        frame.add(scrollableTextArea1, ta1);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Encrypt", "Decrypt"});
        comboBox.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints cb = new GridBagConstraints();
        cb.gridx = 1;
        cb.gridy = 0;
        cb.ipadx = 25;
        cb.ipady = 15;
        cb.anchor = GridBagConstraints.CENTER;
        cb.insets = new Insets (20, 0, 20, 0);
        frame.add(comboBox,cb);


        JTextArea textArea2 = new JTextArea();
        textArea2.setLineWrap (true);
        textArea2.setWrapStyleWord (true);
        textArea2.setEditable(false);
        textArea2.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollableTextArea2 = new JScrollPane(textArea2);
        GridBagConstraints ta2 = new GridBagConstraints();
        ta2.fill = GridBagConstraints.BOTH;
        ta2.gridx = 2;
        ta2.gridy = 1;
        ta2.weightx = 1;
        ta2.weighty = 1;
        ta2.insets = new Insets (20, 50, 20, 50);
        frame.add(scrollableTextArea2, ta2);

        JButton submitButton = new JButton("Encrypt");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints b1 = new GridBagConstraints();
		b1.insets = new Insets(10, 0, 0, 0);
		b1.gridx = 1; 
		b1.gridy = 1;
        b1.ipadx = 25;
        b1.ipady = 15;
        b1.anchor = GridBagConstraints.CENTER;
        frame.add(submitButton, b1);

        JButton cycleButton = new JButton("Cycle");
        cycleButton.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints b2 = new GridBagConstraints();
		b2.insets = new Insets(10, 0, 10, 0);
		b2.gridx = 1; 
		b2.gridy = 2;
        b2.ipadx = 25;
        b2.ipady = 15;
        b2.anchor = GridBagConstraints.CENTER;
        cycleButton.setVisible(false);
        frame.add(cycleButton, b2);

        newFrameButton = new JButton("View Dictionary");
        newFrameButton.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints b3 = new GridBagConstraints();
		b3.anchor = GridBagConstraints.SOUTHEAST;
		b3.insets = new Insets(10, 0, 0, 0); // top padding
		b3.gridx = 2; 
		b3.gridy = 3;
        b3.ipadx = 10;
        b3.ipady = 10;
        newFrameButton.setVisible(false);
        frame.add(newFrameButton, b3);


        submitButton.addActionListener(e -> {
            if(textArea1.getText().equals("")){
                textArea1.setText("Type something...");
            }
            else if(isEncrypt){
                newFrameButton.setVisible(true);
                String text = textArea1.getText().replace("\n", "");
                encryptResult = alg.encrypt(text);
                textArea2.setText(encryptResult.first);
            }
            else {
                newFrameButton.setVisible(true);
                String text = textArea1.getText().replace("\n", "");
                LinkedHashMap<String, LinkedHashMap<Character, Pair<Character, Double>>> decryptResultMap = alg.decrypt(text);
                decryptResult = new ArrayList<>();
                for (Map.Entry<String,LinkedHashMap<Character,Pair<Character,Double>>> pair : decryptResultMap.entrySet()) {
                    decryptResult.add(new Pair<String,LinkedHashMap<Character,Pair<Character,Double>>>(pair.getKey(), pair.getValue()));
                }
                cycleIndex = 0;
                textArea2.setText(decryptResult.get(cycleIndex).first);
            }
        });

        cycleButton.addActionListener(e -> {
            if(decryptResult != null){
                cycleIndex++;
                if(cycleIndex >= decryptResult.size())
                    cycleIndex = 0;
                textArea2.setText(decryptResult.get(cycleIndex).first);
            }
        });
        comboBox.addActionListener(e -> {
            if(comboBox.getSelectedItem().equals("Encrypt")){
                isEncrypt = true;
                cycleButton.setVisible(false);
                submitButton.setText("Encrypt");
                textArea1.setText("");
                textArea2.setText("");
                if(decryptResult != null)
                    decryptResult.clear();
            }
            else{
                isEncrypt = false;
                cycleButton.setVisible(true);
                submitButton.setText("Decrypt");
                textArea1.setText("");
                textArea2.setText("");
                if(decryptResult != null)
                    decryptResult.clear();
            }
        });

        newFrameButton.addActionListener(e -> {
            createTableFrame();
        });
    }

    private void createTableFrame(){
        tableFrame = new JFrame("Table");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if(isEncrypt){
            String[] columnNames = {"Original Letter", "Encrypted Letter"};
            Character[][] data = new Character[encryptResult.second.size()][2];
            int i = 0;
            for(Map.Entry<Character, Character> row : encryptResult.second.entrySet()){
                data[i][0] = row.getKey();
                data[i][1] = row.getValue();
                i++;
            }
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable table = new JTable(model);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, centerRenderer);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 16));
            JTableHeader header = table.getTableHeader();
            header.setPreferredSize(new Dimension(header.getWidth(), 30));
            header.setFont(new Font("Arial", Font.BOLD, 16));
    
            JScrollPane scrollPane = new JScrollPane(table);
            tableFrame.add(scrollPane, BorderLayout.CENTER);
            tableFrame.pack();
            tableFrame.setVisible(true);
        }
        else {
            DecimalFormat doubleFormat = new DecimalFormat("#.###");
            String[] columnNames = {"Original Letter", "Decrypted Letter", "Repeated"};
            String[][] data = new String[decryptResult.get(cycleIndex).second.size()][3];
            int i = 0;
            for(Map.Entry<Character, Pair<Character, Double>> row : decryptResult.get(cycleIndex).second.entrySet()){
                data[i][1] = row.getKey().toString();
                data[i][0] = row.getValue().first.toString();
                data[i][2] = doubleFormat.format(row.getValue().second)+"%";
                i++;
            }
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable table = new JTable(model);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, centerRenderer);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 16));
            JTableHeader header = table.getTableHeader();
            header.setPreferredSize(new Dimension(header.getWidth(), 30));
            header.setFont(new Font("Arial", Font.BOLD, 16));
    
            JScrollPane scrollPane = new JScrollPane(table);
            tableFrame.add(scrollPane, BorderLayout.CENTER);
            tableFrame.pack();
            tableFrame.setVisible(true);
        }

        tableFrame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                newFrameButton.setEnabled(true);
            }

            @Override
            public void windowOpened(WindowEvent e){
                newFrameButton.setEnabled(false);
            }
        });
    }
}