package com.fengwo.module_login.mvp.dto;

import com.contrarywind.interfaces.IPickerViewData;


public class BankDto implements IPickerViewData {
    public String bankName;
    public String bankbankIcon;

    @Override
    public String getPickerViewText() {
        return bankName;
    }
}
