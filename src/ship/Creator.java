package ship;

/**
 * Created by User on 04.03.2017.
 */
public interface Creator {
    int CONNECTION_COUNT = 2;
    String OIL_SHIP = "oil";
    String BOX_SHIP = "box";
    String EAT_SHIP = "eat";
    String TYPE_SHIP[] = {OIL_SHIP , BOX_SHIP , EAT_SHIP};

    String BIG_CAPACITY = "big";
    String MIDDLE_CAPACITY = "middle";
    String LITTLE_CAPACITY = "little";
    String TYPE_CAPACITY[] = {BIG_CAPACITY , MIDDLE_CAPACITY ,LITTLE_CAPACITY};
}
