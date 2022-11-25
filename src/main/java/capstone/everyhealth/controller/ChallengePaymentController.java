package capstone.everyhealth.controller;

import capstone.everyhealth.controller.dto.Challenge.payment.ChallengeTransactionHistory;
import capstone.everyhealth.controller.dto.Challenge.payment.PaymentResultResponseForm;
import capstone.everyhealth.controller.dto.Challenge.payment.RequestAccessTokenForm;
import capstone.everyhealth.domain.challenge.Challenge;
import capstone.everyhealth.domain.challenge.ChallengeParticipant;
import capstone.everyhealth.domain.challenge.ChallengeTransaction;
import capstone.everyhealth.domain.stakeholder.Member;
import capstone.everyhealth.exception.challenge.ChallengeNotFound;
import capstone.everyhealth.exception.challenge.ChallengeParticipantNotFound;
import capstone.everyhealth.exception.stakeholder.MemberNotFound;
import capstone.everyhealth.service.ChallengeService;
import capstone.everyhealth.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@Api(tags = {"챌린지 결제 API"})
public class ChallengePaymentController {

    private final ChallengeService challengeService;
    private final MemberService memberService;
    @Value("${imp.key}")
    String impKey;
    @Value("${imp.secret}")
    String impSecret;

    @ApiOperation(
            value = "챌린지 결제",
            notes = "챌린지 결제를 진행한다.\n"
                    + "지금은 임시로 execute 하면\n\n"
                    + "Request URL\n"
                    + "http://15.164.168.230:8080/challenges/3/payments/members/1 처럼 생긴 링크 뜨는데\n"
                    + "거기 링크 들어가서 결제 버튼 누르면 결제 진행됩니다.\n"
                    + "환불 후 바로 재결제 안돼서 이미 결제 됐다고 뜨면 다른 id 값 쓰셔야합니다"
    )
    @GetMapping("/challenges/{challengeId}/payments/members/{memberId}")
    public String paymentPage(@ApiParam(value = "챌린지 id 값") @PathVariable Long challengeId,
                              @ApiParam(value = "멤버 id 값") @PathVariable Long memberId,
                              Model model) throws ChallengeNotFound, MemberNotFound {

        Challenge challenge = challengeService.find(challengeId);
        Member member = memberService.findMemberById(memberId);
        String orderNumber = createOrderNumber(challengeId, memberId);

        addDataToModel(model, challenge, member, orderNumber);

        return "payment";
    }

    @ResponseBody
    @PostMapping("/payments/result")
    @ApiIgnore
    public void saveChallengePaymentResult(@RequestBody PaymentResultResponseForm paymentResultResponseForm) throws JsonProcessingException, MemberNotFound, ChallengeNotFound, ChallengeParticipantNotFound {

        JSONObject accessToken = getAccessToken();
        JSONObject paymentData = getPaymentData(paymentResultResponseForm, accessToken);
        Long challengeId = paymentResultResponseForm.getChallenge_id();
        Long memberId = paymentResultResponseForm.getMember_id();

        log.info("imp_uid = {}", (String) paymentResultResponseForm.getImp_uid());
        log.info("imp_uid = {}", (String) paymentData.get("imp_uid"));

        //return paymentData;
        challengeService.saveChallengePaymentResult(paymentData, challengeId, memberId);
    }

    @ApiOperation(
            value = "챌린지 결제 취소",
            notes = "챌린지 성공 시 참가비를 환급한다."
    )
    @ResponseBody
    @GetMapping("/challenges/{challengeId}/payments-cancel/members/{memberId}")
    public String refund(@ApiParam(value = "챌린지 id 값") @PathVariable Long challengeId,
                         @ApiParam(value = "멤버 id 값") @PathVariable Long memberId) throws MemberNotFound, ChallengeParticipantNotFound, ChallengeNotFound {

        ChallengeParticipant challengeParticipant = challengeService.findChallengeParticipant(challengeId, memberId);
        ChallengeTransaction challengeTransaction = challengeParticipant.getChallengeTransaction();
        String merchantUid = challengeTransaction.getMerchantUid();

        JSONObject accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.valueOf(accessToken.get("access_token")));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("merchant_uid", merchantUid);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        String url = "https://api.iamport.kr/payments/cancel";

        new RestTemplate().exchange(url, HttpMethod.POST, request, JSONObject.class);

        challengeService.refund(challengeTransaction);

