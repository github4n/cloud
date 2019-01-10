package com.iot.file;

import com.iot.common.util.CommonUtil;
import com.iot.common.util.JsonUtil;
import com.iot.file.api.FileUploadApi;
import com.iot.file.util.FileUtil;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileApplicationTests {

    private static Logger logger = LoggerFactory.getLogger(FileApplicationTests.class);

    @Autowired
    private FileUploadApi fileUploadApi;

    private ExecutorService executorService = Executors.newFixedThreadPool(50);

    private String filePath = "C:\\Users\\yeshiyuan\\Downloads\\aaa.mp4";


    @Test
    public void uploadBigFile() throws URISyntaxException, StorageException, InvalidKeyException, IOException {
        Long startTime = System.currentTimeMillis();


        FileInputStream fileInputStream = null;

        CloudBlockBlob blockBlob = getCloudBlockContainer().getBlockBlobReference("11.pdf");//希望上传后文件的名称
        //本地文件的位置
        try {
            // Open the file
            fileInputStream = new FileInputStream(filePath);
            // Split the file into 32K blocks (block size deliberately kept small for the demo) and upload all the blocks
            int blockNum = 0;
            String blockId = null;
            String blockIdEncoded = null;
            ArrayList<BlockEntry> blockList = new ArrayList<BlockEntry>();
            long uploadSize = 32 * 1024;
            while (fileInputStream.available() > (uploadSize)) {
                Long s = System.currentTimeMillis();
                blockId = String.format("%05d", blockNum);
                blockIdEncoded = Base64.getEncoder().encodeToString(blockId.getBytes());
                blockBlob.uploadBlock(blockIdEncoded, fileInputStream, uploadSize);
                blockList.add(new BlockEntry(blockIdEncoded));
                blockNum++;
                Long e = System.currentTimeMillis();
                logger.info("blockNum: consume {}ms", (e-s));
            }
            System.out.println("blockNum:" + blockNum);
            blockId = String.format("%05d", blockNum);
            blockIdEncoded = Base64.getEncoder().encodeToString(blockId.getBytes());
            blockBlob.uploadBlock(blockIdEncoded, fileInputStream, fileInputStream.available());
            blockList.add(new BlockEntry(blockIdEncoded));

            // Commit the blocks
            blockBlob.commitBlockList(blockList);
        }
        catch (Throwable t) {
            throw t;
        }
        finally {
            // Close the file output stream writer
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        Long endTime = System.currentTimeMillis();
        logger.info("总共耗时：{} ms", (endTime - startTime) );
    }

    @Test
    public void uploadTest() {
        File file = new File(filePath);
        String str = fileUploadApi.uploadFile(FileUtil.toMultipartFile(file),1L);
        System.out.println(str);
    }

    @Test
    public void UploadLargeSize() throws URISyntaxException, StorageException, IOException, InvalidKeyException{
        Long startTime = System.currentTimeMillis();
        File source  = new File(filePath);
        String blobName = source.getName();
        FileInputStream inputStream = new FileInputStream(source);
        final int blockLength = 64*1024;

        int blockCount = (int)(source.length() / blockLength) + 1;
        System.out.println("Total block count："+blockCount+", Total size: "+source.length());
        int currentBlockSize = 0;

        CloudBlobContainer container = getCloudBlockContainer();
        CloudBlockBlob blockBlobRef  = container.getBlockBlobReference(blobName);

        System.out.println("===============Begin Uploading===============");
        ArrayList<BlockEntry> blockList = new ArrayList<BlockEntry>();
        CompletableFuture[] futures = new CompletableFuture[blockCount];
        for (int i = 0; i < blockCount; i++) {
            String blockID = String.format("%08d", i);
            //String blockID = String.format("%05d", i);
            //String blockIdEncoded = Base64.getEncoder().encodeToString(blockID.getBytes());
            currentBlockSize = blockLength;
            if (i == blockCount - 1){
                currentBlockSize = (int) (source.length() - blockLength * i);
            }
            byte[] bufferBytes = new byte[currentBlockSize];
            inputStream.read(bufferBytes, 0, currentBlockSize);
            CompletableFuture completableFuture = CompletableFuture.runAsync(()->{
                long startTme = System.currentTimeMillis();
                logger.info("BlockIndex:{}，startTme:{}ms",blockID,startTme);
                try {
                    blockBlobRef.uploadBlock(blockID, new ByteArrayInputStream(bufferBytes), bufferBytes.length, null, null, null);
                } catch (StorageException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                blockList.add(new BlockEntry(blockID));
                logger.info("Submitted block " + ", BlockIndex:" + blockID + ",consume {}ms",(System.currentTimeMillis()-startTme));
            },executorService);
            futures[i] = completableFuture;
        }
        CompletableFuture.allOf(futures).whenComplete((s, e) -> {
            try {
                Collections.sort(blockList, new Comparator<BlockEntry>() {
                    @Override
                    public int compare(BlockEntry o1, BlockEntry o2) {
                       return o1.getId().compareTo(o2.getId());
                    }
                });
                System.out.println(JsonUtil.toJson(blockList));
                blockBlobRef.commitBlockList(blockList);
            } catch (StorageException e1) {
                e1.printStackTrace();
            }
            Long endTime = System.currentTimeMillis();
            System.out.println("===============Uploading Done===============总共耗时："+ (endTime - startTime) +" ms");
        }).join();
        //this.uploadTest();
    }


    private CloudBlobContainer getCloudBlockContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        String ccountName=PropertyConfigureUtil.mapProps.get("accountName").toString();
        String accountKey=PropertyConfigureUtil.mapProps.get("accountKey").toString();
        // String bucketName=PropertyConfigureUtil.mapProps.get("bucketName").toString();
        //存储连接字符串
        String storageConnectionString="DefaultEndpointsProtocol=https;" + "AccountName="+ccountName+";"
                + "AccountKey="+accountKey;
        //上传容器的名称
        String containerName= "ysy-test";
        // Parse the connection string and create a blob client to interact with Blob storage
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference(containerName);
        //创建容器
        if(container.createIfNotExists()){
            logger.info(CommonUtil.getSystemLog("New Container created :"+containerName));
        }
        return container;
    }
}
