import edu.stanford.nlp.ling.HasWord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
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
    
    public static boolean CheckIfInString( String a, String b ) {
      Pattern x = Pattern.compile(b);
      Matcher y = x.matcher(a);
      return y.find();
    }    
    
    public static boolean ifInList(String subString, List <String> MainString){
        for (String MainString1 : MainString) {
            if (MainString1.equalsIgnoreCase(subString)) {
                return true;
            }
        }
        return false;
    }    
    
    public static void OutputFile(String FilePath, String OutputString, String FileName) throws IOException{
        BufferedReader inputStream = null;        
        PrintWriter outputStream = null;
        try {
            Reader reader_sp = new StringReader(OutputString);
            inputStream = new BufferedReader(reader_sp);
            outputStream = new PrintWriter(new FileWriter(FilePath + "\\" + FileName));
            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }
            outputStream.close();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
    
    public static String GetFileName(File openFile){
        String name = "";
        int pos = openFile.getName().lastIndexOf(".");
        if(pos != -1) {
           name = openFile.getName().substring(0, pos);
        }
        return name;
    }
}
