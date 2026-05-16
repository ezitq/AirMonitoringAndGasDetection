package com.bohdan.airmonitoring.repository;

import com.bohdan.airmonitoring.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceJpaRepository extends JpaRepository<Device, Integer> {

    Device findDeviceByDeviceIdAndDeviceToken(String deviceId, String deviceToken);

    Device findDeviceByPairingCode(String pairingCode);
}
