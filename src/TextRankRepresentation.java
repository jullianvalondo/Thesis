
import java.util.ArrayList;
import java.util.List;

public class TextRankRepresentation{
    public List<Sentence> SummarySentence = new ArrayList<>();
    Sentence LowestScoringSentence;
    private static int MaxSentences;
    public TextRankRepresentation(int Max){
        MaxSentences = Max;
    }
    public String getSummary(){
        String Summary = "";
        for (Sentence SummarySentence1 : SummarySentence) {
            Summary = Summary + SummarySentence1.Sentence_String + "\n";
        }
        return Summary;
    }
    public void addSentence(Sentence currentSentence){
        if(SummarySentence.isEmpty()){
            LowestScoringSentence = currentSentence;
            SummarySentence.add(currentSentence);
        }
        else if(SummarySentence.size() == MaxSentences){
            if(LowestScoringSentence.TextRankVertexScore > currentSentence.TextRankVertexScore){
                //if the current added sentence has lower score than the lowest scoring sentence at the list
                return;
            }
            //remove the LowestScoringSentence
            SummarySentence.remove(LowestScoringSentence);
            SummarySentence.add(currentSentence);
            //iterate through the list and find the sentence with the lowest score
            LowestScoringSentence = this.getLowestScoringSentence();
        }
        else{
            SummarySentence.add(currentSentence);
            //iterate through the list and find the sentence with the lowest score
            LowestScoringSentence = this.getLowestScoringSentence();            
        }
    }
    private Sentence getLowestScoringSentence(){
        Sentence temp = SummarySentence.get(0);
        for (Sentence currentSentence : SummarySentence) {
            if(temp.TextRankVertexScore > currentSentence.TextRankVertexScore){
                temp = currentSentence;
            }
        }
        
        return temp;
    }
    
    @Override
    public String toString(){
        return this.getSummary();
    }
}
