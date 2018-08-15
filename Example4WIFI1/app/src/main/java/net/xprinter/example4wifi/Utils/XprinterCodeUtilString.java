package net.xprinter.example4wifi.Utils;

import java.util.Arrays;
import java.util.List;

public class XprinterCodeUtilString {

    /**
     * 2、 打印并换行
     * LF
     *
     * @return bytes for this command
     */
    public static String printLinefeed() {
        return "$ESC_10";
    }

    /**
     * 15、ESC – n 选择/取消下划线模式
     * Turn underline mode on, set at 1-dot width
     * ESC - n
     *
     * @return bytes for this command
     */
    public static String underline1DotOn() {
        return "$ESC_27_45_1";
    }

    /**
     * 15、ESC – n 选择/取消下划线模式
     * Turn underline mode on, set at 2-dot width
     * ESC - n
     *
     * @return bytes for this command
     */
    public static String underline2DotOn() {
        return "$ESC_27_45_2";
    }

    /**
     * 15、ESC – n 选择/取消下划线模式
     * Turn underline mode off
     * ESC - n
     *
     * @return bytes for this command
     */
    public static String underlineOff() {
        return "$ESC_27_45_0";
    }


    /**
     * 20、ESC @ 初始化打印机
     * Initialize printer
     * Clears the data in the print buffer and resets the printer modes to the modes that were
     * in effect when the power was turned on.
     * ESC @
     *
     * @return bytes for this command
     */
    public static String initPrinter() {
        return "$ESC_27_64";
    }

    /**
     * 22、ESC E n 选择/取消加粗模式
     * Turn emphasized mode on
     * ESC E n
     *
     * @return bytes for this command
     */
    public static String emphasizedOn() {
        return "$ESC_27_69_1";
    }

    /**
     * 22、ESC E n 选择/取消加粗模式
     * Turn emphasized mode off
     * ESC E n
     *
     * @return bytes for this command
     */
    public static String emphasizedOff() {
        return "$ESC_27_69_0";
    }

    /**
     * 23、ESC G n 选择/取消双重打印模式
     * double_strike_on
     * ESC G n
     *
     * @return bytes for this command
     */
    public static String doubleStrikeOn() {
        return "$ESC_27_71_1";
    }

    /**
     * 23、ESC G n 选择/取消双重打印模式
     * double_strike_off
     * ESC G n
     *
     * @return bytes for this command
     */
    public static String doubleStrikeOff() {
        return "$ESC_27_71_0";
    }

    /**
     * 26、ESC M n 选择字体
     * Select Font A
     * ESC M n
     *
     * @return bytes for this command
     */
    public static String selectFontA() {
        return "$ESC_27_77_0";
    }

    /**
     * 26、ESC M n 选择字体
     * Select Font B
     * ESC M n
     *
     * @return bytes for this command
     */
    public static String selectFontB() {
        return "$ESC_27_77_1";
    }

    /**
     * 26、ESC M n 选择字体
     * Select Font C ( some printers don't have font C )
     * ESC M n
     *
     * @return bytes for this command
     */
    public static String selectFontC() {
        return "$ESC_27_77_2";
    }

    /**
     * 10、ESC ! n 选择打印模式
     * double height width mode on Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
//    public static String doubleHeightWidthOn()
//    {
//        return "$ESC_27_33_56";
//    }
    public static List<String> doubleHeightWidthOn() {
        return Arrays.asList("$ESC_27_33_56", "$ESC_28_33_12");
    }

    /**
     * 10、ESC ! n 选择打印模式
     * double height width mode off Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static List<String> doubleHeightWidthOff() {
        return Arrays.asList("$ESC_27_33_0", "$ESC_28_33_0");
    }

    /**
     * 10、ESC ! n 选择打印模式
     * Select double height mode Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static String doubleHeightOn() {
        return "$ESC_27_33_16";
    }

    /**
     * 10、ESC ! n 选择打印模式
     * disable double height mode, select Font A
     * ESC ! n
     *
     * @return bytes for this command
     */
    public static String doubleHeightOff() {
        return "$ESC_27_33_0";
    }

    /**
     * 33、ESC a n 选择对齐方式
     * justification_left
     * ESC a n
     *
     * @return bytes for this command
     */
    public static String justificationLeft() {
        return "$ESC_27_97_0";
    }

    /**
     * 33、ESC a n 选择对齐方式
     * justification_center
     * ESC a n
     *
     * @return bytes for this command
     */
    public static String justificationCenter() {
        return "$ESC_27_97_1";
    }

