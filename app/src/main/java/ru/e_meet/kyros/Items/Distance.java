package ru.e_meet.kyros.Items;

/**
 * Created by Владимир on 11.03.2017.
 */

public class Distance{
    public int m;
    public String lb;
    public Distance(int distance, String label){
        m=distance;
        lb=label;
    }
    public static int findByDistance(Distance[] objects, int distance){
        for(int i=0; i<objects.length; i++){
            if (objects[i].m==distance) return i;
        }
        return 0;
    }
    public int km(){return m/1000;}
}
