package main;

import thread.CommonShipThread;
import java.util.concurrent.*;

/**
 * Created by User on 06.03.2017.
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
   new CommonShipThread().designShip(30);


    }

}