    /**
     * 33、ESC a n 选择对齐方式
     * justification_right
     * ESC a n
     *
     * @return bytes for this command
     */
    public static String justificationRight() {
        return "$ESC_27_97_2";
    }

    /**
     * 37、ESC d n 打印并向前走纸 n 行
     * Print and feed n lines
     * Prints the data in the print buffer and feeds n lines
     * ESC d n
     *
     * @param n lines
     * @return bytes for this command
     */
    public static String printAndFeedLines(byte n) {
        return "$ESC_27_100_" + String.valueOf(n);
    }

    /**
     * 22、ESC E n 选择/取消加粗模式
     * Print and reverse feed n lines
     * Prints the data in the print buffer and feeds n lines in the reserve direction
     * ESC e n
     *
     * @param n lines
     * @return bytes for this command
     */
    public static String printAndReverseFeedLines(byte n) {
        return "$ESC_27_101_" + String.valueOf(n);
    }

    /**
     * 38、ESC p m t1 t2 产生钱箱控制脉冲
     * Drawer Kick
     * Drawer kick-out connector pin 2
     * ESC p m t1 t2
     *
     * @return bytes for this command
     */
    public static String drawerKick() {
        return "$ESC_27_112_0_60_120";
    }

    /**
     * 27、ESC R n 选择国际字符集
     * Select printing color1
     * ESC r n
     *
     * @return bytes for this command
     */
    public static String selectColor1() {
        return "$ESC_27_114_0";
    }

    /**
     * 27、ESC R n 选择国际字符集
     * Select printing color2
     * ESC r n
     *
     * @return bytes for this command
     */
    public static String selectColor2() {
        return "$ESC_27_114_1";
    }

    /**
     * 29、ESC T n 在页模式下选择打印区域方向
     * Select character code table
     * ESC t n
     *
     * @param cp example:CodePage.WPC1252
     * @return bytes for this command
     */
    public static String selectCodeTab(byte cp) {
        return "$ESC_27_116_" + String.valueOf(cp);
    }

    /**
     * 49、GS B n 选择 / 取消黑白反显打印模式
     * white printing mode on
     * Turn white/black reverse printing mode on
     * GS B n
     *
     * @return bytes for this command
     */
    public static String whitePrintingOn() {
        return "$ESC_29_66_1";
    }

    /**
     * 49、GS B n 选择 / 取消黑白反显打印模式
     * white printing mode off
     * Turn white/black reverse printing mode off
     * GS B n
     *
     * @return bytes for this command
     */
    public static String whitePrintingOff() {
        return "$ESC_29_66_0";
    }

    /**
     * feed paper and cut
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a full cut ( cuts the paper completely )
     *
     * @return bytes for this command
     */
    public static String feedPaperCut() {
        return "$ESC_29_86_65_0";
    }

    /**
     * feed paper and cut partial
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a partial cut ( one point left uncut )
     *
     * @return bytes for this command
     */
    public static String feedPaperCutPartial() {
        return "$ESC_29_86_66_0";
    }

    /**
     * select bar code height
     * Select the height of the bar code as n dots
     * default dots = 162
     *
     * @param dots ( heigth of the bar code )
     * @return bytes for this command
     */
    public static String barcodeHeight(byte dots) {
        return "$ESC_29_104_" + String.valueOf(dots);
    }

    /**
     * select font hri
     * Selects a font for the Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     *
     * @param n Font
     *          0, 48 Font A
     *          1, 49 Font B
     * @return bytes for this command
     */
    public static String selectFontHri(byte n) {
        return "$ESC_29_102_" + String.valueOf(n);
    }

    /**
     * select position_hri
     * Selects the print position of Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     *
     * @param n Print position
     *          0, 48 Not printed
     *          1, 49 Above the barcode
     *          2, 50 Below the barcode
     *          3, 51 Both above and below the barcode
     * @return bytes for this command
     */
    public static String selectPositionHri(byte n) {
        return "$ESC_29_72_" + String.valueOf(n);
    }

    public static String printBarcode39(String barcodeContent) {
        byte[] barcodeBytes = barcodeContent.getBytes();
        StringBuilder result = new StringBuilder("$ESC_29_107_4_");
        for (int i = 0; i < barcodeBytes.length; i++) {
            result.append(barcodeBytes[i]).append("_");
        }
        result.append("0");
        return result.toString();
    }


    /**
     * Set horizontal tab positions
     *
     * @param col ( coulumn )
     * @return bytes for this command
     */
    public String setHTPosition(byte col) {
        return "$ESC_27_68_" + String.valueOf(col) + "_0";
    }


}
