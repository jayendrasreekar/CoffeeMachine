import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoffeeMachine {

  Integer outlets;
  Object obj;
  HashMap<String,Integer> ingredients;
  HashMap<String,HashMap<String,Integer>> beveragesList;
  List<String> statuses = new ArrayList<String>();

  CoffeeMachine(Integer outlets, HashMap<String,Integer> ingredients, HashMap<String,HashMap<String,Integer>> beveragesList){
      this.outlets = outlets;
      this.ingredients = ingredients;
      this.beveragesList = beveragesList;
  }

  public void showUnavailability(){
     //TODO : Later :  show unavailability of certain ingredients or drinks based on calculations at everytime the user is showed menu
  };

  public void serveRequest(List<String> requests){
      statuses.clear();
      // Class that handles the request
      MakeBevarage b1 = new MakeBevarage(requests,ingredients,beveragesList,statuses);
      System.out.println("\n\n");
      for(String i: statuses){
          System.out.println(i);
      }
  }

}

