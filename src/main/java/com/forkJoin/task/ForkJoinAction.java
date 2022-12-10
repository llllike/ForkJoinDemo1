package com.forkJoin.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;

public class ForkJoinAction extends RecursiveAction {
    // 当前搜索的目录
    private File file;
    // 需要模糊查询字符串
    private String str;
    public ForkJoinAction(File file, String str){
        this.file=file;
        this.str=str;
    }
    @Override
    protected void compute() {
        if(file==null){
            return;
        }
        if (file.isFile()){
            if (file.getName().contains(str)){
                System.out.println(file.getAbsolutePath());
            }
            return;
        }
        if (file.isDirectory()){
            List<ForkJoinAction> list=new ArrayList<>();
            File[] files = file.listFiles();
            if (files!=null){
                for (File listFile : files) {
                    ForkJoinAction forkJoinAction = new ForkJoinAction(listFile,str);
                    list.add(forkJoinAction);
                }
                if (list.size() > 0) {
                    Collection<ForkJoinAction> findFiles = invokeAll(list);
                    for (ForkJoinAction findFiles1 : findFiles) {
                        //等待所有的任务执行完成
                        findFiles1.join();
                    }
                }
            }

        }
    }
}
