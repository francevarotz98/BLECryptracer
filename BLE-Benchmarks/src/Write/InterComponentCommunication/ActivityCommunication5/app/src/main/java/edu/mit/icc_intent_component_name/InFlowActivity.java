package edu.mit.icc_intent_component_name;

import edu.mit.icc_intent_component_name.R;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class InFlowActivity extends Activity {
	private BluetoothGattCharacteristic mBluetoothGattCharacteristic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent i = getIntent();
		byte[] bytesToWrite = i.getByteArrayExtra("DroidBench");
        mBluetoothGattCharacteristic.setValue(bytesToWrite);
	}

}
