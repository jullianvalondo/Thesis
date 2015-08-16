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
    private List<Paragraph> Content_Paragraph = new ArrayList<>();
    private int NumberOfAbstractSentence = 0;
    
    public String ScoredContent = "";
    public String RepresentationStrings = "";
    public String LeadSentencesString = "";
    //private List<RepresentedParagraph> RepresentedAbstractParagraph = new ArrayList<>();
    public Article(String File_Path) throws FileNotFoundException{
        Source_Article = "";
        DocumentPreprocessor dp = new DocumentPreprocessor(File_Path);
        dp.setSentenceDelimiter("\n");
        boolean Flag_IsAbstractParagraph = false;
        for (List<HasWord> paragraph : dp) {            
            String ss = UtilityClass.ListToString(paragraph);
            //check if input is header
            Pattern p = Pattern.compile("[^IVXLCDM]\\.");
            Matcher m = p.matcher(ss);
            if(!m.find()){
                //check if the header contains "abstract" String
                Flag_IsAbstractParagraph = ss.toLowerCase().contains("Abstract".toLowerCase());
            }
            //else; the input is a paragpraph
            else{
                Source_Article = Source_Article + ss + "\n";
                Paragraph Current_Paragraph = new Paragraph(ss, Flag_IsAbstractParagraph);
                Article_Paragraphs.add(Current_Paragraph);
                //make a list of abstract and non abstract paragraph
                if(Current_Paragraph.IsAbstractParagraph == true){
                    Abstract_Paragraph.add(Current_Paragraph);
                    NumberOfAbstractSentence = NumberOfAbstractSentence + Abstract_Paragraph.get(Abstract_Paragraph.size() - 1).Paragraph_Sentences.size();
                }
                else{
                    Content_Paragraph.add(Current_Paragraph);
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
    public void Score_Abstract_Sentence_To_Paragprahs(){
        for (int i = 0; i < Abstract_Paragraph.size(); i++) {
            Paragraph CurrentAbstractParagraph = Abstract_Paragraph.get(i);
            for (int j = 0; j < CurrentAbstractParagraph.Paragraph_Sentences.size(); j++) {
            Sentence CurrentAbstractSentence = CurrentAbstractParagraph.Paragraph_Sentences.get(j);
                for (int k = 0; k < Content_Paragraph.size(); k++) {
                    Paragraph CurrentContentParagraph = Content_Paragraph.get(k);
                    
                    List <String> Similar_Keywords = new ArrayList<>();
                    double Score = Abstract_Paragraph.get(i).Paragraph_Sentences.get(j).GetNumberOfCooccuringKeywords(Content_Paragraph.get(k));
                    Score = Score / Abstract_Paragraph.get(i).Paragraph_Sentences.get(j).Sentence_Keywords.Keyword_Number * 100;
                    Similar_Keywords = CurrentAbstractSentence.GetSimilarKeywords(CurrentContentParagraph);
                    ScoredContent = ScoredContent + "\n\nAbstract Sentence: " + CurrentAbstractSentence.Sentence_String 
                                      + "\n\tParagraph: " + CurrentContentParagraph.Paragraph_String
                                      +"\n\tScore: " + Score
                                        +"\n\tSimilar Keywords: " + Similar_Keywords.toString()
                                        +"\n\tParagraph Keywords: " + CurrentContentParagraph.Paragraph_Keyword.Keywords
                                        +"\n\tAbstract Sentence Keywords: " +CurrentAbstractSentence.Sentence_Keywords.Keywords;
                    CurrentAbstractSentence.addRepresentation(CurrentContentParagraph, Score, Similar_Keywords);
                            
                }              
            }         
        }        
        //System.out.println("\nRepresented Abstract Sentences: " + NumberOfAbstractSentence);
        for (int i = 0; i < Abstract_Paragraph.size(); i++) {
            Paragraph CurrentAbstractParagraph = Abstract_Paragraph.get(i);
            for (int j = 0; j < CurrentAbstractParagraph.Paragraph_Sentences.size(); j++) {
                Sentence CurrentAbstractSentence = CurrentAbstractParagraph.Paragraph_Sentences.get(j);
                RepresentationStrings = RepresentationStrings + "\n"+CurrentAbstractSentence.RepresentationtoString();
                //System.out.println(CurrentAbstractSentence.RepresentationtoString());
            }         
        } 
    }
    public void FindLeadSentenceOfRepresentedParagraphs(){
        String temp = "";
        for (int i = 0; i < Abstract_Paragraph.size(); i++) {
            Paragraph CurrentAbstractParagraph = Abstract_Paragraph.get(i);
            for (int j = 0; j < CurrentAbstractParagraph.Paragraph_Sentences.size(); j++) {
                Sentence CurrentAbstractSentence = CurrentAbstractParagraph.Paragraph_Sentences.get(j);
                Sentence Lead = CurrentAbstractSentence.RepresentedParagraph.FindLeadSentence(CurrentAbstractSentence.Sentence_Keywords);
                
                
                if(Lead == null){
                    temp = temp + "\n\nAbstract Sentence: " + CurrentAbstractSentence.Sentence_String
                            +"\n\tPagragraph: " + CurrentAbstractSentence.RepresentedParagraph.Paragraph_String
                            + "\n\tNo Lead Sentence Found!";
                    continue;
                }
                
                temp = 
                        temp
                        + "\n\nAbstract Sentence: " + CurrentAbstractSentence.Sentence_String 
                        +"\n\tPagragraph: " + CurrentAbstractSentence.RepresentedParagraph.Paragraph_String
                        +"\n\tLeadSentence: " + Lead.Sentence_String
                        +"\n\tKeywords: ";
            }         
        }
        this.LeadSentencesString = temp;
    }
}