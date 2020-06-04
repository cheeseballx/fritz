package t;

public class Start {

    public static void main(String args[]){

        Fritz fritzi = new Fritz();
        fritzi.setCredentials("admin",null);

        //String path = "upnp/control/deviceinfo";
        //String serviceUrl = "urn:dslforum-org:service:deviceInfo:1";
        //String action = "GetInfo";

        fritzi.showActions("http://192.168.178.1:49000/tr64desc.xml");

        String path = "upnp/control/hosts";
        String serviceUrl = "urn:dslforum-org:service:Hosts:1";
        String action = "GetHostNumberOfEntries";

        fritzi.makeAction(path, serviceUrl, action);

    }
    
}