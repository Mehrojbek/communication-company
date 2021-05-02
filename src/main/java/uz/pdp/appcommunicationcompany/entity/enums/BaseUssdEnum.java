package uz.pdp.appcommunicationcompany.entity.enums;

public enum BaseUssdEnum {
    BALANCE("*102#"),
    SHOW_NUMBER("*148#"),
    MENU("*111#");
    //YANA BIR QANCHA KODLAR BUNI
    private String ussdCode;

    BaseUssdEnum(String ussdCode) {
        this.ussdCode = ussdCode;
    }

    public String getUssdCode() {
        return ussdCode;
    }
}
