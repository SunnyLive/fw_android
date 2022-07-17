package com.fengwo.mobile.android;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import java.util.ArrayList;

public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MApp.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        ArrayList<String> tags = new ArrayList<>();
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("333343568", "9323461ed3174bada22c6fccddd1ff4c", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQD0v6YYUU9imEjllMpqGXJv1VUkpKKZIXesKSAlQFXudB8YMfiaryiHRafkFxowiBYkToJ8YXZJjWPksFRwg0Ttfh2QcHe4qekoRQ80nP5hi7yfq5NAhw9l1gIl8R3IxuMv6l7WJSAJbwbhRSMWhpAfwmE6NylVdxJtshz4k/WYoXwA8sRaByOlnV1wKpUUqnmqQIc6W8i/2a3e0n+/WIeZwb7+QmI9E0DDymBamhkHF996i3j+XtkZMm5LPozUIoVgnZLaKBhYfcRNF6t+vlsAwjL8gTWDxaXhox8OiKAM4o96INnJTA+7oVrZNgkDABMulZzbGfkZGtNkE7sCkb7RAgMBAAECggEADL0I1Q0uV5Bbgu96lYkbhqyQ0OZsvBGc2+4MCURO9KlAkQ7QxKRHNLswYNO+/jWnCV64teaQtIVSZK20x2JdrQq6WTExWgAl7X2iq/db2hHe0GSqtloROBiqZfUlurR3UKAfLYAjKbqIniLuD5cTW0PPsar7UMJwyBrUHS/+QWJUNc1h4z4h+T7THDNlb/St+FD/xynq34EgUBT4puHmc9ixBxoRWUdB0zroutrSSERYrLz/7fKnWGxCBPDc1ZhJJTNnzYsyVDqtC4W5cTgDcAQG0IpaJbCG3Fv9PugNVmVKwR63ZCA9/Rg5gvTwMjdSUVVEylFC3c43pPMd9g9nXQKBgQD+BQ6ibonlHavbhsBFq6EtoFQXEHgl9dlOI22VNcNehDD2CbqORn2JCXRg/9ydhev+Xwkab9lx5/LEr/jwrbgHNJ1LzFvLRs3yH8JxS5YltXLlq7TDBB7fyHWc0UiwcU1vcyEYYOf1GfHzihin/9urR5Bhoo5ZECnpfG6ktqHF8wKBgQD2qBbj9KTa6NmKFyiXqIVcxoejHHpWDhKzP5z/3OxYT36OlK7SItHTddWDPMDZdlEb8PrGzpI3APIGuvPSJxUangmon9kRvyFHKsxpVhjF/awAy7hbYTSBIn7Rpr/hUHvliA1NKkas4ZzlO0R4HdJVsN1LdoivMLZDzcUkBIlFKwKBgQCCNaFszfDNi0oe+5HvpKy4MKkqMX0FKGK91RdFFwwQlP5sAEyqxaNv7XLEjiNOc+mmuJx3+sGhy937yKB47vOYbfX7dsPPDXpgFLudS7uplLtS05OTK7ePyrfPLNBv8YRaDs/2KSZ901DdH/rHkzXNNJ/kDGmfadOyJeCpehyu1QKBgQCPG6dsSxz7Be5xeE5M77Gwyl8X/AXet/uRlG765Ksb8cNz5kAoG5uyHMPsiOs7QLJ+i6p0fpXG4/2Ieh/M5nAQKXFhLJ089q/QIrzGwPVOqoeJTqmAGz3jTqhV63GYwKGh4vYUt1qaI6xsM57eMi8/GIbMqdFUE+csH88pvpgGUQKBgAQzT4Lh6avp7L+VDNnrsCEF8fAdvNr4V6MxeFtR/21aFHxi9j+pPU5LAQTT4oUkNKKbtE/pnnM1uNV1dyRKMmF9ZvbnUMswBW2jCOG9ten8mE2Inx/ADQdxi32eu+WM+Uik85BV/oeHz1V5cJoy+BBEnp/8A8ttVfgq7X9Ng9zo")
                .setEnableDebug(false)
                .setTags(tags)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }
}
