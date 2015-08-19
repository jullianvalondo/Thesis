import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interface {
    public static List<String> Stopwords= new ArrayList<String>();
    public static File stopwordfile = new File("src//stopwords.txt");
    public static List<Article> FileArticle = new ArrayList<>();
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
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
            //input file
            String arg = file_source[file_ctr].getAbsolutePath();
            FileArticle.add(new Article(arg));
        }
        for (int i = 0; i < FileArticle.size(); i++) {
            String AbsolutePath = file_source[i].getAbsolutePath();
            String filePath = AbsolutePath.substring(0,AbsolutePath.lastIndexOf(File.separator));
            String FileName = UtilityClass.GetFileName(file_source[i]);
            
            //System.out.println(file_source[i].getAbsolutePath());
            
            //parse the file input
            //System.out.println("Article " + i+ ": ");
            //System.out.println(FileArticle.get(i).toString());
            //System.out.println("File Parsed.");
            UtilityClass.OutputFile(filePath, FileArticle.get(i).toString(), FileName + "_Parsed.txt");
            
            
            //score each keywords
            FileArticle.get(i).Score_Abstract_Sentence_To_Paragprahs();
            //System.out.println("Scored Sentences: ");
            //System.out.println(FileArticle.get(i).ScoredContent);
            //System.out.println("Article Scored.");
            UtilityClass.OutputFile(filePath, FileArticle.get(i).ScoredContent, FileName + "_Scores.txt");            
            
            //rank each scores
            //System.out.println("Paragraphs to be used for making summary: ");
            //System.out.println(FileArticle.get(i).RepresentationStrings);
            UtilityClass.OutputFile(filePath, FileArticle.get(i).RepresentationStrings, FileName + "_Scores_Ranked.txt");
            
            //find lead sentences in top scored paragraph representation of abstract sentences to content paragraph
            FileArticle.get(i).FindLeadSentenceOfRepresentedParagraphs();
            UtilityClass.OutputFile(filePath, FileArticle.get(i).LeadSentencesString, FileName + "_Lead_Sentences.txt");
            
            FileArticle.get(i).RankRepresentedParagraphs();
            UtilityClass.OutputFile(filePath, FileArticle.get(i).GraphSummary, FileName + "_Graph_Review.txt");
            UtilityClass.OutputFile(filePath, FileArticle.get(i).SummaryString, FileName + "_Summary.txt");
            
        }
        
        JOptionPane.showMessageDialog(null, "Done Summarizing", "Summarizer", JOptionPane.PLAIN_MESSAGE);
    }    
}
