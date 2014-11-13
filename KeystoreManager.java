import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class KeystoreManager {
    //public String keystoreFile = "keyStoreFile.bin";
  public static void main(String[] args) throws Exception {

        createKeystore("keyStore.bin", "unicorn");
        addCertificate("keyStore.bin","tami", "lolo");
  }  

  KeystoreManager ()
  {

  }  

    public static void createKeystore(String file, String pass) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException
    {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        char[] password = pass.toCharArray();
        ks.load(null, password);

        // Store away the keystore.
        FileOutputStream fos = new FileOutputStream(file);
        ks.store(fos, password);
        fos.close();
    }

    public static void addCertificate(String ksFile, String userID, String pass) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, InvalidKeyException, NoSuchProviderException, SignatureException
    {

        char[] keyStorePassword = "unicorn".toCharArray();
        char[] password = pass.toCharArray();

        FileInputStream input = new FileInputStream(ksFile);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(input, keyStorePassword);
        input.close();

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(userID, password);
        java.security.cert.Certificate caCert = keyStore.getCertificate(userID);

        byte[] encoded = caCert.getEncoded();
        X509CertImpl caCertImpl = new X509CertImpl(encoded);

        X509CertInfo caCertInfo = (X509CertInfo) caCertImpl.get(X509CertImpl.NAME + "."
            + X509CertImpl.INFO);

        X500Name issuer = (X500Name) caCertInfo.get(X509CertInfo.SUBJECT + "."
            + CertificateIssuerName.DN_NAME);

        java.security.cert.Certificate cert = keyStore.getCertificate(userID);
        encoded = cert.getEncoded();
        X509CertImpl certImpl = new X509CertImpl(encoded);
        X509CertInfo certInfo = (X509CertInfo) certImpl
            .get(X509CertImpl.NAME + "." + X509CertImpl.INFO);

        Date firstDate = new Date();
        Date lastDate = new Date(firstDate.getTime() + 365 * 24 * 60 * 60 * 1000L);
        CertificateValidity interval = new CertificateValidity(firstDate, lastDate);

        certInfo.set(X509CertInfo.VALIDITY, interval);

        certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(
            (int) (firstDate.getTime() / 1000)));

        certInfo.set(X509CertInfo.ISSUER + "." + CertificateSubjectName.DN_NAME, issuer);

        AlgorithmId algorithm = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        certInfo.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algorithm);
        X509CertImpl newCert = new X509CertImpl(certInfo);

        newCert.sign(privateKey, "MD5WithRSA");

        keyStore.setKeyEntry(userID, privateKey, password,
            new java.security.cert.Certificate[] { newCert });

        FileOutputStream output = new FileOutputStream(ksFile);
        keyStore.store(output, password);
        output.close();
    }
}