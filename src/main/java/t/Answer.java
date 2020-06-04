package t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Answer {

    private Map<String, String> map;

    public Answer(String result) {

        this.map = new HashMap<>();

        String xmlparts[] = result.split("(?=<)|(?<=>)");
        ArrayList<String> cleand = new ArrayList<>();
        for(String s : xmlparts){
            if ( ! (s.trim().length()<=0) )
                cleand.add(s);
        }

        //System.out.println(Arrays.toString(cleand.toArray()));
        for (int i=1; i<cleand.size(); i++){
            if (cleand.get(i-1).contains("New") && !(cleand.get(i).contains("<")) && (!cleand.get(i).contains(">"))){

                String key = cleand.get(i-1);
                key = key.replace("<", "").replace(">", "");
                map.put(key,cleand.get(i));
            }
        }
    }

    public Map<String,String> getTagValues(){

        System.out.println("=================================");
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext();){
            String key = it.next();
            System.out.println(key + " --- " + map.get(key));
        }
        
        return this.map;
    }
}