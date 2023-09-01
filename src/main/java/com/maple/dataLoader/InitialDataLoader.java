package com.maple.dataLoader;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataLoader {

    /**
     *
     * TODO 서버 만료 전 데이터 추가 구현 필요
     * @param
     * @return
     *
     *
     */
/*    @Bean
    public CommandLineRunner loadData(QuestionRepository questionRepository) {
        return new DataLoader(questionRepository);
    }*/

    /*public static class DataLoader implements CommandLineRunner {
        private final QuestionRepository questionRepository;

        public DataLoader(QuestionRepository questionRepository) {
            this.questionRepository = questionRepository;
        }

        @Override
        @Transactional
        public void run(String... args){
            String[] questions = {
                    "훈련병이 된지 1주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련병이 된지 2주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련병이 된지 3주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련병이 된지 4주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련병이 된지 5주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련병이 된지 6주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련병이 된지 7주 째입니다. 해주고 싶은 말이 있나요?",
                    "훈련소 첫날입니다! 저에게 응원의 메세지를 남겨주세요!",
                    "앞으로 남은 군생활동안 제가 힘을 낼 수 있게 응원의 메세지를 남겨주세요!",
                    "입대 전 저는 어떤 사람이었나요?",
                    "제가 전역하면 저와 어떤 일들을 하고 싶나요?",
                    "저의 가장 큰 장점은 무엇이라고 생각하나요?",
                    "저와의 추억 중 가장 기억에 남는 추억은 무엇인가요?",
                    "제 첫 인상에 대해 자세하게 말해주세요!",
                    "저와 함께 가장 웃었던 순간은 언제인가요?",
                    "저와의 우정을 한 마디로 표현한다면?",
                    "제가 휴가 나가면 같이 무엇을 하고 싶나요?",
                    "전역하고 저와 하고 싶은 것들은 무엇인가요?",
                    "저에게 고마웠던 경험이 있나요?",
                    "저랑 가고 싶은 여행지가 있나요?",
                    "저와 가장 어울리는 계절은 무엇인가요? 이유도 적어주세요!",
                    "제가 어느 날 갑자기 바퀴벌레가 된다면 어떻게 하실 건가요?",
                    "저를 알게 된 이후 좋았던 점, 나빴던 점이 있으면 말해주세요!",
                    "저가 훈련소에 입대 했을 때, 당신은 어떤 생각이나 감정을 느꼈나요?",
                    "저에게 전하고 싶은 가장 중요한 조언이나 메시지가 있다면 무엇인가요?",
                    "저와의 관계에서 가장 값진 것은 무엇이라고 생각하나요?",
                    "저와 함께 보냈던 시간 중, 제가 당신에게 변화나 영향을 준 것은 무엇인가요?",
                    "저에게 가장 고마웠던 순간이나 일이 있다면 무엇인가요?",
                    "저와 함께 했던 활동 중, 가장 의미 있었던 활동은 무엇이었나요?",
                    "저와의 관계에서 가장 기억에 남는 대화나 대화 주제는 무엇인가요?",
                    "저한테 비밀로 한 이야기가 있을까요? 있다면 말해주세요!",
                    "당신이 생각하는 저의 흑역사는 무엇인가요?",
                    "저와 가장 잘 어울리는 노래가 무엇인가요?",
                    "저와 함께하면서 가장 특별했던 날이 언제였나요?",
                    "저의 별명을 만들어주세요! 이유도 작성해주세요!",
                    "저에게 돈을 빌려준다면 얼마까지 빌려줄 수 있나요?",
                    "저는 당신에게 어떤 존재인가요?",
                    "만약 제가 아이돌 오디션을 보러 간다고 한다면 당신의 반응은?",
                    "저의 어떤점이 제일 좋나요?",
                    "만약 제가 군대에 말뚝을 박는다고 하면 어떻게 하실 건가요?",
                    "요즘 당신의 기분을 상태 메세지로 표현한다면?",
                    "최근 당신의 근황을 훈련병에게 적어주세요!",
                    "저는 당신의 연락처에 무엇으로 저장되어 있나요?",
                    "오늘은 아무말 대잔치에요. 저에게 아무말이나 해주세요!",
                    "당신의 최근에 어떤 고민을 갖고 있나요??",
                    "저랑 닮은 동물은 무엇인가요?",
                    "저에 대한 칭찬을 적어주세요!",
                    "나와 함께 한 달 동안 무인도에 가게 된다면?",
                    "질문을 보고 있는 당신! 당신은 저에게 어떠한 존재가 되고 싶으신가요?"
            };

            for (String questionContent : questions) {
                Question question = new Question();
                question.setQuestionContent(questionContent);
                questionRepository.save(question);
            }
        }
    }*/
}
