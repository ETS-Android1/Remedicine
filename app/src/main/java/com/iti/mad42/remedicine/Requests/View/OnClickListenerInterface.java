package com.iti.mad42.remedicine.Requests.View;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;

public interface OnClickListenerInterface {
    void onClickAcceptBtn(RequestPojo request);
    void onClickRejectBtn(RequestPojo request);
}
