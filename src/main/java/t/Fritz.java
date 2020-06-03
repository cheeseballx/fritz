package t;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.auth.UsernamePasswordCredentials;


public class Fritz {

/*
Class Fritz for interfacing and controlling a frizbox
=====================================================
* Andreas Gladisch
* 02.06.2020
* public should be usabable for cli and as import
*
*
* Functions are available by creating on Fritz object
* then set it up by set functions 
* and in the end fire actions by the make action
*/

    //System
    private static Logger logging;
    public static Level LOGLEVEL = Level.ALL;

    private String username;  //username
    private String password;  //password
    
    private String host;            //host is the address without the http(s) and ends befor the post
    private String host2;           //if the host is not reachable we will define a secon host
    private boolean secondHost;     //check for to use second host
    
    private int port;       //the port that is used
    private boolean tls;    //tls for http and tls for https

    public Fritz(){

        //logging purposes
        logging = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logging.setLevel(LOGLEVEL);
        logging.info("begin logging here");
        logging.info("create Fritz object, filled with std values");

        //set the defaults
        this.username = "admin";
        this.password = "admin";

        this.host = "192.168.178.1";
        this.host2 = "fritz.box";
        this.secondHost = false;

        this.port = 49443;
        this.tls = true;
    }

    public String getAddress(){
        StringBuilder builder = new StringBuilder();

        builder.append( tls ? "https" : "http");        // https
        builder.append( "://" );                        // https://
        builder.append( !secondHost ? host : host2); // https://xxx.xxx.xxx.xxx
        builder.append( ":" );                          // https://xxx.xxx.xxx.xxx:
        builder.append( port );                         // https://xxx.xxx.xxx.xxx:49000
        
        return builder.toString();
    }

    public Answer makeAction(String path, String serviceUrl ,String action){
        
        //load the address
        String url = getAddress() + "/" + path;

        //log everythink
        logging.info("makeAction to:" + url);
        logging.info("service: " + serviceUrl);
        logging.info("action:"  + action);
        logging.info("tls:"+ (this.tls ? "encrypted" : "open") );

        UsernamePasswordCredentials creds = null; 

        //if one parameter is missing send no credentials
        if (this.username == null || this.password == null || 
            this.username.length()<=0 || this.password.length()<=0)
            logging.info("sending NO Creds. sth seems to be null");
        else{
            creds = new UsernamePasswordCredentials(this.username,this.password);
        }

        //doing the call
        Response response =  Soap.call(url, serviceUrl, action,creds,this.tls);
        logging.info("request done");
        
        //serving the response
        if (response.getCode() == 200){
            Answer ans = new Answer(response.getContent());
            ans.getTagValues();
            return ans;
        }

        logging.warning("got no correct answer: "+ response.getCode());
        logging.info(response.getContent());

        return null;
    }
    

//==================== S E T T E R S ================

    //cannot set username on its on, just able to change it together
    public void setCredentials(String username,String password){
        this.username = username;
        this.password = password;

        logging.config("new Credentials inserted");
    }

    //SETS the hosts
    public void setHost(String host){
        logging.config(String.format("change main-host [%s] -> %s",this.host, host));
        this.host = host;
    }
    public void setHost(String host,String host2){
        logging.config(String.format("changing hosts [%s(%s)] -> %s(%s)",this.host,this.host2, host,host2));
        logging.config("uses " + (this.secondHost ? "main" : "second in brakets" ));
        this.host = host;
        this.host2 = host2;
    }
    public void setHost2(String host2){
        logging.config(String.format("change second-host [%s] -> %s",this.host2, host2));
        this.host2 = host2;
    }
    public void useSecondHost(boolean secondHost){
        logging.config(String.format("set using alternatice host (from %b) to %b" + this.secondHost,secondHost ));
        this.secondHost = secondHost;
    }
    
    //Sets the port
    public void setPort(String port){
        logging.config(String.format("change port [%d] -> %s",this.port, port));
        try{
            this.port = Integer.parseInt(port);
        }
        catch(Exception e){
            logging.warning("Port could not be set! \""+port +"\"  is not convertable stay with the old one");
        }
        
    }
    public void setPort(int port){
        logging.config(String.format("change port [%d] -> %d",this.host, host));
        this.port = port;
    }

    //Sets the tls http vs https
    public void setTls(boolean tls){
        logging.config(String.format("change use TLS [%b] -> %b",this.tls, tls));
        this.tls = tls;
    }
}