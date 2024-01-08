package com.beechannel.live.domain.dto;

import com.beechannel.live.domain.po.Live;
import com.beechannel.live.domain.po.LiveInfo;
import com.beechannel.live.domain.po.SuperviseLicense;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description license result
 * @Author eotouch
 * @Date 2024/01/05 11:47
 * @Version 1.0
 */
@Data
public class LicenseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private SuperviseLicense superviseLicense;

    private LiveInfo liveInfo;

    private Live live;
}
