import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyGUI extends JFrame {
    Socket client = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    String msg = "";
    int port = 8888;
    File selectedFile;
    private JButton selectButton = new JButton("Select");
    private JButton sendButton = new JButton("Send");
    private JButton exitButton = new JButton("Exit");
    private JLabel imageLabel = new JLabel();
    private Container container;

    public MyGUI() {
        super("Client");
        this.setBounds(100, 100, 250, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        exitButton.addActionListener(new ExitActionListener());
        selectButton.addActionListener(new SelectActionListener());
        sendButton.addActionListener(new SendActionListener());
        sendButton.setEnabled(false);
        container = this.getContentPane();
        container.setLayout(new GridLayout(2, 2, 2, 2));
        container.add(selectButton);
        container.add(sendButton);
        container.add(exitButton);
        container.add(imageLabel);
    }

    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }
        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public void setConnection() {
        try {
            client = new Socket("127.0.0.1", port);
            JOptionPane.showMessageDialog(this, "Connected to server", "Result:", JOptionPane.PLAIN_MESSAGE);
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Client Error:", JOptionPane.PLAIN_MESSAGE);
        }
    }

    void sendMessage(String msg) {
//        try {
//            BufferedImage bImage = ImageIO.read(selectedFile);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(bImage, Files.probeContentType(selectedFile.toPath()),baos);
//            byte[]imageInByte = baos.toByteArray();
//            out.writeObject(imageInByte);
//            out.flush();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, e.getMessage(), "Client Error:", JOptionPane.PLAIN_MESSAGE);
//        }


        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Client Error:", JOptionPane.PLAIN_MESSAGE);
        }
    }

    class SelectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                sendButton.setEnabled(true);
                ImageIcon icon = scaleImage(new ImageIcon(selectedFile.getAbsolutePath()), 100, 100);
                imageLabel.setIcon(icon);
                JOptionPane.showMessageDialog(null, selectedFile.getName(), "Result:", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    class SendActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            sendButton.setEnabled(false);
//            imageLabel.setIcon(null);
//            try {
//                out = new ObjectOutputStream(client.getOutputStream());
//                out.flush();
//                in = new ObjectInputStream(client.getInputStream());
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:", JOptionPane.PLAIN_MESSAGE);
//            }


//            try {
//                BufferedImage bImage = ImageIO.read(selectedFile);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream(client.getOutputStream().);
//                ImageIO.write(bImage, Files.probeContentType(selectedFile.toPath()),baos);
//                byte[]imageInByte = baos.toByteArray();
//                out.writeObject(imageInByte);
//                out.flush();
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Client Error:", JOptionPane.PLAIN_MESSAGE);
//            }



            msg = JOptionPane.showInputDialog("Enter your message:", "");
            if (msg == null) msg = "";
            sendMessage(msg);
            if (!msg.equals("exit")) {
                try {
                    msg = (String) in.readObject();
                } catch (ClassNotFoundException | IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:", JOptionPane.PLAIN_MESSAGE);
                }
                JOptionPane.showMessageDialog(null, "server> " + msg, "Response:", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            sendMessage("exit");
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
                if (client != null)
                    client.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Client Error:", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }
}
