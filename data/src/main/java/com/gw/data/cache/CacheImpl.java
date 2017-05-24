package com.gw.data.cache;

import android.content.Context;

import com.gw.data.entity.TransactionItemEntity;
import com.gw.data.exception.CacheReadException;
import com.gw.domain.executor.ThreadExecutor;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link Cache} implementation.
 */
@Singleton
public class CacheImpl implements Cache {

    private static final String SETTINGS_FILE_NAME = "com.gw.SETTINGS";
    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";

    private static final String DEFAULT_FILE_NAME = "transaction_list";
    private static final long EXPIRATION_TIME =  1000 * 60 * 60 ;

    private final Context context;
    private final File cacheDir;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    /**
     * Constructor of the class {@link CacheImpl}.
     *
     * @param context     A
     * @param fileManager {@link FileManager} for saving serialized objects to the file system.
     */
    @Inject
    CacheImpl(Context context,
              FileManager fileManager, ThreadExecutor executor) {
        if (context == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }

    @Override
    public Observable<List<TransactionItemEntity>> get(String fileName) {
        return Observable.create(emitter -> {
            final File transactionItemEntityFile = CacheImpl.this.buildFile(fileName);
            final Object fileContent = CacheImpl.this.fileManager.readObjectFromFile(transactionItemEntityFile);
            final List<TransactionItemEntity> transactionItemEntityList =
                    (List<TransactionItemEntity>) fileContent;

            if (transactionItemEntityList != null) {
                emitter.onNext(transactionItemEntityList);
                emitter.onComplete();
            } else {
                emitter.onError(new CacheReadException());
            }
        });
    }

    @Override
    public void put(List<TransactionItemEntity> transactionItemEntityList, String fileName) {
        if (transactionItemEntityList != null) {
            final File transactionItemEntityFile = this.buildFile(fileName);
            this.executeAsynchronously(new CacheWriter(this.fileManager, transactionItemEntityFile, transactionItemEntityList));
            setLastCacheUpdateTimeMillis();

        }
    }

    @Override
    public boolean isCached(String fileName) {
        final File userEntityFile = this.buildFile(fileName);
        return this.fileManager.exists(userEntityFile);
    }

    @Override
    public boolean isExpired(String fileName) {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }

    @Override
    public void evictAll() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
    }

    /**
     * Build a file, used to be inserted in the disk cache.
     *
     * @return A valid file.
     */
    private File buildFile(String fileName) {
        final StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(fileName);

        return new File(fileNameBuilder.toString());
    }

    /**
     * Set in millis, the last time the cache was accessed.
     */
    private void setLastCacheUpdateTimeMillis() {
        final long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    /**
     * Executes a {@link Runnable} in another Thread.
     *
     * @param runnable {@link Runnable} to execute
     */
    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }


    /**
     * {@link Runnable} class for writing to disk.
     */
    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final Object objectToWrite;

        CacheWriter(FileManager fileManager, File fileToWrite, Object objectToWrite) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.objectToWrite = objectToWrite;
        }

        @Override
        public void run() {
            this.fileManager.writeObjectToFile(fileToWrite, objectToWrite);
        }
    }

    /**
     * {@link Runnable} class for evicting all the cached files
     */
    private static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }

        @Override
        public void run() {
            this.fileManager.clearDirectory(this.cacheDir);
        }
    }
}
