import java.util.*;

/**
 * Created by shahks on 4/3/2016.
 * This class is responsible for entire density analysis
 */
public class DensityAnalysis {
    private final String inpString;
    DensityAnalysis(String inpString){
        this.inpString = inpString;
    }
    void TFanalyze1word (HashMap<String, Integer> map2words){
        String delim = " ";
        StringTokenizer stringTokenizer = new StringTokenizer(inpString,delim);
        //HashMap<String, Integer> map2words = new HashMap<String, Integer>();
        String first = "", entry;
        try{
            while(stringTokenizer.hasMoreElements()){
                first = stringTokenizer.nextElement().toString();
                entry = first;
                if(map2words.containsKey(entry)){
                    int count = map2words.get(entry);
                    map2words.put(entry,++count);
                }
                else{
                    map2words.put(entry,1);
                }
            }
            /*
            List<Map.Entry<String,Integer>> unsorted = new ArrayList<Map.Entry<String, Integer>>(map2words.entrySet());
            sort(unsorted);
            */
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void TFanalyze2words (HashMap<String, Integer> map2words){
        String delim = " ";
        StringTokenizer stringTokenizer = new StringTokenizer(inpString,delim);
        //HashMap<String, Integer> map2words = new HashMap<String, Integer>();
        String first = "", second, entry;
        try{
            if(stringTokenizer.hasMoreElements()) {
                first = stringTokenizer.nextElement().toString();
            }
            while(stringTokenizer.hasMoreElements()){
                second = stringTokenizer.nextElement().toString();
                entry = first + " " + second;
                if(map2words.containsKey(entry)){
                    int count = map2words.get(entry);
                    map2words.put(entry,++count);
                }
                else{
                    map2words.put(entry,1);
                }
                first = second;
            }
            /*
            List<Map.Entry<String,Integer>> unsorted = new ArrayList<Map.Entry<String, Integer>>(map2words.entrySet());
            sort(unsorted);
            */
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void TFanalyze3words (HashMap<String, Integer> map2words){
        String delim = " ";
        StringTokenizer stringTokenizer = new StringTokenizer(inpString,delim);
        //HashMap<String, Integer> map2words = new HashMap<String, Integer>();
        String first = "", second = "", third, entry;
        try{
            if(stringTokenizer.hasMoreElements()) {
                first = stringTokenizer.nextElement().toString();
            }
            if(stringTokenizer.hasMoreElements()) {
                second = stringTokenizer.nextElement().toString();
            }
            while(stringTokenizer.hasMoreElements()){
                third = stringTokenizer.nextElement().toString();
                entry = first + " " + second + " " + third;
                if(map2words.containsKey(entry)){
                    int count = map2words.get(entry);
                    map2words.put(entry,++count);
                }
                else{
                    map2words.put(entry,1);
                }
                first = second;
                second = third;
            }
            /*
            List<Map.Entry<String,Integer>> unsorted = new ArrayList<Map.Entry<String, Integer>>(map2words.entrySet());
            sort(unsorted);
            */
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void IDFanalyze1word (HashMap<String, Integer> map2words){
        String delim = " ";
        StringTokenizer stringTokenizer = new StringTokenizer(inpString,delim);
        //HashMap<String, Integer> map2words = new HashMap<String, Integer>();
        String first = "", entry;
        try{
            while(stringTokenizer.hasMoreElements()){
                first = stringTokenizer.nextElement().toString();
                entry = first;
                if(map2words.containsKey(entry)){
                    int count = map2words.get(entry);
                    map2words.put(entry,++count);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void IDFanalyze2words (HashMap<String, Integer> map2words){
        String delim = " ";
        StringTokenizer stringTokenizer = new StringTokenizer(inpString,delim);
        //HashMap<String, Integer> map2words = new HashMap<String, Integer>();
        String first = "", second, entry;
        try{
            if(stringTokenizer.hasMoreElements()) {
                first = stringTokenizer.nextElement().toString();
            }
            while(stringTokenizer.hasMoreElements()){
                second = stringTokenizer.nextElement().toString();
                entry = first + " " + second;
                if(map2words.containsKey(entry)){
                    int count = map2words.get(entry);
                    map2words.put(entry,++count);
                }
                first = second;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void IDFanalyze3words (HashMap<String, Integer> map2words){
        String delim = " ";
        StringTokenizer stringTokenizer = new StringTokenizer(inpString,delim);
        //HashMap<String, Integer> map2words = new HashMap<String, Integer>();
        String first = "", second = "", third, entry;
        try{
            if(stringTokenizer.hasMoreElements()) {
                first = stringTokenizer.nextElement().toString();
            }
            if(stringTokenizer.hasMoreElements()) {
                second = stringTokenizer.nextElement().toString();
            }
            while(stringTokenizer.hasMoreElements()){
                third = stringTokenizer.nextElement().toString();
                entry = first + " " + second + " " + third;
                if(map2words.containsKey(entry)){
                    int count = map2words.get(entry);
                    map2words.put(entry,++count);
                }
                first = second;
                second = third;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    void sort(List<Map.Entry<String,Integer>> unsorted){
        unsorted.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int first = o1.getValue().intValue();
                int second = o2.getValue().intValue();
                if(first == second)
                    return 0;
                else
                    return (first < second)? 1 : -1;
            }
        });

        /*for(Map.Entry<String,Integer> map : unsorted){
            System.out.println(map.getKey() + " : " + map.getValue());
        }*/
    }
}
