package io.github.xxyopen.novel.test;


import com.github.benmanes.caffeine.cache.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class TestCaffeine {
    public static void main(String[] args) {

        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .build();


        String yue = cache.getIfPresent("yue");
        System.out.println("cache.getIfPresent:" + yue);

        String s = cache.get("yue", k -> "wang");
        System.out.println("cache.get:" + s);

        cache.put("yue", "zhuang");
        System.out.println("cache.put:" + cache.getIfPresent("yue"));



        cache.invalidate("yue");
        System.out.println("cache.invalidate:" + cache.getIfPresent("yue"));


        LoadingCache<String, Integer> cache2 = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .build( string -> {return 3600;});


        Integer yue1 = cache2.getIfPresent("yue2");
        System.out.println("cache2.getIfPresent:" + yue1);

        Integer i = cache2.get("yue2");
        System.out.println("cache2.get:" + i);

        Integer yue2 = cache2.get("yue2", k -> 45645);
        System.out.println("cache2.get:" + yue2);

        cache2.put("yue2", 1234);
        System.out.println("cache2.put:" + cache2.getIfPresent("yue2"));

        cache.invalidate("yue2");
        System.out.println("cache2.invalidate:" + cache2.getIfPresent("yue2"));


        AsyncCache<String, String> asyncCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .buildAsync();


        CompletableFuture<String> asyn = asyncCache.getIfPresent("asyncCache");
        System.out.println("asyncCache.getIfPresent:" + asyn);

        CompletableFuture<String> asyn2 = asyncCache.get("asyncCache", k -> "wang");
        System.out.println("asyncCache.get:" + asyn2.join());

        CompletableFuture<String> asyn3 = new CompletableFuture<>();
        asyn3.complete("123");
        asyncCache.put("asyncCache",asyn3);
        System.out.println("asyncCache.put:" + asyncCache.getIfPresent("asyncCache").join());

        asyncCache.synchronous().invalidate("asyncCache");
        System.out.println("asyncCache.invalidate:" + asyncCache.getIfPresent("asyncCache"));


        AsyncLoadingCache<String, String> AsyncLoadingCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .buildAsync(string -> "wang");

        CompletableFuture<String> zhnogteng = AsyncLoadingCache.getIfPresent("zhnogteng");
        System.out.println("AsyncLoadingCache.getIfPresent:" + zhnogteng);

        CompletableFuture<String> zhnogteng2 = AsyncLoadingCache.get("zhnogteng");
        System.out.println("AsyncLoadingCache.getIfPresent:"+ zhnogteng2.join());

        CompletableFuture<String> CompletableFuture = new CompletableFuture<>();
        CompletableFuture.complete("zhongteng123");

        System.out.println("AsyncLoadingCache.put:"+ AsyncLoadingCache.getIfPresent("zhnogteng").join());

        AsyncLoadingCache.synchronous().invalidate("zhnogteng");
        System.out.println("AsyncLoadingCache.put:"+ AsyncLoadingCache.getIfPresent("zhnogteng").join());





    }
}
