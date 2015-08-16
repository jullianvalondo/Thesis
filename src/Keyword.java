import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

public class Keyword {
    public List<String> Keywords = new ArrayList<String>();
    public final int Keyword_Number;
    private final String Original_Source;
    public List <String> Temporary_Similar_Keywords;
     public Keyword(){
         Original_Source = null;
         Keyword_Number = 0;
     }
    public Keyword(String Parameter_Sentence) throws FileNotFoundException{        

        this.Original_Source = Parameter_Sentence;
        String text = Parameter_Sentence;
        
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(text);

        pipeline.annotate(document);


        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for(CoreMap sentence: sentences) {

          for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {

            String word = token.get(CoreAnnotations.TextAnnotation.class);
            //"[^a-zA-Z0-9\\\\s]*"
            if(word.matches("[^a-zA-Z\\\\s]*") || UtilityClass.ifInList(word, Interface.Stopwords)){
                continue;
            }            

            String lemma = token.get(LemmaAnnotation.class);
              Keywords.add(lemma);
              //Keywords.add(Lemmatize(lemma));
          }
        }
        
        Keywords = new ArrayList<>(new LinkedHashSet<>(Keywords));
        Keyword_Number = Keywords.size();
        /*
        Reader reader_kp = new StringReader(Parameter_Sentence);
        PTBTokenizer ptb = new PTBTokenizer(reader_kp, new CoreLabelTokenFactory(), null);
        
        while(ptb.hasNext()){
            String word = ptb.next().toString();            
            //removes any nonword characters.
            if(word.matches("[^a-zA-Z0-9\\\\s]*") || ifStopWord(word)){
                continue;
            }
            CoreLabel token = new CoreLabel();
            token.setWord(word);
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            Morphology morphology = new Morphology();
            word = morphology.lemma(word, pos);
            //stemming
            
            
            Keywords.add(word);
        }        
        //insert code for finding keyword
        //Keyword_Number = Keywords.length;
        */
    }
    
    
    //recursive lemmatization. more effective on some words but sacrifices efficiency
    
    private String Lemmatize(String text){
        String lemma = "";
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(text);
        pipeline.annotate(document);       
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
          for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
            lemma = token.get(LemmaAnnotation.class);
          }
        }
        if(text.equals(lemma))
            return lemma;
        else
            return Lemmatize(lemma);
    }
    
    @Override
    public String toString(){
        String toRet = "\n\t\tKeywords: ";
        for (String Keyword : Keywords) {
            toRet = toRet + "[" + Keyword + "]" + " ";
        }
        return toRet;
    }    
    
    //to be used for the sentence to find similar keywords, finding lead sentence from keywords in abstact sentence 
    public int Score_Similar_Keywords(Keyword Other_Keywords){
        Temporary_Similar_Keywords = Keywords;
        Temporary_Similar_Keywords.retainAll(Other_Keywords.Keywords);
        return Temporary_Similar_Keywords.size();
    }
    public List<String> Get_Similar_Keywords(Keyword Other_Keywords){
        Temporary_Similar_Keywords = Keywords;
        Temporary_Similar_Keywords.retainAll(Other_Keywords.Keywords);
        //sort all keywords in alphabetical order
        java.util.Collections.sort(Temporary_Similar_Keywords);
        return Temporary_Similar_Keywords;
    }
    
    public int Score_Similar_Keywords(List<String> Other_Keywords){
        Temporary_Similar_Keywords = Keywords;
        Temporary_Similar_Keywords.retainAll(Other_Keywords);
        return Temporary_Similar_Keywords.size();
    }
    public List<String> Get_Similar_Keywords(List<String> Other_Keywords){
        Temporary_Similar_Keywords = Keywords;
        Temporary_Similar_Keywords.retainAll(Other_Keywords);
        //sort all keywords in alphabetical order
        java.util.Collections.sort(Temporary_Similar_Keywords);
        return Temporary_Similar_Keywords;
    }    

    public static boolean ifStopWord(String word){
        for (int i = 0; i < Interface.Stopwords.size(); i++) {
            if(Interface.Stopwords.get(i).equalsIgnoreCase(word))
                return true;
        }
        return false;
    }

}
