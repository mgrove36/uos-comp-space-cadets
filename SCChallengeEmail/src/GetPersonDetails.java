import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class GetPersonDetails {
  public static void getName(String userId) {
    try {
      String parseLine = "";
      URL url = new URL("https://www.ecs.soton.ac.uk/people/" + userId);
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
      StringBuilder result = new StringBuilder();
      String userEmail = userId+"@soton.ac.uk";
      while (!parseLine.contains(userEmail)) {
        parseLine = br.readLine();
        result.append(parseLine);
      }
      int additionalOpenedBrackets = 1;
      while (!parseLine.contains("}") || additionalOpenedBrackets > 0) {
        parseLine = br.readLine();
        if (parseLine.contains("{")) additionalOpenedBrackets++;
        if (parseLine.contains("}")) additionalOpenedBrackets--;
        result.append(parseLine);
      }
      br.close();
      int userDataStart = result.substring(0,result.lastIndexOf(userEmail)).lastIndexOf("{");
      int addressStart = result.indexOf("\"address\":") + 11;
      int imageStart = result.indexOf("\"image\":") + 9;
      String userDataString = result.substring(userDataStart,userDataStart + result.substring(userDataStart,userDataStart + result.substring(userDataStart + 1).indexOf("{")).lastIndexOf(",")) + "}";
      String addressDataString = result.substring(addressStart,addressStart + result.substring(addressStart).indexOf("}")) + "}";
      String imageDataString = result.substring(imageStart,imageStart + result.substring(imageStart).indexOf("}")) + "}";
      int imageUrlStart = imageDataString.indexOf("\"url\": \"") + 8;
      String imageUrl = imageDataString.substring(imageUrlStart,imageUrlStart + imageDataString.substring(imageUrlStart).indexOf("}") - 1).replace("\"","").strip();
      HashMap<String, String> userData = new Gson().fromJson(userDataString, new TypeToken<HashMap<String, String>>() {}.getType());
      HashMap<String, String> addressData = new Gson().fromJson(addressDataString, new TypeToken<HashMap<String, String>>() {}.getType());
      userData.put("imageUrl","https://www.southampton.ac.uk" + imageUrl);
      printSeparator();
      System.out.println("User Data:");
      for (String key: userData.keySet()) {
        System.out.println(key + ": " + userData.get(key));
      }
      printSeparator(2, "");
      System.out.println("Address Data:");
      for (String key: addressData.keySet()) {
        System.out.println(key + ": " + addressData.get(key));
      }
      printSeparator();
    } catch (IOException err) {
      System.out.println(err);
    }
  }

  public static void printSeparator() {
    System.out.println("###############################");
  }

  public static void printSeparator(Integer count) {
    for (int i=1; i <= count; i++) {
      printSeparator();
    }
  }

  public static void printSeparator(Integer count, String separator) {
    for (int i=1; i <= count; i++) {
      printSeparator();
      if (i != count) System.out.println(separator);
    }
  }

  public static void main(String[] args) {
    getName("dem");
  }
}
