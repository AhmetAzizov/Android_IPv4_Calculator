package com.ahmetazizov.android_ip_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
//
public class MainActivity2 extends AppCompatActivity {

    TextInputEditText ipAddressTextField;
    TextInputEditText networkAddressTextField;
    TextInputEditText usableHostIpRangeTextField;
    TextInputEditText broadcastAddressTextField;
    TextInputEditText subnetMaskTextField;
    TextInputEditText totalHostsTextField;
    TextInputEditText usableHostsTextField;
    TextInputEditText ipClassTextField;
    TextInputEditText shortFormTextField;
    TextInputEditText ipAddressBinaryTextField;
    TextInputEditText subnetMaskBinaryTextField;
    TextInputEditText networkAddressBinaryTextField;
    TextInputEditText broadcastAddressBinaryTextField;
    MaterialButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ipAddressTextField = findViewById(R.id.ipAddress);
        networkAddressTextField = findViewById(R.id.networkAddress);
        usableHostIpRangeTextField = findViewById(R.id.usableHostIpRange);
        broadcastAddressTextField = findViewById(R.id.broadcastAddress);
        subnetMaskTextField = findViewById(R.id.subnetMask);
        totalHostsTextField = findViewById(R.id.totalHosts);
        usableHostsTextField = findViewById(R.id.usableHosts);
        ipClassTextField = findViewById(R.id.ipClass);
        shortFormTextField = findViewById(R.id.shortForm);
        ipAddressBinaryTextField = findViewById(R.id.ipAddressBinary);
        subnetMaskBinaryTextField = findViewById(R.id.subnetMaskBinary);
        networkAddressBinaryTextField = findViewById(R.id.networkAddressBinary);
        broadcastAddressBinaryTextField = findViewById(R.id.broadcastAddressBinary);
        backButton = findViewById(R.id.backButton);


        int number = Integer.parseInt(getIntent().getStringExtra("numberOfUsers"));
        String netAddress = getIntent().getStringExtra("ipAddress");
        char networkClass;
        int maxCapacityPower = calculation(number);
        double totalHosts = totalHosts(maxCapacityPower, 1);
        double usableHosts = totalHosts(maxCapacityPower, 2);

        String[] netAddressInput = netAddress.split("\\.");

        String octet1 = netAddressInput[0];
        String octet2 = netAddressInput[1];
        String octet3 = netAddressInput[2];
        String octet4 = netAddressInput[3];

        String ipAddressBinary = decimalToBinary(Integer.parseInt(octet1)) + '.' + decimalToBinary(Integer.parseInt(octet2)) + '.' + decimalToBinary(Integer.parseInt(octet3)) + '.' + decimalToBinary(Integer.parseInt(octet4));


        if (Integer.parseInt(octet1) < 128) networkClass = 'A';
        else if (Integer.parseInt(octet1) < 192) networkClass = 'B';
        else if (Integer.parseInt(octet1) < 224) networkClass = 'C';
        else if (Integer.parseInt(octet1) < 240) networkClass = 'D';
        else networkClass = 'E';


        byte[][] subnetMaskArray = subnetMaskGenerator(maxCapacityPower);
        String subnetMask = subnetArrayToString(subnetMaskArray);


        String networkAddress = "";

        for (int i = 0; i < 35; i++) {
            networkAddress += compare(ipAddressBinary.charAt(i), subnetMask.charAt(i));
        }


        StringBuffer broadcastAddress = new StringBuffer(networkAddress);

        int extra = 0;

        if (maxCapacityPower > 8 && maxCapacityPower < 17) extra = 1;
        else if (maxCapacityPower > 16 && maxCapacityPower < 25) extra = 2;
        else if (maxCapacityPower > 24) extra = 3;

        for (int i = 34; i > 34 - maxCapacityPower - extra; i--) {
            if (broadcastAddress.charAt(i) == '.') {
                continue;
            } else {
                broadcastAddress.setCharAt(i, '1');
            }
        }

        String broadCastAddress = String.valueOf(broadcastAddress);


        String[] fullIpAddressDisplay = ipAddressBinary.split("\\.");
        String[] subnetStringDisplay = subnetMask.split("\\.");
        String[] networkAddressDisplay = networkAddress.split("\\.");
        String[] broadcastAddressDisplay = broadCastAddress.split("\\.");

