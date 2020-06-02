package t;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class MySoap {

    public static void main(String args[]){

        String username = "a";   
        String password = "b";
        String url = "https://192.168.178.1:49443/upnp/control/deviceinfo";
    
        try
        {
            String content = 
                "<soapenv:Envelope soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:h=\"http://soap-authentication.org/digest/2001/10/\" xmlns:u=\"urn:dslforum-org:service:deviceInfo:1\" >" + 
                "\n  <soapenv:Body>" +
                "\n    <u:GetInfo/>" + 
                "\n  </soapenv:Body>" +
                "\n</soapenv:Envelope>";

            // Create the POST object and add the parameters
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
            httpPost.addHeader("SoapAction","urn:dslforum-org:service:deviceInfo:1#GetInfo");

            CloseableHttpClient httpClient = null;
            SSLContextBuilder builder = new SSLContextBuilder(); builder.loadTrustMaterial(null, new TrustSelfSignedStrategy()); 
            //create SSL connection Socket Factory object for trusting self-signed certificates
            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory( builder.build(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslcsf).build();
           
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username,password));
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setCredentialsProvider(credentialsProvider);

            StringEntity input = new StringEntity(content);
            input.setContentType("application/json");
            httpPost.setEntity(input);   
            
            HttpResponse response = httpClient.execute(httpPost,localContext); 

            if (response != null && response.getStatusLine() != null)
            {
                int responseCode = response.getStatusLine().getStatusCode();
                String responseContent = EntityUtils.toString(response.getEntity());

                System.out.println("\n\n-----------------------------");
                System.out.println("\nResponse code: " + responseCode);
                System.out.println("\nResponse content: " + responseContent);
            }

            
        }
        catch (Exception e){
            System.out.println("\nUnexpected Exception: " + e.getMessage());
        }
    }
}