
import java.io.FileNotFoundException;

public class Sentence {
    public final String Sentence_String;
    public final Keyword Sentence_Keywords;
    public Sentence(String Source_Sentence) throws FileNotFoundException{
        Sentence_String = Source_Sentence;
        Sentence_Keywords = new Keyword(Sentence_String);
    }
    
    @Override
    public String toString(){
        return Sentence_String + Sentence_Keywords.toString();
    }
    
    public int GetNumberOfCooccuringKeywords(Sentence Other_Sentence){
        return Sentence_Keywords.Score_Similar_Keywords(Other_Sentence.Sentence_Keywords);
    }
    //percentage of OtherSentenceKeywords/ThisSentenceKeywords
    public double GetRatioOfCooccuringKeywords(Sentence Other_Sentence){
       return Sentence_Keywords.Score_Similar_Keywords(Other_Sentence.Sentence_Keywords) / Sentence_Keywords.Keyword_Number; 
    }
            
    //insert code for finding supporting sentence: use Score_Keyword_Existence.
    //Refer to paragraph of itself
}
