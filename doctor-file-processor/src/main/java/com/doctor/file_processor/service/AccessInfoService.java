package com.doctor.file_processor.service;

import com.doctor.file_processor.domain.entity.AccessInfo;
import com.doctor.file_processor.repositories.AccessInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AccessInfoService {
    private final AccessInfoRepository accessInfoRepository;

    public AccessInfoService(AccessInfoRepository accessInfoRepository) {
        this.accessInfoRepository = accessInfoRepository;
    }

    public synchronized AccessInfo createEntry(String remoteIp) {
        AccessInfo newAccInfo = AccessInfo.builder()
                .created(new Date())
                .ip(remoteIp)
                .numOfCallsLastMinute(1L)
                .build();
        return accessInfoRepository.save(newAccInfo);
    }

    public synchronized void updateAccessInfo(String remoteIp) {
        Optional<AccessInfo> existingAccessInfo = accessInfoRepository.findById(remoteIp);

        existingAccessInfo.ifPresent((accessInfo) -> {
                accessInfo.setNumOfCallsLastMinute(accessInfo.getNumOfCallsLastMinute() + 1);
                accessInfoRepository.save(accessInfo);
            }
        );
    }

    public Iterable<AccessInfo> findAll() {
        return accessInfoRepository.findAll();
    }

    public void deleteAll() {
        accessInfoRepository.deleteAll();
    }

    public AccessInfo findByIp(String remoteIp) {
        return accessInfoRepository.findById(remoteIp).orElse(null);
    }
}
