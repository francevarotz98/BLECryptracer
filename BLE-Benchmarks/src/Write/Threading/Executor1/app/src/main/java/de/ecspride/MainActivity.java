package de.ecspride;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.concurrent.Executors;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @testcase_name Threading_Executor1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description Sensitive data is read in onCreate() and send out in a dedicated thread started
 *   using Java's Executor mechanism.
 * @dataflow onCreate: source -> MyThread.run() -> sink
 * @number_of_leaks 1
 * @challenges The analysis must be able to correctly handle Java's Executor mechanism.
 */
public class MainActivity extends Activity {
	private BluetoothGattCharacteristic mBluetoothGattCharacteristic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        String charValue = "This is a test....";
        String toWrite = charValue.toString();
        SecretKey secret = generateKey("*****");
        byte[] bytesToWrite = new byte[0];
        try {
            bytesToWrite = encryptMsg(toWrite, secret); // bytesToWrite is the source
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		Executors.newCachedThreadPool().execute(new MyRunnable(bytesToWrite));
	}
	
	private class MyRunnable implements Runnable {

		private final byte[] bytesToWrite;
		
		public MyRunnable(byte[] bytesToWrite) {
			this.bytesToWrite = bytesToWrite;
		}

		@Override
		public void run() {
            mBluetoothGattCharacteristic.setValue(bytesToWrite); // sink
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



    // Both methods taken from here: https://stackoverflow.com/questions/40123319/easy-way-to-encrypt-decrypt-string-in-android
    public static SecretKey generateKey(String password)
    {
        SecretKeySpec secret;
        return secret = new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
	/* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = new byte[0];
        try {
            cipherText = cipher.doFinal(message.getBytes("UTF-8")); //Source
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cipherText;
    }

}
