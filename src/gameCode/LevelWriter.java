package gameCode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class LevelWriter {
	public static void writeToFile(String path, String data) {
        try {
            FileWriter writer = new FileWriter(new File(path));
            
            writer.write(data);
            
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String encode(Map<String, DataInfo> file, boolean isMain) {
        String finalString = "";
        int index = 0;
        for(int i = 0;i<file.entrySet().size();i++) {
            String key = file.keySet().toArray()[i].toString();
            DataInfo data = file.get(key);
            if(i!=0) finalString += "\n";
            finalString += key + ": ";
            
            if(data.isAttr()) {
                finalString += data.getVal();
                if(i+1 != file.size() && !isMain) finalString +=";";
                if(i+1 == file.size() || isMain) continue;
                finalString += "-";
            } else {
                finalString += "{-\n" + encode(data.getChildren(), false) + "}";
            }
            
            
            index++;
        }
        return finalString;
    }
}
