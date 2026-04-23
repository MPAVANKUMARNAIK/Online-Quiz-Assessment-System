
import javax.swing.*;
import java.io.*;

public class ScoreHistoryWriter extends JFrame {
    public ScoreHistoryWriter(String name){
        setSize(300,300);
        JTextArea area=new JTextArea();
        try(BufferedReader br=new BufferedReader(new FileReader("data/score_history.txt"))){
            String l; while((l=br.readLine())!=null) if(l.startsWith(name)) area.append(l+"\n");
        }catch(Exception e){}
        add(area);
        setVisible(true);
    }
}
