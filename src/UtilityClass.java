import edu.stanford.nlp.ling.HasWord;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityClass {
    
    //make a list into a sentence
    static String ListToString(List<HasWord> paragraph){
        int myListSize = paragraph.size();
        StringBuilder ss = new StringBuilder(4);
        for (int h=0; h < myListSize; h++) {
            if(h < myListSize -1 && (paragraph.get(h + 1).toString().equalsIgnoreCase(".") || paragraph.get(h + 1).toString().equalsIgnoreCase(",") )){  
                ss.append(paragraph.get(h));
            }
            else{
                ss.append(paragraph.get(h)).append(" ");
            }
        }
        return ss.toString();
    }

    static String ListToString_Sentence(List<Sentence> paragraph) {
        int myListSize = paragraph.size();
        StringBuilder ss = new StringBuilder(4);
        for (int h=0; h < myListSize; h++) {
            if(h < myListSize -1 && (paragraph.get(h + 1).toString().equalsIgnoreCase(".") || paragraph.get(h + 1).toString().equalsIgnoreCase(",") )){  
                ss.append(paragraph.get(h));
            }
            else{
                ss.append(paragraph.get(h)).append(" ");
            }
        }
        return ss.toString();    
    }
    
    public static boolean CheckIfInString( String haystack, String needle ) {
      Pattern p = Pattern.compile(needle);
      Matcher m = p.matcher(haystack);
      return m.find();
    }    
    
    public static boolean ifInList(String subString, List <String> MainString){
        for (String MainString1 : MainString) {
            if (MainString1.equalsIgnoreCase(subString)) {
                return true;
            }
        }
        return false;
    }    
}
