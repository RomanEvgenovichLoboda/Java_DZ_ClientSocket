import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MyGUI extends JFrame {
    private JButton selectButton = new JButton("Select");
    private JButton sendtButton = new JButton("Send");
//    private JList<String> pictureList = new JList<>();
    private JLabel fileLabel = new JLabel("No File");
    private Container container;
    private JLabel imageLabel = new JLabel();
    private JPanel panel ;
//    private ImageIcon imageIcon = new ImageIcon();

    public MyGUI(){
        super("Client");
        this.setBounds(100,100,250,250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        selectButton.addActionListener(new SelectActionListener());
        sendtButton.addActionListener(new SendActionListener());
        sendtButton.setEnabled(false);
        container = this.getContentPane();
        container.setLayout(new GridLayout(2,2,2,2));
        container.add(selectButton);
        container.add(sendtButton);
        container.add(fileLabel);

        container.add(imageLabel);
//        container.add(panel);
    }
    class SelectActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                // user selects a file
                File selectedFile = fileChooser.getSelectedFile();
                sendtButton.setEnabled(true);
                fileLabel.setText(selectedFile.getName());

                ImageIcon icon = scaleImage( new ImageIcon(selectedFile.getAbsolutePath()),100,100);

                imageLabel.setIcon(icon);
                JOptionPane.showMessageDialog(null,selectedFile.getName(),"Result:",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
    class SendActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            fileLabel.setText("No File");
            sendtButton.setEnabled(false);
            imageLabel.setIcon(null);
        }
    }
}
