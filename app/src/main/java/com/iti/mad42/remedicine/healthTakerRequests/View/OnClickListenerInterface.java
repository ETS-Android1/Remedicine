package com.iti.mad42.remedicine.healthTakerRequests.View;

import com.iti.mad42.remedicine.data.pojo.RequestPojo;

public interface OnClickListenerInterface {
    void onClickAcceptBtn(RequestPojo request);
    void onClickRejectBtn(RequestPojo request);
}
