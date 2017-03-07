package main;

import ship.AbstractShip;
import ship.Creator;
import thread.CommonShipThread;
import thread.DesignerShipThread;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by User on 06.03.2017.
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CommonShipThread commonShipThread = new CommonShipThread();
        commonShipThread.designShip();

    }

}
