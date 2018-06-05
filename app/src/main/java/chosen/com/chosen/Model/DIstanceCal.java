package chosen.com.chosen.Model;

public class DIstanceCal {

    long lati, longi;


    public DIstanceCal(long lati,long longi){

        this.lati = lati;
        this.longi = longi;

    }


    public long getLati() {
        return lati;
    }

    public void setLati(long lati) {
        this.lati = lati;
    }

    public long getLongi() {
        return longi;
    }

    public void setLongi(long longi) {
        this.longi = longi;
    }

    public float getDistance(){

        return 0.0f;
    }
}
