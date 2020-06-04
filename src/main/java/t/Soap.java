package t;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.util.Map;


public class Soap {

    public static Response call(String url, String serviceUrl, String action, Map<String,String> param, UsernamePasswordCredentials credentials,boolean tls) {

        String content = String.format(
                "    <soapenv:Envelope soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\""
                +"          xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
                +"          xmlns:u=\"%s\" >"
                + "\n  <soapenv:Body>" 
                + "\n    <u:%s />" 
                + "\n  </soapenv:Body>" 
                + "\n</soapenv:Envelope>", serviceUrl, action);

        // Create the POST object and add the Header
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
        String soapAction = String.format("%s#%s", serviceUrl, action);
        httpPost.addHeader("SoapAction", soapAction);

        // set the body
        StringEntity input = null;
        try {
            input = new StringEntity(content);
        } catch (UnsupportedEncodingException e1) {
            System.out.println("error" +e1.getMessage());
            e1.printStackTrace();
        }
        input.setContentType("application/json");
        httpPost.setEntity(input);

        // http client
        CloseableHttpClient httpClient;

        // if ssl is true
        if (tls) {
            SSLContextBuilder builder = new SSLContextBuilder();
            SSLConnectionSocketFactory sslcsf = null;
            try {
                builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                sslcsf = new SSLConnectionSocketFactory(builder.build(), new NoopHostnameVerifier());
            } catch (NoSuchAlgorithmException e) {
                System.out.println("error2" +e.getMessage());
                e.printStackTrace();
            } catch (KeyStoreException e) {
                System.out.println("error3" +e.getMessage());
                e.printStackTrace();
            } catch (KeyManagementException e) {
                System.out.println("error4" +e.getMessage());
                e.printStackTrace();
            }
            httpClient = HttpClients.custom().setSSLSocketFactory(sslcsf).build();
        }
        // if no ssl
        else {
            httpClient = HttpClients.createDefault();
        }

        HttpClientContext localContext = null;

        // if we have credentils
        if (credentials != null) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            localContext = HttpClientContext.create();
            localContext.setCredentialsProvider(credentialsProvider);
        }

        HttpResponse response = null;
        try {
            if (credentials != null)
                response = httpClient.execute(httpPost, localContext);
            else
                response = httpClient.execute(httpPost);
        } catch (Exception e) {
            // todo error
            System.out.println("todo error");
        }


        if (response != null && response.getStatusLine() != null) {
            int responseCode  = 0; 
            String responseContent = null;

            try {
                responseCode = response.getStatusLine().getStatusCode();
                responseContent = EntityUtils.toString(response.getEntity());
            } catch (ParseException e) {
                System.out.println("error10" +e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("error11" +e.getMessage());
                e.printStackTrace();
            }

            return new Response(responseCode,responseContent);
        }

        return new Response(0,null);

    }
}