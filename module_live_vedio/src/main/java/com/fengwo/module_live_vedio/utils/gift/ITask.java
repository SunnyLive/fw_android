package com.fengwo.module_live_vedio.utils.gift;

public interface ITask extends Comparable<ITask> {
    void run();

    void setPriority(Priority priority);

    Priority getPriority();

    void setSequence(int sequence);

    int getSequence();
}