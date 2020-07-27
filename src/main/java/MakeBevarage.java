import java.util.HashMap;
import java.util.List;

public class MakeBevarage implements Runnable {

    HashMap<String,Integer> ingredients;
    HashMap<String,HashMap<String,Integer>> beveragesList;
    List<String> statuses;
    MakeBevarage(List<String> requests, HashMap<String,Integer> ingredients, HashMap<String,HashMap<String,Integer>> beveragesList,List<String> statuses){
        this.ingredients = ingredients;
        this.beveragesList = beveragesList;
        this.statuses = statuses;

        Thread[] threads = new Thread[requests.size()];
        int i=0;

        // For n requestes at the same time , n threads are spawned to serve the request
        for(String req : requests ) {
            Thread t1 = new Thread(this, req);
            threads[i++]=t1;
        }
        for(i=0;i<requests.size();i++){
            threads[i].start();
        }


        // Wait for all the threads to complete
        for(i=0;i<requests.size();i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    // piece of code which modifies the critical section items Ex: ingredents
    // to avoid inconsistencies and this function calls execute in synchronous way

    public synchronized void makeDrink(String beverage) throws InterruptedException {
        HashMap<String,Integer> bevarageConf = this.beveragesList.get(beverage);
        for(String s:bevarageConf.keySet()){
            Integer available = ingredients.get(s);

            if(available == null){
                this.statuses.add(beverage+"  Cannot be prepared because of Non availability of the Ingredient "+s);
                return;
            }

            Integer required = bevarageConf.get(s);
            Integer leftOver = available - required;
            if(leftOver>=0){
                ingredients.put(s,leftOver);
            }
            else{
                this.statuses.add(beverage+" Cannot be prepared because of Insufficient Ingredient "+s);
                return;
            }
        }
        statuses.add(beverage+"  is getting served.......");
        return;
    }


    public void run() {
        String beverage =  Thread.currentThread().getName();
        try {
            makeDrink(beverage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
