package com.client;

import org.springframework.web.bind.annotation.RestController;
import java.io.*;

import java.security.interfaces.RSAPublicKey;
import java.security.KeyStore;
import java.security.cert.Certificate;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;

@RestController
public class AppRestVerifier {

    public boolean validateJWTSignature(String jwt)
    {
        try{
            RSAPublicKey publicKey = null;
            String keyfile  = "keystore.jks";
            String storepass = "changeit";
            String alias = "oidc-app";

            InputStream file = this.getClass().getClassLoader().getResourceAsStream(keyfile);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(file, storepass.toCharArray());
    
            // Get certificate of public key
            Certificate cert = keystore.getCertificate(alias);
            
            // Get public key
            publicKey = (RSAPublicKey) cert.getPublicKey();
            
            // Received JWT
            String signedJWTAsString = jwt;
    
            SignedJWT signedJWT = SignedJWT.parse(signedJWTAsString);
    
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
    
            return signedJWT.verify(verifier);
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return false;
    }
}