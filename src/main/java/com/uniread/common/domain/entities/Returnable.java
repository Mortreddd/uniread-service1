package com.uniread.common.domain.entities;

public interface Returnable<T extends ApprovalRequest> {

    void returnItem(T request);

}
