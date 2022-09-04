package seleniumuTest;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.virtualauthenticator.Credential;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions.Protocol;

public class test {
	public static void main(String args[]) throws Exception {

		final String rpId = "WebAuthn.org";
		byte[] credentialId = "LpnpDmIs9nuOSsjN9ZX-qYOGcXE9v6idWjUf4dnIgmM".getBytes();
		// byte[] credentialId = { 1, 2, 3, 4 };

		byte[] userHandle = { 1 };

		//final String base64EncodedPK
		 //="MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgwVpwwk38kOg7qv9"
		 //+ "925acFRzCNxawslfcfzTJXwMNpyhRANCAAQbk+RmbbiLue9KuKQZ9P7upeYjwnM2"
		 //+ "WiT6PYebq+m5EZGxdOvLabuhoAW9LF63vMqHYJfjat2RSbDR4/ynwL8s";

		 //seleniumu test
		 final String base64EncodedPK =
		 "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg8_zMDQDYAxlU-Q"
		 + "hk1Dwkf0v18GZca1DMF3SaJ9HPdmShRANCAASNYX5lyVCOZLzFZzrIKmeZ2jwU"
		 + "RmgsJYxGP__fWN_S-j5sN4tT15XEpN_7QZnt14YvI6uvAgO0uJEboFaZlOEB";

		PKCS8EncodedKeySpec privateKey = getPemPrivateKey("pk.pem");
		
		// ChromeDriverのパスを設定
		System.setProperty("webdriver.chrome.driver", "chromDriver/chromedriver");

		// WebDriverのインスタンスを作成
		WebDriver driver = new ChromeDriver();

		VirtualAuthenticatorOptions options = new VirtualAuthenticatorOptions().setProtocol(Protocol.CTAP2)
				.setHasResidentKey(true).setHasUserVerification(true).setIsUserVerified(true);
		VirtualAuthenticator authenticator = ((HasVirtualAuthenticator) driver).addVirtualAuthenticator(options);

		Credential residentCredential = Credential.createResidentCredential(credentialId, rpId, privateKey, userHandle,
				/* signCount= */0);

		Credential nonResidentCredential = Credential.createNonResidentCredential(credentialId, rpId, privateKey,
				/* signCount= */0);

		authenticator.addCredential(nonResidentCredential);
	}

	static PKCS8EncodedKeySpec getPemPrivateKey(String filename) throws Exception {
		File f = new File(filename);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		byte[] keyBytes = new byte[(int) f.length()];
		dis.readFully(keyBytes);
		dis.close();

		String temp = new String(keyBytes);
		String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
		privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
		System.out.println("Private key\n" + privKeyPEM);

		byte[] decoded = Base64.getMimeDecoder().decode(privKeyPEM);
		return new PKCS8EncodedKeySpec(decoded);
	}

}