package net.xprinter.example4wifi.Utils;

import java.io.UnsupportedEncodingException;

public class XprinterCodeUtils {
    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte DLE = 16;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte CR = 13;
    public static final byte FF = 12;
    public static final byte CAN = 24;

    /**
     * BarCode table
     */
    public static class BarCode {
        public static final byte UPC_A = 0;
        public static final byte UPC_E = 1;
        public static final byte EAN13 = 2;
        public static final byte EAN8 = 3;
        public static final byte CODE39 = 4;
        public static final byte ITF = 5;
        public static final byte NW7 = 6;
        public static final byte CODE93 = 72;
        public static final byte CODE128 = 73;
    }


    /**
     * 1、HT 水平定位
     * <p>
     * [描述]
     * 移动打印位置到下一个水平定位点的位置。
     */
    public static String horizontal_positioning() {
        return toString(HT);
    }

    /**
     * 60、①GS k m d1...dk NUL②GS k m n d1...dn 打印条码
     * <p>
     * [范围]
     * ①0 ≤ m ≤ 6 （k 和d 的取值范围是由条码类型来决定）
     * ②65 ≤ m ≤ 73 （k 和 d 的取值范围是由条码类型来决定）
     * [描述] 选择一种条码类型并打印条码。
     */
    public static String printBarCode(int barcodeType, String barcodeString) {
        byte[] barcodebytes = barcodeString.getBytes();
        byte[] result = new byte[3 + barcodebytes.length + 1];
//        byte[] result = new byte[3+barcodebytes.length];
        result[0] = GS;
        result[1] = 107;
        result[2] = (byte) barcodeType;
        int idx = 3;
        for (int i = 0; i < barcodebytes.length; i++) {
            result[idx] = barcodebytes[i];
            idx++;
        }
        result[idx] = 0;
        return toString(result);
    }

    public static String toString(byte... bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
