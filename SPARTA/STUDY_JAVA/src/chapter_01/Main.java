package chapter_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * INPUT
        짜파게티 만들기
        5
        끓는 물에 짜파게티 면을 5분 정도 삶아줍니다.
        면이 익으면 물을 거의 다 버리고, 약간의 면수를 남깁니다 (약 2-3 스푼 정도).
        짜파게티 스프와 후레이크를 넣고 잘 섞어줍니다.
        남은 면수와 함께 면을 볶듯이 섞으며 스프가 면에 잘 스며들도록 해주세요.
        참기름과 올리브유 약간을 추가해 고소한 맛을 더합니다.
        완성된 짜파게티를 접시에 담아주세요.
        추가로 계란 프라이, 파채, 김가루를 곁들여주면 더욱 맛있습니다.
        고추나 청양고추를 곁들이면 매콤한 맛을 더할 수 있습니다.
        잘 섞어 먹으면 완성!
        맛있게 드세요!
     * 
     * OUTPUT
        [짜파게티 만들기]
        별점 : 5 (100.0%)
        1. 끓는 물에 짜파게티 면을 5분 정도 삶아줍니다.
        2. 면이 익으면 물을 거의 다 버리고, 약간의 면수를 남깁니다 (약 2-3 스푼 정도).
        3. 짜파게티 스프와 후레이크를 넣고 잘 섞어줍니다.
        4. 남은 면수와 함께 면을 볶듯이 섞으며 스프가 면에 잘 스며들도록 해주세요.
        5. 참기름과 올리브유 약간을 추가해 고소한 맛을 더합니다.
        6. 완성된 짜파게티를 접시에 담아주세요.
        7. 추가로 계란 프라이, 파채, 김가루를 곁들여주면 더욱 맛있습니다.
        8. 고추나 청양고추를 곁들이면 매콤한 맛을 더할 수 있습니다.
        9. 잘 섞어 먹으면 완성!
        10. 맛있게 드세요!
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Main thisMain = new Main();
        StringBuilder messageStorage = new StringBuilder();
        BufferedReader  br          = new BufferedReader(new InputStreamReader(System.in));
        for(int i = 1; i<=12; i++){
            if( i == 1 ) messageStorage.append("["+br.readLine()+"]\n");
            if( i == 2 ) messageStorage.append(thisMain.printScoreLine(br.readLine())+"\n");
            if( i > 2  ) messageStorage.append((i-2)+". "+br.readLine()+"\n");
        }

        System.out.println(messageStorage.toString());
    }

    private String printScoreLine(String scoreLine){
        float score         =  Float.valueOf(scoreLine);
        int   intScore      = (int)score;
        float percentScore  = (float)(intScore*100/5); 

        return "별점 : "+(int)score+" ("+percentScore+"%)";   
    }
}
