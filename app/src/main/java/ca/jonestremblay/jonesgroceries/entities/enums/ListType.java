package ca.jonestremblay.jonesgroceries.entities.enums;

public enum ListType {
    grocery,
    recipe;

    private static ListType[] list = ListType.values();

    public static ListType getType(int i){
        return list[i];
    }


}
