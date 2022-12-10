## 如果内容对你有帮助的话，点一个免费的star吧，非常感谢!
# 前言

ForkJoin框架

> - 分割原问题；
> - 求解子问题；
> - 合并子问题的解为原问题的解。

ForkJoin框架解决的是分治问题

主要分为两类，有返回值和无返回值

# 1、有返回值

下面是例子是计算从start到end累加和

## 任务类：

```java
import java.util.concurrent.RecursiveTask;
public class ForkJoinCalculate extends RecursiveTask<Long>{
	private long start;
	private long end;
	private static final long THRESHOLD = 100000L;
	//临界值
	public ForkJoinCalculate(long start, long end) {
		this.start = start;
		this.end = end;
	}
	@Override
	protected Long compute() {
		long length = end - start;
		if(length <= THRESHOLD){
			long sum = 0;
			for (long i = start; i <= end; i++) {
				sum += i;
			}
			return sum;
		}else{
			long middle = (start + end) / 2;
			ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
			//拆分，并将该子任务压入线程队列
			left.fork();
			ForkJoinCalculate right = new ForkJoinCalculate(middle+1, end);
			right.fork();
			return left.join() + right.join();
		}
	}
}
```

当start和end之间的差值大于阈值时

就选择从中间拆分成两个子任务

当start和end之间的差值小于阈值时

就直接计算

## 测试结果：

```java
@Test
	public void test1(){
		long start = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask<Long> task = new ForkJoinCalculate(0L, 100000000000L);
		long sum = pool.invoke(task);
		long end = System.currentTimeMillis();
		long sum1=0;
		for (long i = 0; i < 100000000000L; i++) {
			sum1+=i;
		}
		long end2=System.currentTimeMillis();
		System.out.println(sum1);
		System.out.println(sum);
		//耗费的时间为: 8840
		//耗费的时间为: 28091
		System.out.println("耗费的时间为: " + (end - start));
		System.out.println("耗费的时间为: " + (end2 - end));
	}
```

可以看到，耗费的时间分别是8840和28091毫秒

差了三倍

# 2、无返回值

下面是例子是从文件夹里面搜索文件

## 任务类：

```java
public class ForkJoinAction extends RecursiveAction {
    // 当前搜索的目录
    private File file;
    // 需要模糊查询字符串
    private String str;
    public ForkJoinAction(File file,String str){
        this.file=file;
        this.str=str;
    }
    @Override
    protected void compute() {
        if (file.isDirectory()){
            List<ForkJoinAction> list=new ArrayList<>();
            for (File listFile : file.listFiles()) {
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
        }else {
            if (file==null){
                return;
            }
            if (file.getName().contains(str)){
                System.out.println(file.getAbsolutePath());
            }
        }
    }
}
```



## 测试结果：

```java
@Test
	public void test5(){
		ForkJoinAction forkJoinAction = new ForkJoinAction(new File("D:"), "java");
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		//execute方法是异步的
		forkJoinPool.execute(forkJoinAction);
		//阻塞，等待ForkJoin执行完，主线程才往下执行
		forkJoinAction.join();
	}
```

可以看到从D盘根目录扫描“java”

只耗费了15毫秒

![Snipaste_2022-12-10_13-07-24.png](https://hhuhahaha.oss-cn-hangzhou.aliyuncs.com/img/Snipaste_2022-12-10_13-07-24.png)3、代码下载链接

阿里云OSS：https://hhuhahaha.oss-cn-hangzhou.aliyuncs.com/code/ForkJoinDemo1.zip

Github: https://github.com/llllike/ForkJoinDemo1

Gitee:https://gitee.com/jk_2_yu/ForkJoinDemo1
