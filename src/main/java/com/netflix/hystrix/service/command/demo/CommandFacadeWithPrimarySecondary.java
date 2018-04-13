package com.netflix.hystrix.service.command.demo;
//package com.jd.marketing.hystrix.service.command.demo;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.netflix.config.ConfigurationManager;
//import com.netflix.config.DynamicBooleanProperty;
//import com.netflix.config.DynamicPropertyFactory;
//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixCommandGroupKey;
//import com.netflix.hystrix.HystrixCommandKey;
//import com.netflix.hystrix.HystrixCommandProperties;
//import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
//import com.netflix.hystrix.HystrixThreadPoolKey;
//import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
//
//public class CommandFacadeWithPrimarySecondary extends HystrixCommand<String> {
//	private final static String FLAG = "primarySecondary.usePrimary";
//	
//	private final static DynamicBooleanProperty usePrimary = DynamicPropertyFactory.getInstance().getBooleanProperty(FLAG, true);
//
//	private final int id;
//
//	public CommandFacadeWithPrimarySecondary(int id) {
//		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SystemX")).andCommandKey(HystrixCommandKey.Factory.asKey("PrimarySecondaryCommand"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)));
//		this.id = id;
//	}
//
//	@Override
//	protected String run() throws Exception {
//		if (usePrimary.get()) {
//			return new PrimaryCommand(id).execute();
//		} else {
//			return new SecondaryCommand(id).execute();
//		}
//	}
//
//	@Override
//	protected String getFallback() {
//		return "static-fallback-" + id;
//	}
//
//	private static class PrimaryCommand extends HystrixCommand<String> {
//		private final int id;
//
//		private PrimaryCommand(int id) {
//			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SystemX"))
//					.andCommandKey(HystrixCommandKey.Factory.asKey("PrimaryCommand"))
//					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("PrimaryCommand"))
//					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(600)));
//			this.id = id;
//		}
//
//		@Override
//		protected String run() {
//			// perform expensive 'primary' service call
//			return "responseFromPrimary-" + id;
//		}
//	}
//
//	private static class SecondaryCommand extends HystrixCommand<String> {
//		private final int id;
//
//		private SecondaryCommand(int id) {
//			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SystemX"))
//					.andCommandKey(HystrixCommandKey.Factory.asKey("SecondaryCommand"))
//					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("SecondaryCommand"))
//					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(100)));
//			this.id = id;
//		}
//
//		@Override
//		protected String run() {
//			// perform fast 'secondary' service call
//			return "responseFromSecondary-" + id;
//		}
//	}
//
//	public static class UnitTest {
//		private HystrixRequestContext context;
//		
//		@Before
//		public void before() {
//			context = HystrixRequestContext.initializeContext();
//		}
//		
//		@After
//		public void after() {
//			if(context!=null) {				
//				context.shutdown();
//			}
//			ConfigurationManager.getConfigInstance().clear();
//		}
//		
//		@Test
//		public void testPrimary() {
//			// 将属性"primarySecondary.usePrimary"设置为true，则走PrimaryCommand；设置为false，则走SecondaryCommand
//			ConfigurationManager.getConfigInstance().setProperty(FLAG, true);
//			assertEquals("responseFromPrimary-20", new CommandFacadeWithPrimarySecondary(20).execute());
//		}
//
//		@Test
//		public void testSecondary() {
//			// 将属性"primarySecondary.usePrimary"设置为true，则走PrimaryCommand；设置为false，则走SecondaryCommand
//			ConfigurationManager.getConfigInstance().setProperty(FLAG, false);
//			assertEquals("responseFromSecondary-30", new CommandFacadeWithPrimarySecondary(30).execute());
//		}
//	}
//
//}
