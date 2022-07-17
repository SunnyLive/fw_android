package com.fengwo.module_login.mvp.presenter;

import com.fengwo.module_comment.base.BaseListDto;
import com.fengwo.module_comment.base.HttpResult;
import com.fengwo.module_comment.base.LoadingObserver;
import com.fengwo.module_login.mvp.dto.HuafenLevelDto;
import com.fengwo.module_login.mvp.dto.MyPrivilegeDto;
import com.fengwo.module_login.mvp.dto.MyTaskDto;
import com.fengwo.module_login.mvp.dto.PrivilegeDto;
import com.fengwo.module_login.mvp.dto.TaskDto;
import com.fengwo.module_login.mvp.ui.iview.IHuafenView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class HuafenPresenter extends BaseLoginPresenter<IHuafenView> {

    public void getUserHuafenLevel() {
        service.getHuafenLevel()
                .compose(io_main())
                .compose(handleResult())
                .subscribe(new LoadingObserver<HuafenLevelDto>() {
                    @Override
                    public void _onNext(HuafenLevelDto data) {
                        getView().setLevle(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });

    }

    public void getUserTask() {
        Flowable allTask = service.getAllTask();
        Flowable myTask = service.getMyTask();
        Flowable.zip(allTask, myTask, new BiFunction<HttpResult<List<TaskDto>>, HttpResult<List<MyTaskDto>>, List<TaskDto>>() {
            @Override
            public List<TaskDto> apply(HttpResult<List<TaskDto>> taskDtos, HttpResult<List<MyTaskDto>> myTaskDtos) throws Exception {
                List<TaskDto> result = new ArrayList<>();
                List<TaskDto> allTask = taskDtos.data;
                List<MyTaskDto> myTask = myTaskDtos.data;
                for (int i = 0; i < allTask.size(); i++) {
                    for (int j = 0; j < myTask.size(); j++) {
                        int allId = allTask.get(i).id;
                        int myId = myTask.get(j).id;
                        if (allId == myId) {
                            TaskDto dto = allTask.get(i);
                            MyTaskDto myTaskDto = myTask.get(i);
                            String name = "";
                            if (myTaskDto.taskNum == 0) {
                                name = dto.taskName;
                            } else
                                name = dto.taskName + "(" + myTaskDto.useNum + "/" + myTaskDto.taskNum + ")";
                            dto.taskName = name;
                            result.add(dto);
                            break;
                        }
                    }
                }
                return result;
            }
        }).compose(io_main())
                .subscribe(new LoadingObserver<List<TaskDto>>() {
                    @Override
                    public void _onNext(List<TaskDto> data) {
                        getView().setTaskList(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }

    public void getUserPrivilege() {
        Flowable allPrivilege = service.getPrivilege();
        Flowable myPrivilege = service.getMyPrivilege();
        Flowable.zip(allPrivilege, myPrivilege, new BiFunction<HttpResult<List<PrivilegeDto>>, HttpResult<MyPrivilegeDto>, List<PrivilegeDto>>() {
            @Override
            public List<PrivilegeDto> apply(HttpResult<List<PrivilegeDto>> listHttpResult, HttpResult<MyPrivilegeDto> myPrivilegeDtoHttpResult) throws Exception {
                List<PrivilegeDto> result = listHttpResult.data;
                String[] myPrivilege = myPrivilegeDtoHttpResult.data.privilege.split(",");
                for (int i = 0; i < result.size(); i++) {
                    for (int j = 0; j < myPrivilege.length; j++) {
                        if (result.get(i).id == Integer.parseInt(myPrivilege[j])) {
                            result.get(i).isHas = true;
                            break;
                        }
                    }
                }
                return result;
            }
        }).compose(io_main())
                .subscribe(new LoadingObserver<List<PrivilegeDto>>() {
                    @Override
                    public void _onNext(List<PrivilegeDto> data) {
                        getView().setPrivelege(data);
                    }

                    @Override
                    public void _onError(String msg) {

                    }
                });
    }
}
