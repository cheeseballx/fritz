package t;

import java.util.logging.Logger;

import javax.sound.midi.Track;
import org.apache.http.HttpResponse;

public class Fritz {

/*
Class Fritz for interfacing and controlling a frizbox
=====================================================
* Andreas Gladisch
* 02.06.2020
* public should be usabable for cli and as import
*/

    //System
    Logger logging;

    private String username;  //username
    private String password;  //password
    
    private String host;            //host is the address without the http(s) and ends befor the post
    private String host2;           //if the host is not reachable we will define a secon host
    private boolean useSecondHost;  //check for to use second host
    
    private int port;       //the port that is used
    private boolean tls;    //tls for http and tls for https

    public Fritz(){

        //logging purposes
        logging = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        //set the defaults
        this.username = "admin";
        this.password = "admin";

        this.host = "192.168.178.1";
        this.host2 = "fritz.box";
        this.useSecondHost = false;

        this.port = 49443;
        this.tls = true;
    }

    public String getAddress(){
        StringBuilder builder = new StringBuilder();

        builder.append( tls ? "https" : "http");        // https
        builder.append( "://" );                        // https://
        builder.append( !useSecondHost ? host : host2); // https://xxx.xxx.xxx.xxx
        builder.append( ":" );                          // https://xxx.xxx.xxx.xxx:
        builder.append( port );                         // https://xxx.xxx.xxx.xxx:49000
        
        return builder.toString();
    }

    public boolean makeAction(String path, String serviceUrl ,String action){
        
        String url = getAddress() + "/" + path;

        logging.info("makeAction to:" + url);
        logging.info("service: " + serviceUrl);
        logging.info("action:"  + action);

        Response response =  Soap.call(url, serviceUrl, action);

        if (response.getCode() == 200)
            return true;

        return false;
    }
    
    

    
}