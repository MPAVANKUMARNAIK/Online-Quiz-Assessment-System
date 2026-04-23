import java.io.*;

public class ExportUtil {
    public static void exportCSV(){
        try(
            BufferedReader br=new BufferedReader(new FileReader("data/score_history.txt"));
            BufferedWriter bw=new BufferedWriter(new FileWriter("data/export.csv"))
        ){
            String line;
            while((line=br.readLine())!=null){
                bw.write(line);
                bw.newLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}