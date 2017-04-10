package usagitoneko.nekof.controller;

/**
 * Created by user on 4/9/2017.
 */

public class WifiSSidPW {
   public final static String SSID1 ="Q9jv8Tx01mlZ";
   public final static String SSID2 ="1pb7YH30AcXI";
   public final static String SSID3 ="8uYXn27OqsG7";
   public final static String SSID4 ="1Wdv&yGo0LeU";
   public final static String SSID5 ="t8U4vnJFa50Q";
   public final static String SSID6 ="Ec91Q0Vb317I";
   public final static String SSID7 ="0UZP3eh6aY2j";
   public final static String SSID8 ="s82UhEN51Wha";
   public final static String SSID9 ="4Opa3rg45nf2";
   public final static String SSID10="2i97g0HIb86v";

   public final static String PASS1 ="6FV0bu9V";
   public final static String PASS2 ="76dc09hu";
   public final static String PASS3 ="V68f56T0";
   public final static String PASS4 ="9UjoG5d2";
   public final static String PASS5 ="seRCrte4";
   public final static String PASS6 ="wTCVih0R";
   public final static String PASS7 ="7G7YcHYc";
   public final static String PASS8 ="4SWsxc9I";
   public final static String PASS9 ="n9kojbT5";
   public final static String PASS10="FVuv86F4";

    private int hexNUM;
    public WifiSSidPW(int hexNUM) {
        this.hexNUM = hexNUM;
    }

    public  String getSSID(){
        switch (hexNUM){
            case 0x30:
                return SSID1;
            case 0x31:
                return SSID2;

            case 0x32:
                return SSID3;

            case 0x33:
                return SSID4;

            case 0x34:
                return SSID5;

            case 0x35:
                return SSID6;

            case 0x36:
                return SSID7;

            case 0x37:
                return SSID8;

            case 0x38:
                return SSID9;

            case 0x39:
                return SSID10;
        }
        return null;

    }

    public  String getWifiPassWord(){
        switch (hexNUM){
            case 0x30:
                return PASS1;
            case 0x31:
                return PASS2;

            case 0x32:
                return PASS3;

            case 0x33:
                return PASS4;

            case 0x34:
                return PASS5;

            case 0x35:
                return PASS6;

            case 0x36:
                return PASS7;

            case 0x37:
                return PASS8;

            case 0x38:
                return PASS9;

            case 0x39:
                return PASS10;
        }
        return null;
    }
}
