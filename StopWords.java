/**
 * Created by shahks on 4/3/2016.
 * This class removes the stop words and punctuation and words consisting of only digits
 */
import java.util.*;

class StopWords{
    private final String[] stopWrds = new String[]{"i", "a", "about","an","are","as","at","be","by","com","for","from","how","in", "is","it",
            "of","on","or","that","the","this","to","was","what","when","where","who","will","with","&", "and", "had"};

    private String inString;
    private StringBuffer outString;
    private static int wordLength = 2;

    StopWords(String inp){
        this.inString = inp;
        outString = new StringBuffer();

    }

    public String removeStopWords(){
        try{
            /*removes all punctuations from the string*/
            this.inString = this.inString.replaceAll("\\p{Punct}+","");

            /*remove the stop words*/
            String delims = " ";
            StringTokenizer stringTokenizer = new StringTokenizer(this.inString,delims);
            while(stringTokenizer.hasMoreElements())
            {
                int flag = 1;
                String s1 = stringTokenizer.nextElement().toString();
                s1 = s1.toLowerCase();
                if(s1.matches("[0-9](.*)"))
                    continue;
                for(int i = 0; i < stopWrds.length; i++){
                    if(s1.equals(stopWrds[i])){
                        flag=0;
                    }
                }
                if(flag!=0 && s1.length() > wordLength) {
                    outString.append(s1);
                    outString.append(" ");
                }
            }
        }
        catch(Exception e){
            System.err.println("Input String Error" + e);
        }
        return outString.toString();
    }
}