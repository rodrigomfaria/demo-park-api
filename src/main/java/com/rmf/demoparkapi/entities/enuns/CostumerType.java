package com.rmf.demoparkapi.entities.enuns;

public enum CostumerType {

    PESSOAFISICA(1, "Pessoa Física"), PESSOAJURIDICA(2, "Pessoa Jurídica");

    private int cod;

    private String description;

    CostumerType(int cod, String description) {
        this.description = description;
        this.cod = cod;
    }

    public int getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static CostumerType toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (CostumerType x : CostumerType.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Invalid Id: " + cod);
    }

}
