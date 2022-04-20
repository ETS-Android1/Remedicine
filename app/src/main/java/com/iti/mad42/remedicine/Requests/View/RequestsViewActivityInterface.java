package com.iti.mad42.remedicine.Requests.View;

import com.iti.mad42.remedicine.Model.pojo.RequestPojo;

import java.util.List;

public interface RequestsViewActivityInterface {
    public void getAllRequests(List<RequestPojo> requests);
}
