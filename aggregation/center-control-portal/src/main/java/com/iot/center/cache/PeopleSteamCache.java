package com.iot.center.cache;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.iot.center.bean.PeopleSteam;

public class PeopleSteamCache {
    private static Cache<String, PeopleSteam> peopleSteamCache = CacheBuilder.newBuilder()
            .maximumSize(100).expireAfterWrite(240, TimeUnit.HOURS)
            .recordStats()
            .build();

    public static PeopleSteam get(String key) throws ExecutionException {
    	PeopleSteam peopleSteam = peopleSteamCache.get(key, new Callable<PeopleSteam>() {
            @Override
            public PeopleSteam call() throws Exception {
                return new PeopleSteam();
            }
        });
        return peopleSteam;
    }

    public static void put(String key, PeopleSteam value) {
    	peopleSteamCache.put(key, value);
    }
}
