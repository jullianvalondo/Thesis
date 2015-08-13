
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Sentence {
    public final String Sentence_String;
    public final Keyword Sentence_Keywords;
    
    //fields for represenation of sentences in paragraph
    public Paragraph RepresentedParagraph = null;
    public double RepresentedScore = 0;
    public List <String> Similar_Keywords = new ArrayList<>();
    
    //constructor
    public Sentence(String Source_Sentence) throws FileNotFoundException{
        Sentence_String = Source_Sentence;
        Sentence_Keywords = new Keyword(Sentence_String);
    }
    
    @Override
    public String toString(){
        return Sentence_String + Sentence_Keywords.toString();
    }
    //returns number of similar keywords of another sentence
    public int GetNumberOfCooccuringKeywords(Sentence Other_Sentence){
        return Sentence_Keywords.Score_Similar_Keywords(Other_Sentence.Sentence_Keywords);
    }
    //percentage of OtherSentenceKeywords/ThisSentenceKeywords
    public double GetRatioOfCooccuringKeywords(Sentence Other_Sentence){
        if(Sentence_Keywords.Score_Similar_Keywords(Other_Sentence.Sentence_Keywords) == 0){
            return 0;
        }
        else{
            return Sentence_Keywords.Score_Similar_Keywords(Other_Sentence.Sentence_Keywords) / Sentence_Keywords.Keywords.size();
        }
        
    }
    public List<String> GetSimilarKeywords(Sentence Other_Sentence){
        return Sentence_Keywords.Get_Similar_Keywords(Other_Sentence.Sentence_Keywords);
    }
    
    
     public int GetNumberOfCooccuringKeywords(Paragraph Other_Paragraph){
        return Sentence_Keywords.Score_Similar_Keywords(Other_Paragraph.Paragraph_Keyword);
    }
    //percentage of OtherSentenceKeywords/ThisSentenceKeywords
    public double GetRatioOfCooccuringKeywords(Paragraph Other_Paragraph){
       if(Sentence_Keywords.Score_Similar_Keywords(Other_Paragraph.Paragraph_Keyword) == 0){
           return 0;
       }else{
           return Sentence_Keywords.Score_Similar_Keywords(Other_Paragraph.Paragraph_Keyword)/ Other_Paragraph.Paragraph_Keyword.Keywords.size();
       }  
    }
    public List<String> GetSimilarKeywords(Paragraph Other_Paragraph){
        return Sentence_Keywords.Get_Similar_Keywords(Other_Paragraph.Paragraph_Keyword);
    }   
    //insert code for finding supporting sentence: use Score_Keyword_Existence.
    //Refer to paragraph of itself
    
    //for finding the repesentation of the sentence
    public void addRepresentation(Paragraph CurrentParagraph, double Score, List<String> CurrentKeywords){
        if(Score > this.RepresentedScore){
            RepresentedParagraph = CurrentParagraph;
            RepresentedScore = Score;
            Similar_Keywords.clear();
            Similar_Keywords.addAll(CurrentKeywords);
        }
    }
    public String RepresentationtoString(){
                    return "\nAbstract Sentence: " + this.Sentence_String
                                      + "\n\tParagraph: " + RepresentedParagraph.Paragraph_String
                                      +"\n\tScore: " + RepresentedScore
                                        +"\n\tSimilar Keywords: " + Similar_Keywords.toString();
    }
}
