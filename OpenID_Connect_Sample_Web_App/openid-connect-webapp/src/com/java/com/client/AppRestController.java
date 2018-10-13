package com.client;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.RedirectView;
import org.apache.commons.codec.binary.Base64;
import java.io.*;
import org.json.*;
import java.net.*;

@RestController
public class AppRestController {

    String id_token = null;
    String nickname = null;
    String picture = null;
    String msgVerify = "JWT Not Verified Yet";

    @RequestMapping(value = "/login-auth0", method=RequestMethod.GET)
    public RedirectView processForm1() {
        RedirectView redirectView = new RedirectView();

        String url = "https://chathu.auth.miniorange.com/authorize"+
                    "?audience=https://chathu.auth.miniorange.com/api/v2/"+
                    "&scope=openid%20profile"+
                    "&response_type=code"+
                    "&client_id=DpeGsXwMZuainG5XzDs1tRDmcdnbu86n"+
                    "&redirect_uri=http%3A%2F%2F192.168.30.149%3A9999%2Foauth%2Faccess"+
                    "&state=abc";
        redirectView.setUrl(url);
        return redirectView;
    }
    
   
    @RequestMapping(value = "/auth/access", method = RequestMethod.GET)
    public RedirectView authUser(ModelMap model, @RequestParam(value = "code",required=true) String authCode) {
        
        try{
             String response = getAuthResponse(authCode);

            
            JSONObject jsonBody = getUserData(response);
            this.othername = jsonBody.getString("OtherNAmes");
            this.picture = jsonBody.getString("picture");

            return viewHomePage();
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    @RequestMapping("/verify")
    public RedirectView verify() {
        AppRestVerifier app = new AppRestVerifier();
        if(app.validateJWTSignature(this.id_token))
            this.msgVerify = "JWT Verified";
        else
            this.msgVerify = "JWT Verification Failed";
        return viewHomePage();
    }

    public String getAuthResponse(String authCode)
    {
      
        String auth_url = "https://chathu.auth.miniorange.com/oauth/token";
        
       
        String POST_PARAMS = "grant_type=authorization_code"+
                            "&client_id=DpeGsXwMZuainG5XzDs1tRDmcdnbu86n"+
                            "&client_secret=qDdonREau1SU6nXFZiTfoCw9-6cRZv3gqcv73Wg8qDtbmlpIWN9TMkJtHwJ-1Ro0"+
                            "&code="+authCode+
                            "&redirect_uri=http%3A%2F%2F192.168.30.149%3A9999%2Foauth%2Faccess";
        String authReponse = "";

        try
        {
            URL obj = new URL(auth_url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

         
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");

          
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(POST_PARAMS.getBytes());
            os.flush();
            os.close();


            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();

                authReponse = response.toString();

            }
            else
            {
                System.out.println("Error : " + responseCode);
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        
        
        return authReponse;
    }

    public JSONObject getUserData(String response)
    {
        JSONObject bodyJson = null;
        try{
            
            JSONObject jsonObj = new JSONObject(response);
            this.id_token = jsonObj.getString("id_token");
            
           
            String[] arr_spliit = id_token.split("\\.");
            String headEnc = arr_spliit[0];
            String bodyEnc = arr_spliit[1];
            String sigEnc = arr_spliit[2];

            Base64 base64Url = new Base64(true);
            String body = new String(base64Url.decode(bodyEnc));

            bodyJson = new JSONObject(body);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return bodyJson;
    }

    public RedirectView viewHomePage(){
        AppController app = new AppController();
        app.setModelAttribute("nickname",this.nickname);
        app.setModelAttribute("picture",this.picture);
        app.setModelAttribute("verify_status",this.msgVerify);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return redirectView;
    }
}