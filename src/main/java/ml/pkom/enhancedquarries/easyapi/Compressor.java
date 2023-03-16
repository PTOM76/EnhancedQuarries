package ml.pkom.enhancedquarries.easyapi;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compressor {
    public static String compress(String str) {
        try {
            byte[] input = str.getBytes("UTF-8");
            Deflater deflater = new Deflater();
            deflater.setInput(input);
            deflater.finish();
            byte[] output = new byte[deflater.getTotalOut()];
            deflater.deflate(output);
            deflater.end();
            return new String(Base64.getEncoder().encode(output));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompress(String compressedStr) {
        try {
            byte[] input = Base64.getDecoder().decode(compressedStr);
            Inflater inflater = new Inflater();
            inflater.setInput(input);
            byte[] output = new byte[100];
            int resultLength = inflater.inflate(output);
            inflater.end();
            return new String(output, 0, resultLength, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
