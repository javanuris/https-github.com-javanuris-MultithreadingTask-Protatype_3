package ship;

/**
 * Created by User on 04.03.2017.
 */
public abstract class AbstractShip {
    private String type;
    private String capacityInfo;
    private int maxCapacity;
    volatile private boolean loaded = false;
    volatile private int goodOnShip = 0;
    volatile private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AbstractShip(String type, String capacity , int id) {
        this.type = type;
        this.capacityInfo = capacity;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getCapacityInfo() {
        return capacityInfo;
    }

    public synchronized int getMaxCapacity() {
        switch (getCapacityInfo()) {
            case Creator.BIG_CAPACITY:
                return maxCapacity = 100;
            case Creator.MIDDLE_CAPACITY:
                return maxCapacity = 50;
            case Creator.LITTLE_CAPACITY:
                return maxCapacity = 20;
        }
        return 0;
    }

    public int getGoodOnShip() {
        return goodOnShip;
    }

    public synchronized void setGoodOnShip(int goodOnShip) {
        this.goodOnShip = goodOnShip;
    }

    public synchronized boolean loadDetermine() {
        if (getGoodOnShip() >= getMaxCapacity()) {
            return true;
        }
        return false;
    }

    @Override
    public  String toString() {
        return "Номер: "+getId() + " - Тип: "+getType().toUpperCase() + "  Груз на судне " + getGoodOnShip();
    }
}
