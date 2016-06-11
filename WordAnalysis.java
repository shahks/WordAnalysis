import com.intellij.ide.actions.ToggleFloatingModeAction;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.List;

/**
 * Created by shahks on 4/3/2016.
 * This class contains the main program and the program is used to detect the relevancy of the page and removes clutter
 * using TF-IDF(Term Frequency - Inverse Document Frequency) for the term, bigram and trigram as well
 * and also extracting only the main content of the page using CETR (Content Extraction via Tag Ratio) with Threshold
 * method to concentrate only on the main content. Currently, the threshold is the average, need to tune the
 * parameter for optimal results.
 */


public class WordAnalysis {

    private static int numberDocuments;
    public static void main(String [] args) throws IOException, URISyntaxException {
        String usrAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36";
        //String input = "http://www.amazon.com/Cuisinart-CPT-122-Compact-2-Slice-Toaster/dp/B009GQ034C/ref=sr_1_1?s=kitchen&ie=UTF8&qid=1431620315&sr=1-1&keywords=toaster";
        //String input = "http://www.cnn.com/2013/06/10/politics/edward-snowden-profile/";

        System.out.println("Please enter a link:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = args[0];
        System.out.println(input);
        System.out.println("Please wait...");
        Document document = new Document("");
        try{
            document = Jsoup.connect(input).userAgent(usrAgent).timeout(100*1000).get();
            numberDocuments++;
            //System.out.println("fetched : " + input);
        }
        catch (HttpStatusException e){
            //System.out.println("Http Status Exception:" + e);
            //System.exit(-1);
        }
        catch (SocketTimeoutException e){
            //System.out.println("Socket TimeOut Exception:" + e);
        }
        catch (MalformedURLException e){
            //System.out.println("Malformed URL Exception:" + e);
        }
        catch(UnsupportedMimeTypeException e){
            //System.out.println("Unsupported Mime Type Exception");
        }
        catch(UnknownHostException e){
            //System.out.println("Unknown Host Exception");
        }

        String text;
        int type_algo = 2;
        /* 1.CETR + TF-IDF
        *  2.only TF-IDF
        *  */
        switch(type_algo){
            case 1: {
                CETR content = new CETR(document);
                text = content.getMainContent();
                StopWords stp = new StopWords(text);
                text = stp.removeStopWords();
                break;
            }
            default: {
                StopWords stp = new StopWords(document.text());
                text = stp.removeStopWords();
                break;
            }
        }

        HashMap<String, Integer> TFMapword = new HashMap<>();
        HashMap<String, Integer> TFMap2words = new HashMap<>();
        HashMap<String, Integer> TFMap3words = new HashMap<>();
        List<String> removalList = new ArrayList<> ();

        DensityAnalysis densityAnalysis = new DensityAnalysis(text);
        densityAnalysis.TFanalyze1word(TFMapword);
        for(Map.Entry<String,Integer> entry : TFMapword.entrySet()){
            if(entry.getValue() < 3){
                removalList.add(entry.getKey());
            }
        }
        for(String s : removalList){
            TFMapword.remove(s);
        }
        removalList.clear();

        densityAnalysis.TFanalyze2words(TFMap2words);
        for(Map.Entry<String,Integer> entry : TFMap2words.entrySet()){
            if(entry.getValue() < 3){
                removalList.add(entry.getKey());
            }
        }
        for(String s : removalList){
            TFMap2words.remove(s);
        }
        removalList.clear();

        densityAnalysis.TFanalyze3words(TFMap3words);
        for(Map.Entry<String,Integer> entry : TFMap3words.entrySet()){
            if(entry.getValue() < 3){
                removalList.add(entry.getKey());
            }
        }
        for(String s : removalList){
            TFMap3words.remove(s);
        }
        removalList.clear();

        HashMap<String, Integer> IDFMapword = new HashMap<>(TFMapword);
        for(Map.Entry<String,Integer> entry : IDFMapword.entrySet()){
            entry.setValue(1);
        }

        HashMap<String, Integer> IDFMap2words = new HashMap<>(TFMap2words);
        for(Map.Entry<String,Integer> entry : IDFMap2words.entrySet()){
            entry.setValue(1);
        }

        HashMap<String, Integer> IDFMap3words = new HashMap<>(TFMap3words);
        for(Map.Entry<String,Integer> entry : IDFMap3words.entrySet()){
            entry.setValue(1);
        }

        Elements linktag = document.select("a");
        List<String> find_urls = new ArrayList<>();
        URI uri = new URI(input);
        String domain = uri.getHost();
        if(!domain.startsWith("http"))
            domain = "http://" + domain;

        for(Element element : linktag){
            String link = element.attr("href");
            if(link.startsWith("javascript:") || link.startsWith("#")){
                continue;
            }
            if(!link.startsWith("http")){
                link = domain + link;
            }
            if(!link.startsWith(domain))
                continue;
            find_urls.add(link);
            //System.out.println(link);
        }
        Collections.shuffle(find_urls);
        Set<String> hs = new HashSet<>(find_urls);
        List<String> urls = new ArrayList<>(hs);

        int width_factor = 1;
        int depth_factor = 1;
        if(urls.size() >= 100 && urls.size() < 200) {
            width_factor = 2;
        }
        if(urls.size() >= 200 && urls.size() < 300) {
            width_factor = 3;
        }
        if(urls.size() >= 300) {
            width_factor = 4;
        }

        for(int i=0; i< urls.size()/width_factor; i++ ){
            crawler(urls.get(i), IDFMapword, IDFMap2words, IDFMap3words, 1, width_factor,depth_factor);
        }

        HashMap<String, Double> TFIDFMapWord = tf_idf(TFMapword, IDFMapword);
        List<Map.Entry<String,Double>> unsortedWord = new ArrayList<>(TFIDFMapWord.entrySet());
        sort(unsortedWord);
        //printtop5(unsortedWord);
        //System.out.println();
        HashMap<String, Double> TFIDFMap2Words = tf_idf(TFMap2words, IDFMap2words);
        List<Map.Entry<String,Double>> unsorted2Words = new ArrayList<>(TFIDFMap2Words.entrySet());
        sort(unsorted2Words);
        printtop5(unsorted2Words);
        //System.out.println();
        HashMap<String, Double> TFIDFMap3Words = tf_idf(TFMap3words, IDFMap3words);
        List<Map.Entry<String,Double>> unsorted3Words = new ArrayList<>(TFIDFMap3Words.entrySet());
        sort(unsorted3Words);
    }

    public static void crawler(String input,HashMap<String, Integer> IDFMapword, HashMap<String, Integer> IDFMap2words, HashMap<String, Integer> IDFMap3words, int depthlevel, int width_factor, int depth_factor) throws IOException, URISyntaxException {
        if(depthlevel > depth_factor)
            return;
        Document document;
        try{
            String usrAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36";
            document = Jsoup.connect(input).userAgent(usrAgent).timeout(10*1000).get();
            numberDocuments++;
            //System.out.println("fetched : " + input);
            String text = document.text();
            StopWords stp = new StopWords(text);
            text = stp.removeStopWords();

            DensityAnalysis densityAnalysis = new DensityAnalysis(text);
            densityAnalysis.IDFanalyze1word(IDFMapword);
            densityAnalysis.IDFanalyze2words(IDFMap2words);
            densityAnalysis.IDFanalyze3words(IDFMap3words);

            Elements linktag = document.select("a");
            List<String> urls = new ArrayList<>();
            URI uri = new URI(input);
            String domain = uri.getHost();
            if(!domain.startsWith("http"))
                domain = "http://" + domain;

            for(Element element : linktag){
                String link = element.attr("href");
                if(link.startsWith("javascript:") || link.startsWith("#")){
                    continue;
                }
                if(!link.startsWith("http")){
                    link = domain + link;
                }
                if(!link.startsWith(domain))
                    continue;
                urls.add(link);
            }
            Collections.shuffle(urls);
            for(int i=0; i< urls.size()/(width_factor * (depthlevel + 1)) ; i++ ){
                crawler(urls.get(i), IDFMapword, IDFMap2words, IDFMap3words, depthlevel + 1, width_factor,depth_factor);
            }
        }
        catch (HttpStatusException e){
            //System.out.println("Http Status Exception:" + e);
        }
        catch (SocketTimeoutException e){
            //System.out.println("Socket TimeOut Exception:" + e);
        }
        catch (MalformedURLException e){
            //System.out.println("Malformed URL Exception:" + e);
        }
        catch(UnsupportedMimeTypeException e){
            //System.out.println("Unsupported Mime Type Exception");
        }
        catch(UnknownHostException e){
            //System.out.println("Unknown Host Exception");
        }
    }
    public static int findMaxValue(HashMap<String, Integer> map){
        int max = 0;
        for(Map.Entry<String,Integer> mapentry : map.entrySet()){
            int value = mapentry.getValue();
            if(value > max){
                max = value;
            }
        }
        return max;
    }

    public static HashMap<String, Double> tf_idf (HashMap<String, Integer> TFmap, HashMap<String, Integer> IDFmap){
        HashMap<String, Double> TFIDFMap = new HashMap<>();
        int max = findMaxValue(TFmap);
        for(Map.Entry<String,Integer> mapentry : TFmap.entrySet()){
            int tfRawValue = mapentry.getValue();
            int idfRawValue = IDFmap.get(mapentry.getKey());
            double tfvalue = 0.5 + (0.5 * (double)(tfRawValue) / (double)(max));
            double idfvalue = Math.log10(1 + (double)numberDocuments / (double) idfRawValue);
            TFIDFMap.put(mapentry.getKey(), (tfvalue * idfvalue));
        }
        return TFIDFMap;
    }
    public static void sort(List<Map.Entry<String,Double>> unsorted){
        unsorted.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                double first = o1.getValue();
                double second = o2.getValue();
                if(first == second)
                    return 0;
                else
                    return (first < second)? 1 : -1;
            }
        });
    }
    public static void printtop5(List<Map.Entry<String,Double>> unsorted){
        int length = unsorted.size();
        for(int i = 0; i < 5 && i < length; i++){
            Map.Entry<String,Double> map = unsorted.get(i);
            System.out.print(map.getKey() + ", ");
        }
        System.out.println();
    }
}
