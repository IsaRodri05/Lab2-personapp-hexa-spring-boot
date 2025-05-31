package co.edu.javeriana.as.personapp.domain;

public enum Gender {
    MALE("M"), 
    FEMALE("F"), 
    OTHER("O");

    private final String dbValue;

    Gender(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static Gender fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Gender value cannot be null");
        }
        
        String normalized = value.trim().toUpperCase();
        switch (normalized) {
            case "M":
            case "MALE":
                return MALE;
            case "F":
            case "FEMALE":
                return FEMALE;
            case "O":
            case "OTHER":
                return OTHER;
            default:
                throw new IllegalArgumentException("Invalid gender value: " + value + 
                    ". Valid values: M, F, O, MALE, FEMALE, OTHER");
        }
    }

    public static String toDbValue(String gender) {
        return fromString(gender).getDbValue();
    }

    public static Character toCharDbValue(String gender) {
        return fromString(gender).getDbValue().charAt(0);
    }
}