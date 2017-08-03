package usagitoneko.nekof.controller;

import android.nfc.tech.NfcV;

import java.io.IOException;

/**
 * Created by DareBacon on 3/8/2017.
 */

public class Stm32BasicIO {
    private NfcV nfcV;
    public Stm32BasicIO(NfcV nfcv) {
        this.nfcV = nfcv;
        try {
            this.nfcV.connect();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * convert primitive bytes to Byte[] object
     * @param bytesPrim primitive bytes to be converted
     * @return object of Bytes[]
     */
    static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];

        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; // Autoboxing

        return bytes;
    }

    /**
     * This method will send dataBytes to the memory of eeprom in stm32
     * @param dataBytes the data that send to the eeprom of stm32 in multiple of 4 bytes
     * @param blocks the length of dataBytes in multiple of 4
     * @param memLoc the starting location of dataBytes, in multiple of 4
     */
    public void sendDataBytesToEeprom(byte[] dataBytes, byte blocks, byte memLoc){  //FIXME implement memloc and numofBytes
        try {
            nfcV.transceive(new byte[]{0x02, 0x21, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * read data in eeprom of stm32 in location memloc and numofBytes of bytes
     * @param memLoc the starting location of bytes read
     * @param numOfBytes the number of bytes read in multiple of 4
     * @return the data from eeprom in location memLoc
     */
    public byte[] receiveDataFromEeprom(int memLoc, int numOfBytes){    //FIXME implement memloc and numofBytes
        byte[] result = new byte[4];
        try {
            result = nfcV.transceive(new byte[]{0x02, 0x21, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});

        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * send android confirmation flag to stm32 to notify stm32
     */
    public void sendAndroidConfirmation(){
        try {
            nfcV.transceive(new byte[]{0x02, 0x21, (byte) 0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});  //FIXME

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
