package fritzchen;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class FritzTest {

    @Test public void mainInterfaceTesting() {
        
        Fritz fritzi = new Fritz();
        fritzi.setCredentials("admin",null);

        //String path = "upnp/control/deviceinfo";
        //String serviceUrl = "urn:dslforum-org:service:deviceInfo:1";
        //String action = "GetInfo";

        //fritzi.showActions("http://192.168.178.1:49000/tr64desc.xml","service");

        String path = "upnp/control/hosts";
        String serviceUrl = "urn:dslforum-org:service:Hosts:1";
        String action = "GetSpecificHostEntry";
        Map<String,String> param = new HashMap();
        param.put("NewMACAddress", "AA:AA:AA:AA:AA:AA");

        Answer ans = fritzi.makeAction(path, serviceUrl, action,param);
        ans.getTagValues(true);

        assertTrue("could not find this device sp 500",ans.getCode() == 500);
        assertTrue("error message needs to be not found 714",ans.getTagValues(false).get("errorCode").equals("714"));

        
    }   
}