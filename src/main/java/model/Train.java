package model;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private int id;
    private String trainCode;
    private List<TrainStop> stops;

    public Train(int id, String trainCode){
        this.id = id;
        this.trainCode = trainCode;
        this.stops = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public String getTrainCode(){
        return  trainCode;
    }

    public List<TrainStop> getStops(){
        return stops;
    }

}
