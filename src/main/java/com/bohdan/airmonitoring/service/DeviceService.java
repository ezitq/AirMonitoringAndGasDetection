package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.Device;
import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.DeviceJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeviceService {

    private DeviceJpaRepository deviceJpaRepository;

    @Autowired
    public DeviceService(DeviceJpaRepository deviceJpaRepository) {
        this.deviceJpaRepository = deviceJpaRepository;
    }

    public Device pairDevice(User user, String pairingCode){

        Device device = deviceJpaRepository.findDeviceByPairingCode(pairingCode);

        if(device == null){ return null; }

        device.setPaired(true);
        device.setOwner(user);

        user.addNewDevice(device);

        return device;
    }
}
