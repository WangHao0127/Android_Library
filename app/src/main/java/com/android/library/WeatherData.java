package com.android.library;

/**
 * Author: WangHao
 * Created On: 2018/9/19 0019 11:01
 * Description:
 */
public class WeatherData {

    /**
     * weatherinfo : {"city":"北京","cityid":"101010100","temp":"27.9","WD":"南风","WS":"小于3级",
     * "SD":"28%","AP":"1002hPa","njd":"暂无实况","WSE":"<3","time":"17:55","sm":"2.1","isRadar":"1",
     * "Radar":"JC_RADAR_AZ9010_JB"}
     */

    private WeatherinfoBean weatherinfo;

    public WeatherinfoBean getWeatherinfo() { return weatherinfo;}

    public void setWeatherinfo(WeatherinfoBean weatherinfo) { this.weatherinfo = weatherinfo;}

    public static class WeatherinfoBean {
        /**
         * city : 北京
         * cityid : 101010100
         * temp : 27.9
         * WD : 南风
         * WS : 小于3级
         * SD : 28%
         * AP : 1002hPa
         * njd : 暂无实况
         * WSE : <3
         * time : 17:55
         * sm : 2.1
         * isRadar : 1
         * Radar : JC_RADAR_AZ9010_JB
         */

        private String city;
        private String cityid;
        private String temp;
        private String WD;
        private String WS;
        private String SD;
        private String AP;
        private String njd;
        private String WSE;
        private String time;
        private String sm;
        private String isRadar;
        private String Radar;

        public String getCity() { return city;}

        public void setCity(String city) { this.city = city;}

        public String getCityid() { return cityid;}

        public void setCityid(String cityid) { this.cityid = cityid;}

        public String getTemp() { return temp;}

        public void setTemp(String temp) { this.temp = temp;}

        public String getWD() { return WD;}

        public void setWD(String WD) { this.WD = WD;}

        public String getWS() { return WS;}

        public void setWS(String WS) { this.WS = WS;}

        public String getSD() { return SD;}

        public void setSD(String SD) { this.SD = SD;}

        public String getAP() { return AP;}

        public void setAP(String AP) { this.AP = AP;}

        public String getNjd() { return njd;}

        public void setNjd(String njd) { this.njd = njd;}

        public String getWSE() { return WSE;}

        public void setWSE(String WSE) { this.WSE = WSE;}

        public String getTime() { return time;}

        public void setTime(String time) { this.time = time;}

        public String getSm() { return sm;}

        public void setSm(String sm) { this.sm = sm;}

        public String getIsRadar() { return isRadar;}

        public void setIsRadar(String isRadar) { this.isRadar = isRadar;}

        public String getRadar() { return Radar;}

        public void setRadar(String Radar) { this.Radar = Radar;}
    }
}
