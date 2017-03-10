package thread;

import ship.AbstractShip;
import ship.Creator;

import java.util.concurrent.*;

/**
 * Created by User on 06.03.2017.
 */
public class CommonShipThread {

    private ArrayBlockingQueue<AbstractShip>
            ships = new ArrayBlockingQueue<>(Creator.TUNEL_COUNT),
            shipsListOil = new ArrayBlockingQueue<>(Creator.QUEUE_COUNT),
            shipsListBox = new ArrayBlockingQueue<>(Creator.QUEUE_COUNT),
            shipsListEat = new ArrayBlockingQueue<>(Creator.QUEUE_COUNT);

    private LoaderShipThread
            loaderOilThread = new LoaderShipThread(shipsListOil),
            loaderBoxThread = new LoaderShipThread(shipsListBox),
            loaderEatThread = new LoaderShipThread(shipsListEat);

    private ExecutorService
            serviceOil = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT),
            serviceBox = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT),
            serviceEat = Executors.newFixedThreadPool(Creator.CONNECTION_COUNT);


    private DesignerShipThread designerShipThread = new DesignerShipThread();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void loadShip(int shipCount) throws InterruptedException {

        System.out.println("Старт!!!");
        try {
            for (int i = 0; i < shipCount; i++) {
                ships.add(executorService.submit(designerShipThread).get());
                for (AbstractShip abstractShip : ships) {
                    switch (abstractShip.getType()) {
                        case Creator.OIL_SHIP:
                            while (shipsListOil.size() >= shipsListOil.remainingCapacity()) {
                                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
                            }
                            shipsListOil.add(ships.take());
                            serviceOil.execute(loaderOilThread);
                            break;
                        case Creator.BOX_SHIP:
                            while (shipsListBox.size() >= shipsListOil.remainingCapacity()) {
                                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
                            }
                            shipsListBox.add(ships.take());
                            serviceBox.execute(loaderBoxThread);
                            break;
                        case Creator.EAT_SHIP:
                            while (shipsListEat.size() >= shipsListOil.remainingCapacity()) {
                                executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
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
