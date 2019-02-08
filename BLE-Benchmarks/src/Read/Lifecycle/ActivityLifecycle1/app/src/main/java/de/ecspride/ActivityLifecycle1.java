package de.ecspride;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @testcase_name ActivityLifecycle1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description The return value of source method is stored to a static variable in one callback method
 *  and sent to a sink in a different callback method
 * @dataflow onCreate: source -> imei -> URL; onResume: URL -> sink
 * @number_of_leaks 1
 * @challenges the analysis must be able to handle the activity lifecycle correctly and
 *  handle try/catch blocks
 */
public class ActivityLifecycle1 extends Activity {
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
	private static byte[] URL = new byte[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_lifecycle1);

		URL = mBluetoothGattCharacteristic.getValue(); //source
    }

    @Override
    protected void onStart(){
    	super.onStart();
    	try{
    		connect();
    	}catch(Exception ex){
    		//do nothing
    	}
    }

	 private void connect() throws IOException{
         SecretKey secret = generateKey("*****");
         byte[] decryptedValue = new byte[0];
         try {
             decryptedValue = decryptMsg(URL, secret);
         } catch (InvalidKeyException e) {
             e.printStackTrace();
         } catch (IllegalBlockSizeException e) {
             e.printStackTrace();
         } catch (BadPaddingException e) {
             e.printStackTrace();
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
    }


    // Both methods taken from here: https://stackoverflow.com/questions/40123319/easy-way-to-encrypt-decrypt-string-in-android
    public static SecretKey generateKey(String password)
    {
        SecretKeySpec secret;
        return secret = new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] decryptMsg(byte[] sourceBytes, SecretKey secret)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] decryptedBytes = cipher.doFinal(sourceBytes); //Sink
        return decryptedBytes;
    }
}
