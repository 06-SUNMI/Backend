package capstone.everyhealth.domain.routine;

import java.util.*;

public class WorkoutData {

    public static final Map<String, List<String>> workoutMap = new HashMap<>(){{
        put("가슴",new ArrayList<>(Arrays.asList("벤치 프레스 (덤벨)", "체스트 프레스 (머신)", "푸쉬업")));
        put("등",new ArrayList<>(Arrays.asList("렛 풀다운", "원 암 덤벨 로우", "시티드 로우 (케이블)")));
        put("하체",new ArrayList<>(Arrays.asList("스쿼트", "와이드 스쿼트", "레그 익스텐션")));
        put("어깨",new ArrayList<>(Arrays.asList("숄더 프레스 (덤벨)", "레터럴 레이즈 (덤벨)", "숄더 프레스 (머신)")));
        put("삼두",new ArrayList<>(Arrays.asList("벤치 딥스","트라이셉 푸쉬다운 (케이블)", "원 암 트라이셉 익스텐션 (덤벨)", "라잉 트라이셉 익스텐션 (바벨)")));
        put("이두",new ArrayList<>(Arrays.asList("바이셉 컬 (덤벨)", "해머 컬 (덤벨)", "바이셉 컬 (케이블)")));
        put("코어",new ArrayList<>(Arrays.asList("크런치", "플랭크", "레그 레이즈")));
        put("전완근",new ArrayList<>(Arrays.asList("리스트 컬 (덤벨)", "리버스 리스트 컬 (덤벨)", "리스트 컬 (바벨)")));
        put("유산소",new ArrayList<>(Arrays.asList("트레드밀 러닝", "슬로우 버피", "크리스 크로스 점프")));
    }};
}
