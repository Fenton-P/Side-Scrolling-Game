package gameCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class LevelReader {
	public static String fileReader(String path, String attribute) {
		File file = new File(path);
		try {
			Scanner reader = new Scanner(file);
			int lines = 0;
			
			String mainAttr = attribute.split("\\.")[0];
			String[] subAttrs = without(attribute.split("\\."), 0);
			
			while(reader.hasNextLine()) {
				String nextLine = reader.nextLine();
				lines++;
				
				while(nextLine.charAt(nextLine.length()-1) == '-') {
					nextLine = nextLine.replace("-", "") + reader.nextLine();
					lines++;
				}
				
				if(!nextLine.split(": ")[0].equals(mainAttr)) continue;
				
				String line = String.join(": ", without(nextLine.split(": "), 0));
				
				String[] finalArr = getAttr(line, subAttrs).split(": ");
				
				return finalArr[finalArr.length-1];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static DataInfo getValueFromString(String attribute, Map<String, DataInfo> nodes) {
		if(attribute.indexOf(".") == -1) return nodes.get(attribute);
		return getValueFromString(String.join(".", exclude(dataSplit(attribute), 0)), nodes.get(dataSplit(attribute)[0]).getChildren());
	}
	
	public static String[] dataSplit(String data) {
		ArrayList<String> list = new ArrayList<String>();
		
		int index = data.indexOf(".");
		
		if(index == -1) {
			String[] arr = {data};
			return arr;
		}
		
		list.add(data.substring(0, index));
		list.addAll(ListHandler.arrToList(dataSplit(data.substring(index+1))));
		
		return listToArr(list);
	}
	
	private static String getAttr(String line, String[] attribute) {
		if(line.charAt(0)!='{') {
			if(line.charAt(line.length()-1)=='}') {
				return line.substring(0, line.length()-1);
			}
			return line;
		}
		
		String newLine = String.join("", without(without(line.split(""), line.length()-1), 0));
		
		String[] attrs = customSplit(newLine);
		
		for(String str : attrs) {
			if(!str.split(": ")[0].equals(attribute[0])) {
				continue;
			}
			
			String finalString = String.join(": ", without(str.split(": "), 0));
			
			return getAttr(finalString, without(attribute, 0));
		}
		
		return null;
	}
	
	public static String[] readFile(String path) {
		Optional<Scanner> reader = getFileReader(path);
		
		Optional<String[]> val = reader.map(value -> parseFile(value));
		
        return val.orElseGet(() -> {
        	String[] arr = new String[0];
        	return arr;
        });
	}
	
	public static String[] parseFile(Scanner reader) {
        ArrayList<String> textArray = new ArrayList<String>();
        
        while(reader.hasNextLine()) textArray.add(reader.nextLine());
        
        String[] returnArray = new String[textArray.size()];
        returnArray = textArray.toArray(returnArray);
        
        return returnArray;
    }
	
	public static Optional<Scanner> getFileReader(String filePath) {
		try { return Optional.of(new Scanner(new File(filePath))); 
		} catch (IOException e) { return Optional.empty(); }
	}
    
    public static Map<String, DataInfo> decode(String[] text) {
        Map<String, DataInfo> returnMap = new HashMap<>();
        
        for(int i = 0;i<text.length;i++) {
            String line = text[i];
            
            while(line.charAt(line.length()-1) == '-') {
                i++;
                line = line.substring(0, line.length()-1);
                line += text[i];
            }
            
            returnMap.put(line.split(": ")[0], getObject(line));
        }
        
        return returnMap;
    }
    
    public static String rejoinArr(String[] arr) {
        String finalString = "";
        for(String i : arr) {
            finalString += ": " + i;
        }
        return finalString.substring(2);
    }
    
    public static String[] exclude(String[] arr, int index) {
    	if(arr.length == 0) return arr;
    	
        String[] returnArr = new String[arr.length - 1];
        
        for(int i = 0;i<returnArr.length;i++) returnArr[i] = i >= index ? arr[i+1] : arr[i];
        
        return returnArr;
    }
    
    public static DataInfo getObject(String text) {
        String[] val = text.split(": ");
        String name = val[0];
        String value = rejoinArr(exclude(val, 0));
        if(val[1].charAt(0)!='{') {
            return new DataInfo(name, value);
        }
        
        value = value.substring(1, value.length()-1);
        String[] values = customSplit(value); //Have to rejoin it if its not in the parenthesis
        //values = rejoin(values);
        Map<String, DataInfo> childObjects = new HashMap<>();
        
        for(int i = 0;i<values.length;i++) {
            String str = values[i];
            String[] val2 = str.split(": ");
            String name2 = val2[0];
            String value2 = val2[1];
            DataInfo childObject = getObject(str);
            
            childObjects.put(name2, childObject);
        }
        
        return new DataInfo(name, childObjects);
    }
    
    public static String[] rejoin(String[] arr) {
        // Example text;text should be split while text: {value1;value2} shouldn't be
        ArrayList<String> newArray = new ArrayList<String>();
        int count = 0;
        for(int i = 0;i<arr.length;i++) {
            String str = arr[i];
            count += getCount(str, '{');
            count -= getCount(str, '}');
            
            while(count > 0) {
                count += getCount(str, '{');
                count -= getCount(str, '}');
                i++;
                
                str += ";" + arr[i];
            }
            
            newArray.add(str);
        }
        
        return listToArr(newArray);
    }
    
    public static int getCount(String str, char item) {
        int count = 0;
        
        for(int i = 0;i<str.length();i++) if(str.charAt(i) == item) count++;
        
        return count;
    }
    
    public static String[] listToArr(ArrayList<String> list) {
        String[] arr = new String[list.size()];
        
        for(int i = 0;i<list.size();i++) {
            arr[i] = list.get(i);
        }
        
        return arr;
    }
	
	private static String[] customSplit(String newLine) { //Currently broken will be rewriting
//		String[] arr = newLine.split(";");
//		ArrayList<String> newArr = new ArrayList<String>();
//		
//		for(int i = 0;i<arr.length;i++) {
//			if(arr[i].split(": ")[1].charAt(0)!='{') {
//				newArr.add(arr[i]);
//				continue;
//			}
//			
//			int index2 = i+1;
//			String strToAdd = arr[i] + ";" + arr[index2];
//			int count = 0;
//			if(charCount(arr[i], "{")>1) count+=charCount(arr[i], "{")-1;
//			while(count>0) {
//				if(arr[index2].charAt(arr[index2].length()-1)=='}') {
//					count--;
//				}
//				if(arr[index2].indexOf('{')!=-1) {
//					count++;
//				}
//				if(count<=0) {
//					break;
//				}
//				index2++;
//				strToAdd += ";" + arr[index2];
//			}
//			
//			i = index2;
//			newArr.add(strToAdd);
//		}
//		
//		String[] rtnArr = new String[newArr.size()];
//		
//		int index = 0;
//		for(String i : newArr) {
//			rtnArr[index] = i;
//			index++;
//		}
//		
//		return rtnArr;
		
		String[] arr = newLine.split(";");
		ArrayList<String> newArr = new ArrayList<String>();
		
		for(int i = 0;i<arr.length;i++) {
			String str = arr[i];
			String attr = str.split(": ")[0];
			
			for(String j : without(str.split(": "), 0)) attr += ": " + j;
			
			int count = 0;
			String stringToAdd = str;
			count += getIncreasedCount(attr);
			if(attr.charAt(attr.length()-1) == '}') count -= getDecreasedCount(attr);
			while(count>0) {
				i++;
				str = arr[i];
				attr = str.split(": ")[1];
				
				stringToAdd += ";" + str;
				
				if(attr.charAt(0) == '{') count += getIncreasedCount(attr);
				if(attr.charAt(attr.length()-1) == '}') count -= getDecreasedCount(attr);
			}
			newArr.add(stringToAdd);
		}
		
		return getStringArr(newArr);
	}
	
	private static int getIncreasedCount(String attr) {
		int count = 0;
		for(int i = attr.length()-1;i>-1;i--) {
			if(attr.charAt(i) == '{') {
				count++;
			}
		}
		
		return count;
	}

	private static String[] getStringArr(ArrayList<String> arr) {
		String[] returnArr = new String[arr.size()];
		
		for(int i = 0;i<returnArr.length;i++) {
			returnArr[i] = arr.get(i);
		}
		
		return returnArr;
	}

	private static int getDecreasedCount(String attr) {
		int count = 0;
		
		for(int i = attr.length()-1;i>-1;i--) {
			if(attr.charAt(i) == '}') count++;
			else break;
		}
		
		return count;
	}

	private static int charCount(String string, String string2) {
		if(string.indexOf(string2) == -1) return 0;
		
		return charCount(string.substring(string.indexOf(string2) + 1), string2);
	}

	private static String[] without(String[] arr, int index) {
		String[] rtnArr = new String[arr.length-1];
		
		for(int i = 0;i<arr.length;i++) {
			if(i==index) {
				continue;
			}
			if(i>index) {
				rtnArr[i-1] = arr[i];
				continue;
			}
			rtnArr[i] = arr[i];
		}
		
		return rtnArr;
	}
	
	public static String[] getSaveNames() {
		ArrayList<String> names = new ArrayList<String>();
		
		File savesList = new File("src/gameCode/saves/savesList.txt");
		
		try {
			Scanner scanner = new Scanner(savesList);
			
			while(scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				
				String name = nextLine.split(": ")[1];
				names.add(name);
			}
			
			String[] rtnArr = new String[names.size()];
			
			for(int i = 0;i<names.size();i++) {
				rtnArr[i] = names.get(i);
			}
			
			return rtnArr;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}