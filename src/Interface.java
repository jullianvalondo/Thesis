
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interface {
    public static List<String> Stopwords= new ArrayList<String>();
    public static File stopwordfile = new File("src//stopwords.txt");
    public static List<Article> FileArticle = new ArrayList<>();
    
    public static void main(String[] args) throws FileNotFoundException {
        
        //stopwords
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(stopwordfile.toPath(), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                Stopwords.add(line);
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }        
        
        //interface for FILE input
        final JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("Plain Text Files", "txt"));
        fc.showOpenDialog(null);
        File file_source[] = fc.getSelectedFiles();

        for(int file_ctr = 0; file_ctr < fc.getSelectedFiles().length; file_ctr++)
        {            
            String arg = file_source[file_ctr].getAbsolutePath();
            //System.out.println(arg);
            FileArticle.add(new Article(arg));
            //Article InputText = new Article(arg);
            //System.out.println(InputText.toString());
        }
        for (int i = 0; i < FileArticle.size(); i++) {
            System.out.println(file_source[i].getAbsolutePath());
            System.out.println(FileArticle.get(i).toString());
        }
    }    
}
