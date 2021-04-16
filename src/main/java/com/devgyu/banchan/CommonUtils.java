package com.devgyu.banchan;

public class CommonUtils {
    public static String splitSigungu(String road){
        String sigungu = road.split(" ")[1];
        String convertedRoad = "";

        if (sigungu.indexOf('시') != -1) {
            convertedRoad = sigungu.split("시")[0] + "시";
        } else if (sigungu.indexOf('군') != -1) {
            convertedRoad = sigungu.split("군")[0] + "군";
        } else if (sigungu.indexOf('구') != -1) {
            convertedRoad = sigungu.split("구")[0] + "구";
        } else {
            throw new IllegalArgumentException("주문을 확인하던중 에러가 발생하였습니다. 관리자에게 문의 부탁드립니다.");
        }
        return convertedRoad;
    }
}
