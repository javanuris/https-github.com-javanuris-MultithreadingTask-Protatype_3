package thread;
import ship.AbstractShip;
import ship.Creator;
import ship.ShipCreator;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by User on 06.03.2017.
 */
public class DesignerShipThread implements Callable<AbstractShip> {

    private volatile AbstractShip abstractShip;
    private volatile int shipId = 0;

    @Override
    public AbstractShip call() throws Exception {
        shipId++;
        abstractShip = new ShipCreator(Creator.TYPE_SHIP[new Random().nextInt(3)], Creator.TYPE_CAPACITY[new Random().nextInt(3)], shipId);
        Thread.sleep(100+ new Random().nextInt(1000));
        return abstractShip;
    }
}