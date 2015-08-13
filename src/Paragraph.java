
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Paragraph {
    public final String Paragraph_String;
    public final boolean IsAbstractParagraph;
    public final Keyword Paragraph_Keyword;
    List<Sentence> Paragraph_Sentences = new ArrayList<>();
    public Paragraph(String source, boolean checker) throws FileNotFoundException{
        IsAbstractParagraph = checker;
        Paragraph_String = source;
        Paragraph_Keyword = new Keyword(Paragraph_String);
        Reader reader_sp = new StringReader(Paragraph_String);
        DocumentPreprocessor sp = new DocumentPreprocessor(reader_sp);                
        for (List<HasWord> sentence : sp) {
            String sentence_string = UtilityClass.ListToString(sentence);
            Sentence CurrentSentence = new Sentence(sentence_string);
            Paragraph_Sentences.add(CurrentSentence);         
        }
    }
    @Override
    public String toString(){
        String SentenceToReturn = "";      
        for (Sentence Paragraph_Sentence : Paragraph_Sentences) {
            SentenceToReturn = SentenceToReturn + "\n\tSentence: " + Paragraph_Sentence.toString();
        }
        if(IsAbstractParagraph){
            return "Abstract Paragraph: " + Paragraph_String + Paragraph_Keyword.toString()+ SentenceToReturn;
        }
        //return "Paragraph: " + Paragraph_String + "\nKeywords: " + Paragraph_Keyword.toString()+ SentenceToReturn;
        return "Paragraph: " + Paragraph_String + Paragraph_Keyword.toString()+ SentenceToReturn;
    }
    
    //percentage of OtherSentenceKeywords/ThisParagraphKeywords
    public double GetRatioOfCooccuringKeywords(Sentence Other_Sentence){
       return GetNumberOfCooccuringKeywords(Other_Sentence) / Paragraph_Keyword.Keyword_Number; 
    }    
    public int GetNumberOfCooccuringKeywords(Sentence Other_Sentence){
        return Paragraph_Keyword.Score_Similar_Keywords(Other_Sentence.Sentence_Keywords);
    }
    public List<String> GetSimilarKeywords(Sentence Other_Sentence){
        return Paragraph_Keyword.Get_Similar_Keywords(Other_Sentence.Sentence_Keywords);
    }    
}
