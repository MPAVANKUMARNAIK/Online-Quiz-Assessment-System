import javax.swing.*;

public class Profile extends JFrame{
    public Profile(String user){
        setSize(300,200);
        add(new JLabel("User Profile: "+user, JLabel.CENTER));
        setVisible(true);
    }
}