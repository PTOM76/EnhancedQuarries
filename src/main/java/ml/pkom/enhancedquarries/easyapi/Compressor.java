package ml.pkom.enhancedquarries.easyapi;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compressor {
    public static String compress(String str) {
        try {
            byte[] data = str.getBytes("UTF-8");
            Deflater deflater = new Deflater();
            deflater.setInput(data);
            deflater.finish();
            byte[] compressedData = new byte[data.length];
            int compressedDataLength = deflater.deflate(compressedData);
            deflater.end();
            byte[] result = new byte[compressedDataLength];
            System.arraycopy(compressedData, 0, result, 0, compressedDataLength);
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompress(String compressedStr) {
        try {
            byte[] compressedData = Base64.getDecoder().decode(compressedStr);
            Inflater inflater = new Inflater();
            inflater.setInput(compressedData);
            byte[] decompressedData = new byte[compressedData.length];
            int decompressedDataLength = inflater.inflate(decompressedData);
            inflater.end();
            byte[] result = new byte[decompressedDataLength];
            System.arraycopy(decompressedData, 0, result, 0, decompressedDataLength);
            return new String(result, "UTF-8");
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
