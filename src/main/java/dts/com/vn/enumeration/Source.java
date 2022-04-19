package dts.com.vn.enumeration;

public enum Source {

    SMPP("SMS"),
    BATCH("SOAP");
    private final String key;

    Source(String key) {this.key = key;}

    public static String getValue(String sourceType) {
        String chanel = null;
        switch (sourceType) {
            case "SMPP":
                chanel = Source.SMPP.value();
                break;
            case "MBF":
            case "BATCH":
                chanel = Source.BATCH.value();
                break;
        }
        return chanel;
    }

    public String value() {
        return this.key;
    }

}
