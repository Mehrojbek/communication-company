package uz.pdp.appcommunicationcompany.entity.enums;

public enum CodeEnum {
    ZERO(90),
    ONE(91);

    private int codeNumber;

    CodeEnum(int codeNumber) {
        this.codeNumber = codeNumber;
    }

    public int getCodeNumber() {
        return codeNumber;
    }
}
