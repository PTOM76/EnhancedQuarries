package ml.pkom.enhancedquarries.easyapi;

import java.io.UnsupportedEncodingException;

public class Compressor {
    public static String compress(String src) {
        src = num2str(bin2hex(src));

        int cnt = 0;
        int length = src.length();
        String result = "", prev = "", c = "";
        boolean firstFlag = true;
        for (int i = 0; i < length; i++){
            c = src.substring(i, i + 1);
            if (firstFlag){
                firstFlag = false;
                prev = c;
            }
            if (!c.equals(prev)){
                if (cnt > 0){
                    result += prev + cnt;
                    cnt = 0;
                }
            }
            prev = c;
            cnt++;
        }
        result += c + cnt;
        return result;
    }

    public static String decompress(String data) {

        int cnt = 0;
        int length = src.length();
        String result = "", prev = "", c = "";
        boolean firstFlag = true;
        for (int i = 0; i < length; i++){
            c = src.substring(i, i+1);
            if (firstFlag){
                firstFlag = false;
                prev = c;
            }
            if (!c.equals(prev)){
                if (cnt > 0){
                    result += prev + cnt;
                    cnt = 0;
                }
            }
            prev = c;
            cnt++;
        }
        result += c + cnt;
        result = hex2bin(str2num(result));
        return result;
    }

    public static String bin2hex(String data) {
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            String s = Integer.toHexString(c);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String hex2bin(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] = (byte) Integer.parseInt(hex.substring(index * 2, (index + 1) * 2), 16);
        }


        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String num2str(String str) {
        return str.replace("0", "o")
                .replace("1", "p")
                .replace("2", "q")
                .replace("3", "r")
                .replace("4", "s")
                .replace("5", "t")
                .replace("6", "u")
                .replace("7", "v")
                .replace("8", "w")
                .replace("9", "x");
    }

    public static String str2num(String str) {
        return str.replace("o", "0")
                .replace("p", "1")
                .replace("q", "2")
                .replace("r", "3")
                .replace("s", "4")
                .replace("t", "5")
                .replace("u", "6")
                .replace("v", "7")
                .replace("w", "8")
                .replace("x", "9");
    }
}
