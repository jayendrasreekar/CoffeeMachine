

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;


public class CoffeeMachineApplication {

    Integer outlets;
    Object obj;
    HashMap<String,Integer> ingredients;
    HashMap<String,HashMap<String,Integer>> beveragesList;
    CoffeeMachine coffeeMachine;

    public CoffeeMachineApplication(){
        ingredients = new HashMap<String, Integer>();
        beveragesList = new HashMap<String, HashMap<String, Integer>>();
    }

    private void loadConfiguration(Scanner sc) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        System.out.println("Input the Config file. If not available use the one in src/main/resources/input1.json\n");
        String path = sc.next();
        obj = parser.parse(new FileReader(path));
        System.out.println("Loading the file..\n\n");
        JSONObject jsonObject = (JSONObject) obj;
        outlets = (Integer.parseInt(String.valueOf(((JSONObject) ((JSONObject) jsonObject.get("machine")).get("outlets")).get("count_n"))));


        JSONObject resources = (JSONObject) ((JSONObject) jsonObject.get("machine")).get("total_items_quantity");
        Set<String> keys = resources.keySet();
        Iterator<String> i = keys.iterator();
        while(i.hasNext()){
            String key = i.next();
            //System.out.println(key +" and "+ resources.get(key));
            ingredients.put(key, Integer.valueOf(String.valueOf(resources.get(key))));
        }

        JSONObject beverages = (JSONObject) ((JSONObject) jsonObject.get("machine")).get("beverages");
        keys = beverages.keySet();
        i = keys.iterator();
        while(i.hasNext()){
            String key = i.next();
            JSONObject beverage = (JSONObject) beverages.get(key);
            HashMap<String,Integer> beverageConf = new HashMap<String, Integer>();
            Set<String> ingredients = beverage.keySet();
            Iterator<String> iter = ingredients.iterator();
            while(iter.hasNext()){
                String k = iter.next();
                //System.out.println(k+" and" + beverage.get(k));
                beverageConf.put(k,Integer.valueOf(String.valueOf(beverage.get(k))));
            }
            beveragesList.put(key,beverageConf);
        }
    }

    public static void main(String[]args) throws IOException, ParseException, InterruptedException {
        CoffeeMachineApplication cap = new CoffeeMachineApplication();
        Scanner sc = new Scanner(System.in);
        cap.loadConfiguration(sc);
        cap.coffeeMachine = new CoffeeMachine(cap.outlets, cap.ingredients, cap.beveragesList);
        while(true) {
          System.out.println("\n\n*************** CHAI HOUSE *******************\n");
          System.out.println("------> If User Press 1");
          System.out.println("------> If Service-person Press 2\n");
          System.out.println("-----> To Exit Press 0 \n");

          int a = sc.nextInt();

          switch(a){
              case 0: exit(0);
              case 1: System.out.println("Enter the beverages seperated by commas captured at a moment  Ex:coffee,tea,greentea");
                      System.out.println("Available beverages: "+ cap.beveragesList.keySet());
                      String req= sc.next();
                      List<String> req_beverages = Arrays.asList(req.trim().split(","));
                      cap.coffeeMachine.serveRequest(req_beverages);
                      break;
              case 2: System.out.println("Refill the Ingredients. To add ingredient, provide it in the form hot_water,150");
                      String[] s = sc.next().trim().split(",");
                      String ingredient = s[0];
                      int quantity = Integer.parseInt(s[1]);
                      if(cap.ingredients.containsKey(ingredient)) {
                          cap.ingredients.put(ingredient,cap.ingredients.get(ingredient)+quantity);
                      }
                      break;
              default:
                      System.out.println("Invalid Input Try Again");
          }
        }
    }

}
