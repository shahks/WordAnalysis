import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahks on 4/4/2016.
 */
public class CETR {
    private Document document;
    CETR(Document document){
        this.document = document;
    }
    public String getMainContent() throws FileNotFoundException, UnsupportedEncodingException,IOException {
        Document doc; //Temporary document to convert each line into document
        int denominator;    //denominator for ratio, check if 0 make it 1
        BufferedReader br = new BufferedReader(new FileReader("abc.txt"));
        List<Document> line_list = new ArrayList<Document>();   //Store each line of document
        List<Double> tag_ratio = new ArrayList<Double>();   //store tag_ratio
        String line;
        //System.out.println(document);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select(".hidden,style,noscript,script,.jscript").remove();
        //System.out.println(document.select("*"));
        PrintWriter writer = new PrintWriter("abc.txt");
        writer.println(document);

        double avg = 0.0;
        double ratio;
        int zerocount = 0;
        while((line = br.readLine()) != null){
            doc = Jsoup.parse(line);
            line_list.add(doc);
            Elements elements = doc.select("*");
            if((denominator = elements.size()) == 0){
                denominator = 1;
            }
            ratio = (double) doc.text().length()/(double) denominator;
            if (ratio == 0.0)
                zerocount++;
            avg += ratio;
            tag_ratio.add(ratio);
        }
        avg = avg / (double)(tag_ratio.size() - zerocount);
        //avg = avg ;
        StringBuffer return_string = new StringBuffer();
        for(int i = 0; i < tag_ratio.size(); i++){
            double value = tag_ratio.get(i);
            if( value > avg){
                return_string.append(line_list.get(i).text());
            }
        }
        //System.out.println(return_string);
        return return_string.toString();
    }
}
