package main.java.FEM.FileOperation;




public class DataFromFile {

    private double temperature;
    private double tau;
    private double dtau;
    private double ambT;
    private double alfa;
    private double _H;
    private double _B;
    private Integer N_H;
    private Integer N_B;
    private double specHeat;
    private double conductivity;
    private double density;

    public DataFromFile() {
        temperature = 100;
        tau = 500;
        dtau = 50;
        ambT = 1200;
        alfa = 300;
        _H = 0.100;
        _B = 0.100;
        N_H = 4;
        N_B = 4;
        specHeat = 700;
        conductivity = 25;
        density = 7800;
    }


    public double getTemperature() {
        return temperature;
    }

    public double getTau() {
        return tau;
    }


    public double getDtau() {
        return dtau;
    }


    public double getAmbT() {
        return ambT;
    }


    public double getAlfa() {
        return alfa;
    }


    public double get_H() {
        return _H;
    }


    public double get_B() {
        return _B;
    }


    public Integer getN_H() {
        return N_H;
    }


    public Integer getN_B() {
        return N_B;
    }


    public double getSpecHeat() {
        return specHeat;
    }


    public double getConductivity() {
        return conductivity;
    }


    public double getDensity() {
        return density;
    }


}
