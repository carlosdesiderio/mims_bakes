package uk.me.desiderio.mimsbakes.view;

/**
 * Utils class to format ingredients strings
 */

public class StringUtils {
    
    public static final String UNICODE_BULLET_POINT = "\u2022 ";
    /**
     * returns ingredient string formatted for the details view ingredient list
     * eg: 1.5 tsp salt
     */
    public static String getFormatedIngredientString(String name, float quantity, String meassure) {
        return getFormattedQuantity(quantity) + getFormatedMeassure(meassure) + " " + name;
    }

    /**
     * returns ingredient string formatted for the shopping list
     * eg: Salt (1.5 tsp)
     */
    public static String getFormatedIngredientStringForShopping(String name,
                                                                float quantity,
                                                                String meassure) {

        StringBuilder builder = new StringBuilder(UNICODE_BULLET_POINT);
        builder.append(name);
        builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
        builder.append(" (")
                .append(getFormattedQuantity(quantity))
                .append(getFormatedMeassure(meassure))
                .append(")");

        return builder.toString();
    }

    // removes any extra zeros to the right
    private static String getFormattedQuantity(float quantity) {
        String quantityString = String.valueOf(quantity);
        return (quantityString.contains(".")
                ?quantityString.replaceAll("\\.?0+$","")
                :quantityString);
    }

    // optimises meassure string
    private static String getFormatedMeassure(String meassure) {
        String formattedMeasure = meassure.toLowerCase();

        switch (formattedMeasure) {
            case "unit":
                // removes this string
                return "";
            case "k":
                // changes it for readibility
                return "kg";
            case "g":
            case "oz":
                // removes space on the left
                return formattedMeasure;
            default:
                return " " + formattedMeasure;
        }
    }
}
