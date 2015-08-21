import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

public class Article {
    
    private String Source_Article;
    List<Paragraph> Article_Paragraphs = new ArrayList<>();
    private final List<Paragraph> Abstract_Paragraph = new ArrayList<>();
    private final List<Paragraph> Content_Paragraph = new ArrayList<>();
    private int NumberOfAbstractSentence = 0;
    List<Representations> Representations = new ArrayList<>();
    
    private ListenableUndirectedWeightedGraph<Sentence, DefaultEdge> TextRankGraph = new ListenableUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
    
    public String ScoredContent = "";
    public String RepresentationStrings = "";
    public String LeadSentencesString = "";
    public String GraphSummary = "";
    public String SummaryString = "";
    
    public String TextRankString = "";
    private double d = 0.85; //damping factor for text rank
    public static int MaximumSentences = 20;
    public static TextRankSummary TextRankObject = new TextRankSummary(MaximumSentences);
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
                
                Representations.add(new Representations(CurrentAbstractSentence.RepresentedParagraph,CurrentAbstractSentence, Lead ));
                temp = temp + Representations.get(Representations.size() - 1).toString();
            }         
        }
        this.LeadSentencesString = temp;
    }
    
    public void RankRepresentedParagraphs(){
        for (Representations Representation : Representations) {
            Representation.RankRepresentedParagraph();
            GraphSummary = GraphSummary + Representation.GraphString;
            SummaryString = SummaryString + Representation.Summary;
        }
    }
    
    public void TextRank(){
        
        //add weigth edges
        for (Paragraph currentParagraph : Article_Paragraphs) {
            for (Sentence currentSentence : currentParagraph.Paragraph_Sentences) {
                TextRankGraph.addVertex(currentSentence);
                for (Paragraph otherParagraph : Article_Paragraphs) {
                    for (Sentence otherSentence : otherParagraph.Paragraph_Sentences) {
                        if(currentSentence.equals(otherSentence)){
                            continue;
                        }
                        double score = UtilityClass.TextRankScoring(currentSentence, otherSentence);
                        TextRankGraph.addVertex(otherSentence);
                        TextRankGraph.addEdge(currentSentence, otherSentence);
                        DefaultEdge e = TextRankGraph.getEdge(otherSentence, currentSentence);
                        TextRankGraph.setEdgeWeight(e, score);
                        /*
                        System.out.println("\n\nSentence: " + currentSentence.Sentence_String
                                    +"\nkeywords: " + currentSentence.Sentence_Keywords.toString()
                                    +"\nSentence: " + otherSentence.Sentence_String
                                    +"\nkeywords: " + otherSentence.Sentence_Keywords.toString()
                                    + "\n\tSimilar Keywords: " + currentSentence.GetSimilarKeywords(otherSentence).toString()
                                    +"\n\tScore: " + score);
                        */
                        
                    }
                }
            }
        }
        
        //add vertex weight
        for (Paragraph currentParagraph : Article_Paragraphs) {
            for (Sentence currentSentence : currentParagraph.Paragraph_Sentences) {
                double DenominatorSum = 0;
                for (Paragraph otherParagraph : Article_Paragraphs) {
                    for (Sentence otherSentence : otherParagraph.Paragraph_Sentences) {
                        if(currentSentence.equals(otherSentence)){
                            continue;
                        }
                        DefaultEdge e = TextRankGraph.getEdge(otherSentence, currentSentence);
                        DenominatorSum = DenominatorSum + TextRankGraph.getEdgeWeight(e);
                    }
                }
                double NumeratorSum = 0;
                for (Paragraph otherParagraph : Article_Paragraphs) {
                    for (Sentence otherSentence : otherParagraph.Paragraph_Sentences) {
                        if(currentSentence.equals(otherSentence)){
                            continue;
                        }
                        DefaultEdge e = TextRankGraph.getEdge(otherSentence, currentSentence);
                        NumeratorSum = NumeratorSum + (TextRankGraph.getEdgeWeight(e) / DenominatorSum);
                    }
                }
                Double score = (1 - d) + (d * NumeratorSum);
                currentSentence.TextRankVertexScore = score;
                TextRankObject.addSentence(currentSentence);
                TextRankString = TextRankString + score + "\n";
            }
        }
    }
}

class Representations{
    public final Paragraph RepresentedParagraph;
    public final Sentence AbstractSentence;
    public final Sentence LeadSentence;
    public String GraphString = "";
    public String Summary = "";
    Representations(Paragraph p, Sentence a, Sentence l){
        RepresentedParagraph = p;
        AbstractSentence = a;
        LeadSentence = l;
    }
    public void RankRepresentedParagraph(){
        GraphString = AbstractSentence.RepresentedParagraph.GraphSentences();
        //System.out.println("\n"+"Abstract Sentence: " + AbstractSentence.Sentence_String+"\nParagraph Summary: \n");
        //Summary = Summary + "\n\n"+ AbstractSentence.Sentence_String+"\n";
        //AbstractSentence.RepresentedParagraph.ResetVisitFlag();
        Summary = Summary + AbstractSentence.RepresentedParagraph.FindSubGraph(LeadSentence) + "\n";
    }
    @Override
    public String toString(){
        if(LeadSentence == null){
            return "\n\nAbstract Sentence: " + AbstractSentence.Sentence_String
                +"\n\tPagragraph: " + RepresentedParagraph.Paragraph_String
                + "\n\tNo Lead Sentence Found!";
        }
        else{
            return "\n\nAbstract Sentence: " + AbstractSentence.Sentence_String
                +"\n\tPagragraph: " + RepresentedParagraph.Paragraph_String
                +"\n\tLeadSentence: " + LeadSentence.Sentence_String;
        }
    }
}

class TextRankSummary{
    static List<Sentence> SummarySentence = new ArrayList<>();
    Sentence LowestScoringSentence;
    private static int MaxSentences;
    public TextRankSummary(int Max){
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