package com.example.masluli.Model.Weather;

public
class Day {
    String mintemp_c;
    String maxtemp_c;
    String daily_will_it_rain;
    String daily_chance_of_rain;
    Condition condition;

    public String getMintemp_c() {
        return mintemp_c;
    }

    public void setMintemp_c(String mintemp_c) {
        this.mintemp_c = mintemp_c;
    }

    public String getMaxtemp_c() {
        return maxtemp_c;
    }

    public void setMaxtemp_c(String maxtemp_c) {
        this.maxtemp_c = maxtemp_c;
    }

    public String getDaily_will_it_rain() {
        return daily_will_it_rain;
    }

    public void setDaily_will_it_rain(String daily_will_it_rain) {
        this.daily_will_it_rain = daily_will_it_rain;
    }

    public String getDaily_chance_of_rain() {
        return daily_chance_of_rain;
    }

    public void setDaily_chance_of_rain(String daily_chance_of_rain) {
        this.daily_chance_of_rain = daily_chance_of_rain;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
