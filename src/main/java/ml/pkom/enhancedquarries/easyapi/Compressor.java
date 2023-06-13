package ml.pkom.enhancedquarries.easyapi;

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
}
