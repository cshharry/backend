package com.woorifisa.backend.test;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woorifisa.backend.common.dto.MemberDTO;
import com.woorifisa.backend.common.dto.SubscriptionDTO;
import com.woorifisa.backend.common.entity.Member;
import com.woorifisa.backend.common.repository.SubscriptionRepository;

import jakarta.transaction.Transactional;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    MemberRepository2 repository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    private ModelMapper mapper = new ModelMapper();

    public MemberDTO login(Map<String, String> reqMap) throws LoginException {
        Member member = repository.findByMemId(reqMap.get("id")).orElse(null);
        System.out.println(member);
        if (member != null) {
            if (reqMap.get("pw").equals(member.getMemPw())) {
                MemberDTO dto = mapper.map(member, MemberDTO.class);
                System.out.println(dto);
                return dto;
            } else {
                throw new LoginException("비밀번호가 다릅니다.");
            }
        } else {
            throw new LoginException("존재하지 않는 아이디 입니다.");
        }
    }

    @Override
    @Transactional
    public String updateMem(Map<String, String> reqMap) {
        int result = repository.updateByMemnumMemId(reqMap.get("mem_num"), reqMap.get("mem_id"));
        if (result != 1) {
            return "수정 실패";
        }
        return "수정 성공";
    }

    @Override
    @Transactional
    public String insertMem(MemberDTO dto) {
        int result = repository.insertMem(dto.getMemName(), dto.getMemId(), dto.getMemPw(), dto.getMemEmail(),
                dto.getMemPhone(), dto.getMemSex(), dto.getMemAddr(), dto.getMemBirth(), dto.getMemType());

        if(result == 1){
            return "insert success";
        }
        return "insert fail";
    }

    @Transactional
    @Override
    public String insertSub(SubscriptionDTO dto)   {
        try {
            subscriptionRepository.insertSub(dto.getSubPer(),dto.getSubStart(), dto.getSubDeli(), dto.getSubStat(), dto.getSubUpd(), dto.getSubCnt(), dto.getMemNum(), dto.getProdNum(), dto.getPayNum());
        } catch (Exception e) {
            e.printStackTrace();
            return "실패" + e.getMessage();
        }
        
        return "성공";
    }
}
