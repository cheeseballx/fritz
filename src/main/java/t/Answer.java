package t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Answer {

    private Map<String, String> map;
    private int code;

    public Answer(String result,int code) {

        this.code = code;

        this.map = new HashMap<>();

        String xmlparts[] = result.split("(?=<)|(?<=>)");
        ArrayList<String> cleand = new ArrayList<>();
        for(String s : xmlparts){
            if ( ! (s.trim().length()<=0) )
                cleand.add(s);
        }

        for (int i=1; i<cleand.size(); i++){
            if ( !(cleand.get(i).contains("<")) && (!cleand.get(i).contains(">"))){
                if (cleand.get(i-1).contains("New") || cleand.get(i-1).contains("Code") || cleand.get(i-1).contains("Description")) {
                    String key = cleand.get(i-1);
                    key = key.replace("<", "").replace(">", "");
                    map.put(key,cleand.get(i));
                }
            }
        }
    }

    public Map<String,String> getTagValues(boolean consoleOut){
        if (consoleOut){
            System.out.println("=================================");
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext();){
                String key = it.next();
                System.out.println(key + " --- " + map.get(key));
            }
        }
        return this.map;
    }

    public int getCode(){
        return this.code;
    }
}