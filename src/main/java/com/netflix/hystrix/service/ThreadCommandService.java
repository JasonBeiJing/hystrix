package com.netflix.hystrix.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.entity.MarketingEntiy;
import com.netflix.hystrix.service.command.MarketingServiceOneGetByIdCommand;

import rx.Observable;
import rx.Observer;

@Service
public class ThreadCommandService {
	
	public MarketingEntiy getByIdWithCommand(Long id) throws InterruptedException, ExecutionException {
		HystrixCommand<MarketingEntiy> command = new MarketingServiceOneGetByIdCommand(id);
		/**
		 * 同步调用
		 */
		// return command.execute();
		
		/**
		 * 异步调用,前提是采用了线程隔离（THREAD），而不是信号隔离（SEMAPHORE）。如果为SEMAPHORE，那么还是当前主线程，阻塞，同步调用
		 */
		//Future<MarketingEntiy> future = command.queue();
		//闲着没事干点别的事情去，一会再回来取结果
		//return future.get();
		
		/**
		 * 在调用的时候就开始执行对应的指令, mode = EAGER
		 */
		Observable<MarketingEntiy> hotObservable = command.observe();
		hotObservable.subscribe(new Observer<MarketingEntiy>() {
			@Override
			public void onCompleted() {
				System.err.println("====HOT====");
			}

			@Override
			public void onError(Throwable e) {
				System.err.println("*********HOT*********");
				e.printStackTrace();
			}

			@Override
			public void onNext(MarketingEntiy t) {
				System.out.println("--HOT--->" + t.getName());
			}

		});
		return null;
		
		/**
		 * observe()方法的lazy版本，当我们去subscribe的时候，对应的指令才会被执行并产生结果,  mode = LAZY
		 */
//		Observable<MarketingEntiy> coldObservable = command.toObservable();
//		coldObservable.subscribe(new Observer<MarketingEntiy>() {
//			@Override
//			public void onCompleted() {
//				System.err.println("====COLD====");				
//			}
//
//			@Override
//			public void onError(Throwable arg0) {
//				System.err.println("*********COLD*********");
//				arg0.printStackTrace();
//			}
//
//			@Override
//			public void onNext(MarketingEntiy arg0) {
//				System.out.println("--COLD--->" + arg0.getName());				
//			}
//		});
//		return null;
		
		//基于［发布-订阅］响应式的调用，本质上是观察者模式的一种具体实现。
		//HystrixCommand的queue方法实际上是调用了toObservable().toBlocking().toFuture()，而execute方法实际上是调用了queue().get()，而get()是阻塞的，也就变成了同步。
	}
}
