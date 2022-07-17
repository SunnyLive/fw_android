package com.fengwo.module_comment;

import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.utils.UploadHelper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PublicService {
    @GET("api/file/token")
    Observable<HttpResult<UploadHelper.OssTokenDto>> getOssToken(@Query("contentType") String contentType);
}
