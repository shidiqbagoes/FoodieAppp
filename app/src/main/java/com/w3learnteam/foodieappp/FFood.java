package com.w3learnteam.foodieappp;

public class FFood {

    String ffoodvalue;
    Integer ffoodpic;

    public FFood() {
    }

    public FFood(String ffoodvalue, Integer ffoodpic) {
        this.ffoodvalue = ffoodvalue;
        this.ffoodpic = ffoodpic;
    }

    public String getFfoodvalue() {
        return ffoodvalue;
    }

    public void setFfoodvalue(String ffoodvalue) {
        this.ffoodvalue = ffoodvalue;
    }

    public Integer getFfoodpic() {
        return ffoodpic;
    }

    public void setFfoodpic(Integer ffoodpic) {
        this.ffoodpic = ffoodpic;
    }
}
