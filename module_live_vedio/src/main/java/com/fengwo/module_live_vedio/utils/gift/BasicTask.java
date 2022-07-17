package com.fengwo.module_live_vedio.utils.gift;

public abstract class BasicTask implements ITask {
 
    // 默认优先级。
    private Priority priority = Priority.DEFAULT;
 
    @Override
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
 
    @Override
    public Priority getPriority() {
        return priority;
    }

    // 做优先级比较。
    @Override
    public int compareTo(ITask another) {
        final Priority me = this.getPriority();
        final Priority it = another.getPriority();
        return me == it ?  this.getSequence() - another.getSequence() :
                it.ordinal() - me.ordinal();
    }
}