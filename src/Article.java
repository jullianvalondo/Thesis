
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Article {

    private String Source_Article;
    List<Paragraph> Article_Paragraphs = new ArrayList<>();
    private List<Paragraph> Abstract_Paragraph = new ArrayList<>();
    public Article(String File_Path) throws FileNotFoundException{
        Source_Article = "";
        DocumentPreprocessor dp = new DocumentPreprocessor(File_Path);
        dp.setSentenceDelimiter("\n");
        boolean Flag_IsAbstractParagraph = false;
        for (List<HasWord> paragraph : dp) {            
            String ss = UtilityClass.ListToString(paragraph);
            //JOptionPane.showMessageDialog(null, ss);
            Pattern p = Pattern.compile("[^IVXLCDM]\\.");
            Matcher m = p.matcher(ss);
            if(!m.find()){
                Flag_IsAbstractParagraph = ss.toLowerCase().contains("Abstract".toLowerCase());

            }
            else{
                Source_Article = Source_Article + ss + "\n";
                Article_Paragraphs.add(new Paragraph(ss, Flag_IsAbstractParagraph));
                if(Flag_IsAbstractParagraph){
                    //Abstract_Paragraph.add(Article_Paragraphs.get(Article_Paragraphs.size() - 1));
                }
            }
        }
        //process each article
    }
    @Override
    public String toString(){
        String ParagraphToReturn = "";
        for (Paragraph Article_Paragraph : Article_Paragraphs) {
            ParagraphToReturn = ParagraphToReturn + "\n" + Article_Paragraph.toString() + "\n";
        }
        return "Article: " + Source_Article + ParagraphToReturn;
    }
}
