
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Paragraph{
    public final double threshold = Thesis.SentenceSimilarityThreshold; // 10%
    
    public final String Paragraph_String;
    public final boolean IsAbstractParagraph;
    public final Keyword Paragraph_Keyword;
    List<Sentence> Paragraph_Sentences = new ArrayList<>();
    
    ListenableUndirectedWeightedGraph<Sentence, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
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
    
    public String TextRank(){
        String ret = "";
        //ListenableUndirectedWeightedGraph<Sentence, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<Sentence, DefaultEdge>(DefaultWeightedEdge.class);
        for (Sentence currentSentence : Paragraph_Sentences) {
            graph.addVertex(currentSentence);
        }
        for (int i = 0; i < Paragraph_Sentences.size(); i++) {
            Sentence currentSentence = Paragraph_Sentences.get(i);
            //graph.addVertex(currentSentence);
            for (int j = 0; j < Paragraph_Sentences.size(); j++) {
                if(i == j){
                    continue;
                }
                Sentence otherSentence =  Paragraph_Sentences.get(j);
                //graph.addVertex(otherSentence);
                double similarity_score = currentSentence.GetNumberOfCooccuringKeywords(otherSentence);// / currentSentence.Sentence_Keywords.Keywords.size();
                double score = similarity_score/currentSentence.Sentence_Keywords.Keywords.size() * 100;
                
                ret = ret + "\nSentence: " + currentSentence.Sentence_String
                                    +"\nkeywords: " + currentSentence.Sentence_Keywords.toString()
                                    +"\nSentence: " + otherSentence.Sentence_String
                                    +"\nkeywords: " + otherSentence.Sentence_Keywords.toString()
                                    + "\n\tSimilar Keywords: " + currentSentence.GetSimilarKeywords(otherSentence).toString()
                                    +"\n\tSimilarity Score: " + similarity_score
                                    +"\n\tScore: " + score;
                
                graph.addEdge(otherSentence, currentSentence);
                DefaultEdge e = graph.getEdge(otherSentence, currentSentence);
                if(e == null){
                    JOptionPane.showMessageDialog(null, "Warning. The edge is null at\nSentence: " + otherSentence.Sentence_String + "\nSentence: " + currentSentence.Sentence_String);
                }
                
                //    JOptionPane.showMessageDialog(null, "Warning. The score is null at\nSentence: " + otherSentence.Sentence_String + "\nSentence: " + currentSentence.Sentence_String); 
                
                graph.setEdgeWeight(e, score);
            }
        }
        //System.out.println("Graph:\n" + graph.toString() + "\n");
        return ret;
    }
    
    public String FindSubGraph(Sentence CurrentSentence){
        String toRet = "";
        if(CurrentSentence.visited == true){
            return toRet;
        }
        else{
            //System.out.println(CurrentSentence);
            toRet = "\n"+CurrentSentence.toString();
            CurrentSentence.visited = true;
        }
        for (Sentence otherSentence : Paragraph_Sentences) {
            if(CurrentSentence.equals(otherSentence)){
                continue;
            }
            double current_score = graph.getEdgeWeight(graph.getEdge(CurrentSentence, otherSentence));
            if(current_score >= threshold){
                toRet = toRet + FindSubGraph(otherSentence);
            }
        }
        return toRet;
    }
    
    public void ResetVisitFlag(){
        for (Sentence thisSentence : Paragraph_Sentences) {
            thisSentence.visited = false;
        }
    }
    
    public Sentence FindLeadSentence(Keyword Other_Keywords){
        double MaxScore = 0;
        double score;
        Sentence tempSentence = null;
        for (Sentence Paragraph_Sentence : Paragraph_Sentences) {
            score = Paragraph_Sentence.GetNumberOfCooccuringKeywords(Other_Keywords);
            if (score > MaxScore) {
                MaxScore = score;
                tempSentence = Paragraph_Sentence;
            }
        }
        return tempSentence;
    }
}