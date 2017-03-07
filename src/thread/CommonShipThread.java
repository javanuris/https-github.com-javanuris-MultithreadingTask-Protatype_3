package thread;

import ship.AbstractShip;
import ship.Creator;

import java.util.concurrent.*;

/**
 * Created by User on 06.03.2017.
 */
public class CommonShipThread {
    private ArrayBlockingQueue<AbstractShip> ships = new ArrayBlockingQueue<>(1);
    private ArrayBlockingQueue<AbstractShip> shipsListOil = new ArrayBlockingQueue<>(10);
    private ArrayBlockingQueue<AbstractShip> shipsListBox = new ArrayBlockingQueue<>(10);
    private ArrayBlockingQueue<AbstractShip> shipsListEat = new ArrayBlockingQueue<>(10);

    private LoaderShipThread loaderOilThread = new LoaderShipThread(shipsListOil);
    private ExecutorService serviceOil = Executors.newFixedThreadPool(2);
    private LoaderShipThread loaderBoxThread = new LoaderShipThread(shipsListBox);
    private ExecutorService serviceBox = Executors.newFixedThreadPool(2);
    private LoaderShipThread loaderEatThread = new LoaderShipThread(shipsListEat);
    private ExecutorService serviceEat = Executors.newFixedThreadPool(2);

    public void designShip(int shipCount) throws InterruptedException {
        DesignerShipThread designerShipThread = new DesignerShipThread();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            for (int i = 0; i < shipCount; i++) {
                ships.add(executorService.submit(designerShipThread).get());
                System.out.println(ships.size());
                for (AbstractShip abstractShip : ships) {
                    switch (abstractShip.getType()) {
                        case Creator.OIL_SHIP:
                            shipsListOil.add(ships.take());
                            System.out.println(shipsListOil.size() + " Oli");
                            serviceOil.execute(loaderOilThread);
                            break;
                        case Creator.BOX_SHIP:
                            shipsListBox.add(ships.take());
                            System.out.println(ships.size());
                            System.out.println(shipsListOil.size() + " Box");
                            serviceBox.execute(loaderBoxThread);
                            break;
                        case Creator.EAT_SHIP:
                            shipsListEat.add(ships.take());
                            System.out.println(shipsListOil.size() + " Eat");
                            serviceEat.execute(loaderEatThread);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Cистема перегруженна!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        serviceOil.shutdown();
        serviceBox.shutdown();
        serviceEat.shutdown();
    }
}
