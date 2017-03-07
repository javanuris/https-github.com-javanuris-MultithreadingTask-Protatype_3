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


    public void designShip() throws InterruptedException {
        DesignerShipThread designerShipThread = new DesignerShipThread();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            for (int i = 0; i < 10; i++) {
                Future<AbstractShip> abstractShipFuture = executorService.submit(designerShipThread);
                ships.add(abstractShipFuture.get());
                for (AbstractShip abstractShip : ships) {
                    switch (abstractShip.getType()) {
                        case Creator.OIL_SHIP:
                            shipsListOil.add(ships.take());
                            loaderTask(shipsListOil);
                            break;
                        case Creator.BOX_SHIP:
                            shipsListBox.add(ships.take());
                            loaderTask(shipsListBox);
                            break;
                        case Creator.EAT_SHIP:
                            shipsListEat.add(ships.take());
                            loaderTask(shipsListEat);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IllegalStateException e) {
            executorService.awaitTermination(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    public void loaderTask(ArrayBlockingQueue<AbstractShip> shipsListOil) {
        LoaderShipThread loaderShipThread = new LoaderShipThread(shipsListOil);
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(loaderShipThread);
        service.shutdown();
    }

}
