package thread;

import ship.AbstractShip;
import ship.Creator;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by User on 07.03.2017.
 */
public class LoaderShipThread implements Runnable {
    ArrayBlockingQueue<AbstractShip> ships;
    private static final int LOAD_SPEED = 10;
    public LoaderShipThread(ArrayBlockingQueue<AbstractShip> abstractShips) {
        this.ships = abstractShips;
    }

    @Override
    public void run() {
        AbstractShip ship;
        try {
            while (ships.size() >0) {
                ship = ships.take();
                System.out.println(ship.toString() + " Пришел");
                Thread.sleep(100+ new Random().nextInt(100));
                System.out.println("Номер: " + ship.getId() + " Начала погрузку");
                while (!ship.loadDetermine()) {
                    ship.setGoodOnShip(ship.getGoodOnShip() + LOAD_SPEED);
                    Thread.sleep(1000 + new Random().nextInt(3000));
                }
                System.out.println("Номер: " + ship.getId() + " Загрузился");
                Thread.sleep(100+ new Random().nextInt(100));
                System.out.println(ship.toString() + " Ушел");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
