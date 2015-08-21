
public class Representations{
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
