package ca.jonestremblay.jonesgroceries.entities;

public enum MeasureUnits {
    x,
    gr,
    kg,
    mL,
    L,
    oz;

    private static MeasureUnits[] list = MeasureUnits.values();

    public static MeasureUnits getUnit(int i){
        return list[i];
    }


}
