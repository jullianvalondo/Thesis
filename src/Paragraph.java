
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Paragraph{     
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
            SentenceToReturn = SentenceToReturn + "\n\tSentence: " + Paragraph_Sentence.toString()
                                                +Paragraph_Sentence.Sentence_Keywords.toString();
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
    
    public void TextRank(){
        ListenableUndirectedWeightedGraph<Sentence, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<Sentence, DefaultEdge>(DefaultWeightedEdge.class);
        for (int i = 0; i < Paragraph_Sentences.size(); i++) {
            Sentence currentSentence = Paragraph_Sentences.get(i);
            graph.addVertex(currentSentence);
            for (int j = 0; j < Paragraph_Sentences.size(); j++) {
                if(i == j){
                    continue;
                }
                Sentence otherSentence =  Paragraph_Sentences.get(j);
                graph.addVertex(otherSentence);
                
                graph.addEdge(otherSentence, currentSentence);
                graph.setEdgeWeight(graph.getEdge(otherSentence, currentSentence), currentSentence.GetNumberOfCooccuringKeywords(otherSentence) / currentSentence.Sentence_Keywords.Keywords.size());
            }
        }
        System.out.println("Graph:\n" + graph.toString() + "\n");
        
    }
    
    public Sentence FindLeadSentence(Keyword Other_Keywords){
        double MaxScore = 0;
        double score = 0;
        Sentence tempSentence = null;
        for (int i = 0; i < Paragraph_Sentences.size(); i++) {
            score = Paragraph_Sentences.get(i).GetNumberOfCooccuringKeywords(Other_Keywords);
            if(score > MaxScore){
                MaxScore = score;
                tempSentence = Paragraph_Sentences.get(i);
            }
        }
        return tempSentence;
    }
}