        return "환불 완료";
    }

    @ApiOperation(
            value = "챌린지 결제 내역 조회",
            notes = "멤버가 참여한 모든 챌린지 결제 내역을 조회한다.\n"
                    + "PAID : 결제 완료\n"
                    + "REFUNDED : 환급 완료"
    )
    @ResponseBody
    @GetMapping("/challenges/payments-history/members/{memberId}")
    public List<ChallengeTransactionHistory> findChallengeTransactionHistoryList(@ApiParam(value = "멤버 id 값") @PathVariable Long memberId) throws MemberNotFound, ChallengeNotFound, ChallengeParticipantNotFound {

        List<ChallengeTransactionHistory> challengeTransactionHistoryList = new ArrayList<>();
        List<ChallengeTransaction> challengeTransactionList = challengeService.findChallengeTransactionHistory(memberId);

        addChallengeTransactionHistoryList(challengeTransactionHistoryList, challengeTransactionList);

        return challengeTransactionHistoryList;
    }

    private void addChallengeTransactionHistoryList(List<ChallengeTransactionHistory> challengeTransactionHistoryList, List<ChallengeTransaction> challengeTransactionList) {
        for (ChallengeTransaction challengeTransaction : challengeTransactionList) {

            ChallengeTransactionHistory challengeTransactionHistory = createChallengeTransactionHistory(challengeTransaction);
            challengeTransactionHistoryList.add(challengeTransactionHistory);
        }
    }

    private ChallengeTransactionHistory createChallengeTransactionHistory(ChallengeTransaction challengeTransaction) {
        return ChallengeTransactionHistory.builder()
                .challengePaymentStatus(challengeTransaction.getChallengePaymentStatus())
                .challengeEndDate(challengeTransaction.getChallengeParticipant().getChallenge().getEndDate())
                .challengeName(challengeTransaction.getChallengeParticipant().getChallenge().getName())
                .challengeStartDate(challengeTransaction.getChallengeParticipant().getChallenge().getStartDate())
                .challengeStatus(challengeTransaction.getChallengeParticipant().getChallengeStatus())
                .build();
    }


    private String createOrderNumber(Long challengeId, Long memberId) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-"
                + (challengeId * 197632L + 1324211L)
                + "-"
                + (memberId * 178342L + 1452355L);
    }

    private void addDataToModel(Model model, Challenge challenge, Member member, String orderNumber) {
        model.addAttribute("challengeName", challenge.getName());
        model.addAttribute("challengePrice", challenge.getParticipationFee());
        model.addAttribute("participantName", member.getName());
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("challengeId", challenge.getId());
        model.addAttribute("memberId", member.getId());
    }

    private JSONObject getPaymentData(PaymentResultResponseForm PaymentResultResponseForm, JSONObject accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.valueOf(accessToken.get("access_token")));

        log.info("accessToken = {}", accessToken.get("access_token"));

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "https://api.iamport.kr/payments/" + PaymentResultResponseForm.getImp_uid();

        ResponseEntity<JSONObject> response = new RestTemplate()
                .exchange(url, HttpMethod.GET, request, JSONObject.class);

        log.info("== get complete ==");
        log.info("response = {}", response);
        log.info("====");

        LinkedHashMap hashMap = (LinkedHashMap) response.getBody().get("response");
        JSONObject jsonObject = new JSONObject();

        for (Object key : hashMap.keySet()) {
            jsonObject.put(key, hashMap.get(key));
        }

        return jsonObject;
    }

    private JSONObject getAccessToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        // @Builder 사용 이유와 사용법 정리 - https://myeongdev.tistory.com/14
        RequestAccessTokenForm requestAccessTokenForm = RequestAccessTokenForm.builder()
                .imp_key(impKey)
                .imp_secret(impSecret)
                .build();

        HttpEntity<RequestAccessTokenForm> request = new HttpEntity<>(requestAccessTokenForm, headers);

        String url = "https://api.iamport.kr/users/getToken";

        ResponseEntity<JSONObject> responseEntity = new RestTemplate()
                .postForEntity(url, request, JSONObject.class);

        log.info("AccessToken : {}", responseEntity.getBody().get("response"));

        LinkedHashMap hashMap = (LinkedHashMap) responseEntity.getBody().get("response");
        JSONObject jsonObject = new JSONObject();

        for (Object key : hashMap.keySet()) {
            jsonObject.put(key, hashMap.get(key));
        }

        log.info("hashmap to jsonObject = {}", jsonObject);

        // return (JSONObject) responseEntity.getBody().get("response"); 강제 형변환 에러나므로 위처럼 수동 변환하기
        return jsonObject;
    }
}