        ipAddressTextField.setText(binaryToDecimal(fullIpAddressDisplay[0]) + "." + binaryToDecimal(fullIpAddressDisplay[1]) + "." + binaryToDecimal(fullIpAddressDisplay[2]) + "." + binaryToDecimal(fullIpAddressDisplay[3]));
        networkAddressTextField.setText(binaryToDecimal(networkAddressDisplay[0]) + "." + binaryToDecimal(networkAddressDisplay[1]) + "." + binaryToDecimal(networkAddressDisplay[2]) + "." + binaryToDecimal(networkAddressDisplay[3]));
        usableHostIpRangeTextField.setText(binaryToDecimal(networkAddressDisplay[0]) + "." + binaryToDecimal(networkAddressDisplay[1]) + "." + binaryToDecimal(networkAddressDisplay[2]) + "." + (binaryToDecimal(networkAddressDisplay[3]) + 1) + "   -  " + binaryToDecimal(broadcastAddressDisplay[0]) + "." + binaryToDecimal(broadcastAddressDisplay[1]) + "." + binaryToDecimal(broadcastAddressDisplay[2]) + "." + (binaryToDecimal(broadcastAddressDisplay[3]) - 1));
        broadcastAddressTextField.setText(binaryToDecimal(broadcastAddressDisplay[0]) + "." + binaryToDecimal(broadcastAddressDisplay[1]) + "." + binaryToDecimal(broadcastAddressDisplay[2]) + "." + binaryToDecimal(broadcastAddressDisplay[3]));
        subnetMaskTextField.setText(binaryToDecimal(subnetStringDisplay[0]) + "." + binaryToDecimal(subnetStringDisplay[1]) + "." + binaryToDecimal(subnetStringDisplay[2]) + "." + binaryToDecimal(subnetStringDisplay[3]));
        totalHostsTextField.setText(String.valueOf((int)(totalHosts)));
        usableHostsTextField.setText(String.valueOf((int)usableHosts));
        ipClassTextField.setText(String.valueOf(networkClass));
        shortFormTextField.setText(netAddress + "/" + (32 - maxCapacityPower));
        ipAddressBinaryTextField.setText(ipAddressBinary);
        subnetMaskBinaryTextField.setText(subnetMask);
        networkAddressBinaryTextField.setText(networkAddress);
        broadcastAddressBinaryTextField.setText(String.valueOf(broadcastAddress));



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }




    void networkAddressCalculation(){

    }

    public static int calculation(int number) {
        int power = 1;
        while(number > Math.pow(2, power) - 2){
            power++;
        }

        return power;
    }



    static double totalHosts(int pow, int choice){
        double result = 0;

        if (choice == 1) {
            result = Math.pow(2, pow);
        }else if(choice == 2){
            result = Math.pow(2, pow) - 2;
        }

        return result;
    }

    public static String decimalToBinary(int number){
        String binary = "";

        for (int i = 128; i > 0; i = i / 2){
            if (number - i >= 0){
                binary += "1";
                number = number - i;
            }else{
                binary += "0";
            }
        }

        return binary;
    }

    public static char compare(char one, char two){
        char three;
        if(one == '1' && two == '1'){
            three = '1';
        }else{
            three = '0';
        }

        if (one == '.'){
            three = '.';
        }

        return three;
    }

    static int[] decimalArray = {128, 64, 32, 16, 8, 4, 2, 1};

    static int binaryToDecimal(String number){
        int decimal = 0;

        for(int i = 0; i < 8; i++){
            if(number.charAt(i) == '1'){
                decimal += decimalArray[i];
            }

            //System.out.println(number.charAt(i));
        }

        return decimal;
    }

    static String subnetArrayToString(byte[][] subnetArray){
        String subnetString = "";

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 8; j++){
                subnetString += String.valueOf(subnetArray[i][j]);
            }
            if (i < 3) subnetString += '.';
        }

        return subnetString;
    }


    static byte[][] subnetMaskGenerator(int maxCapacityPower) {
        byte[][] subnetArray = {{1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1}};

        if (maxCapacityPower > 24) {
            int Octet4 = maxCapacityPower - 24;
            for (int i = 8 - Octet4; i < 8; i++) {
                subnetArray[0][i] = 0;
            }

            for (int i = 1; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    subnetArray[i][j] = 0;
                }
            }
        } else if (maxCapacityPower > 16) {
            int Octet3 = maxCapacityPower - 16;
            for (int i = 8 - Octet3; i < 8; i++) {
                subnetArray[1][i] = 0;
            }

            for (int i = 2; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    subnetArray[i][j] = 0;
                }
            }
        } else if (maxCapacityPower > 8) {
            int Octet2 = maxCapacityPower - 8;
            for (int i = 8 - Octet2; i < 8; i++) {
                subnetArray[2][i] = 0;
            }

            for (int i = 3; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    subnetArray[i][j] = 0;
                }
            }
        } else {
            for (int i = 8 - maxCapacityPower; i < 8; i++) {
                subnetArray[3][i] = 0;
            }
        }

        return subnetArray;
    }




}