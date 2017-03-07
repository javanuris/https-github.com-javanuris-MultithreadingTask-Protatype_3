package thread;

import ship.AbstractShip;
import ship.Creator;

import java.util.concurrent.*;

/**
 * Created by User on 06.03.2017.
 */
public class CommonShipThread {
    private ArrayBlockingQueue<AbstractShip> ships = new ArrayBlockingQueue<>(Creator.TUNEL_COUNT);
    private ArrayBlockingQueue<AbstractShip> shipsListOil = new ArrayBlockingQueue<>(Creator.QUEUE_COUNT);
    private ArrayBlockingQueue<AbstractShip> shipsListBox = new ArrayBlockingQueue<>(Creator.QUEUE_COUNT);
    private ArrayBlockingQueue<AbstractShip> shipsListEat = new ArrayBlockingQueue<>(Creator.QUEUE_COUNT);

    private LoaderShipThread loaderOilThread = new LoaderShipThread(shipsListOil);
    private ExecutorService serviceOil = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);
    private LoaderShipThread loaderBoxThread = new LoaderShipThread(shipsListBox);
    private ExecutorService serviceBox = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);
    private LoaderShipThread loaderEatThread = new LoaderShipThread(shipsListEat);
    private ExecutorService serviceEat = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);

    private DesignerShipThread designerShipThread = new DesignerShipThread();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void designShip(int shipCount) throws InterruptedException {
        System.out.println("Старт!!!");
        try {
            for (int i = 0; i < shipCount; i++) {
                ships.add(executorService.submit(designerShipThread).get());
                for (AbstractShip abstractShip : ships) {
                    switch (abstractShip.getType()) {
                        case Creator.OIL_SHIP:
                            if (shipsListOil.size() >= shipsListOil.size()) {
                                executorService.awaitTermination(3, TimeUnit.SECONDS);
                            }
                            shipsListOil.add(ships.take());
                            serviceOil.execute(loaderOilThread);
                            break;
                        case Creator.BOX_SHIP:
                            if (shipsListBox.size() >= shipsListBox.size()) {
                                executorService.awaitTermination(3, TimeUnit.SECONDS);
                            }
                            shipsListBox.add(ships.take());
                            serviceBox.execute(loaderBoxThread);
                            break;
                        case Creator.EAT_SHIP:
                            if (shipsListEat.size() >= shipsListEat.size()) {
                                executorService.awaitTermination(3, TimeUnit.SECONDS);
                            }
                            shipsListEat.add(ships.take());
                            serviceEat.execute(loaderEatThread);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Cистема перегруженна!!!");
            shutdownTrio();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        shutdownTrio();
    }

    private void shutdownTrio() {
        executorService.shutdown();
        serviceOil.shutdown();
        serviceBox.shutdown();
        serviceEat.shutdown();
    }
}
