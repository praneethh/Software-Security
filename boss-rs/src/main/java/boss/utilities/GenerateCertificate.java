package boss.utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import sun.security.pkcs.PKCS10;
import sun.security.x509.X500Name;




public class GenerateCertificate {
	
	   static {
	        Security.addProvider(new BouncyCastleProvider());
	    }	
	public static void Signature(PrivateKey priv){
		try{
		Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
		dsa.initSign(priv);
		FileInputStream fis = new FileInputStream("data.txt");
		BufferedInputStream bufin = new BufferedInputStream(fis);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bufin.read(buffer)) >= 0) {
		    dsa.update(buffer, 0, len);
		};
		bufin.close();
		byte[] realSig = dsa.sign();
		
		//save the signature in a file
		FileOutputStream sigfos = new FileOutputStream("sig");
		sigfos.write(realSig);
		sigfos.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean Verify( byte[] signedCertificate, byte[] certificate,byte[] publicKey){
		
		try{
//		FileInputStream keyfis = new FileInputStream("DSAPublicKey.key");
//		byte[] encKey = new byte[keyfis.available()];  
//		keyfis.read(encKey);
//		File publicKeyFile = new File ("DSAPublicKey.key");
//		 byte[] encodedKey = new byte[(int)publicKeyFile.length()];
//		    new FileInputStream(publicKeyFile).read(encodedKey);
//		keyfis.close();
		//X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encodedKey);
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
		Signature signed = Signature.getInstance("SHA1withDSA", "SUN");
		KeyFactory keyFactory = KeyFactory.getInstance("DSA","SUN");
		PublicKey pubKey =keyFactory.generatePublic(pubKeySpec);
		
		// get signature
//		FileInputStream sigfis = new FileInputStream("sig");
//		byte[] sigToVerify = new byte[sigfis.available()]; 
//		sigfis.read(sigToVerify);
//		sigfis.close();
		

		
		signed.initVerify(pubKey);
		
		// data to the signature
//		FileInputStream datafis = new FileInputStream("");
//		BufferedInputStream bufin = new BufferedInputStream(datafis);

//		byte[] buffer = new byte[1024];
//		int len;
//		while (bufin.available() != 0) {
//		    len = bufin.read(buffer);
//		    sig.update(buffer, 0, len);
//		};
		signed.update(certificate, 0, certificate.length);
	//	bufin.close();
		
		
		boolean isVerified = signed.verify(signedCertificate);
		return isVerified;
		}
		 catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	 public static byte[] PublicPrivateKeyGeneration(String userName) {
		 byte[] certificate=null;
         try {
          KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
       	  //KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
           SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
           keyGenerator.initialize(512, secRandom);

           KeyPair pubprivpair = keyGenerator.generateKeyPair();
           PrivateKey privateKey = pubprivpair.getPrivate();
           PublicKey publicKey = pubprivpair.getPublic();
         
           certificate = certificateGeneration(publicKey,privateKey,userName);
           
           
           byte[] encPriv = privateKey.getEncoded();
           //File dir= new File("keys");
           File dir= new File(userName); 
           File privFile = new File (dir,"DSAPrivateKey.key");
          // FileOutputStream privfos = new FileOutputStream("C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\keys\\"+userName+"_DSAPrivateKey.key");
           FileOutputStream privfos = new FileOutputStream("./"+userName+"DSAPrivateKey.key");
           //FileOutputStream privfos = new FileOutputStream(privFile);
           privfos.write(encPriv);
           privfos.close();

           byte[] encPub = publicKey.getEncoded();
           //boolean verify = Verify(encPub,sigData,certificate);
           
          // File pubFile = new File (dir,userName+"_DSAPublicKey.key");
           File pubFile = new File (dir,"DSAPublicKey.key");
           FileOutputStream pubfos = new FileOutputStream("./"+userName+"DSAPublicKey.key");
           //FileOutputStream pubfos = new FileOutputStream(pubFile);
           pubfos.write(encPub);
           pubfos.close();
           
           return certificate;

    } catch (Exception e) {
          e.printStackTrace();
          certificate=null;
    }
         return certificate;
   }
	 
	 
	 public static void main (String [] args) {
		 
		 //byte[] cert=PublicPrivateKeyGeneration("rbh");
		 
		 //Verify();
	 }
	 
	 public static byte[] certificateGeneration(PublicKey publicKey, PrivateKey privKey,String username){
		 byte[] certificate = null;
		 try{
			 
			 Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
			    // in 2 years
			    Date validityEndDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);
			 
			    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
			    X500Principal dnName = new X500Principal("CN=John Doe");

			    certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
			    certGen.setSubjectDN(dnName);
			    certGen.setIssuerDN(dnName); // use the same
			    certGen.setNotBefore(validityBeginDate);
			    certGen.setNotAfter(validityEndDate);
			    certGen.setPublicKey(publicKey);
			    certGen.setSignatureAlgorithm("SHA1withDSA");

			   // X509Certificate cert = certGen.generate(privKey, "BC");
			    //certificate=cert.getEncoded();
			    
			    
			 String signatureAlgoithm = "SHA1withDSA";
			 Signature sign = Signature.getInstance(signatureAlgoithm);
			 sign.initSign(privKey);
			 
			 
			    String CN=username;
			    String OU="BOSS";
	     		String O="SS_ASU";
			    String L="TEMPE";
			    String state="Arizona";
			    String country="US";
			 X500Name x500certi = new X500Name(username, OU, O, L, state, country);
			 PKCS10 csr = new PKCS10(publicKey);
			 csr.encodeAndSign(x500certi,sign);
			 certificate = csr.getEncoded();
		 }catch(Exception e){
		
		 }
		 return certificate;
	 }

	

}
