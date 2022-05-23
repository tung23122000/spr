package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.InfoIsdnList;
import dts.com.vn.entities.InfoTargetSystem;
import dts.com.vn.repository.InfoIsdnListRepository;
import dts.com.vn.repository.InfoTargetSystemRepository;
import dts.com.vn.service.CronjobTransferFileIsdnListService;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConjobTransferFileIsdnListServiceImpl implements CronjobTransferFileIsdnListService {

    private static final Logger logger = LoggerFactory.getLogger(AutoImportServiceImpl.class);

    private final InfoIsdnListRepository infoIsdnListRepository;

    private final InfoTargetSystemRepository infoTargetSystemRepository;

    public ConjobTransferFileIsdnListServiceImpl(
            InfoIsdnListRepository infoIsdnListRepository,
            InfoTargetSystemRepository infoTargetSystemRepository) {
        this.infoIsdnListRepository = infoIsdnListRepository;
        this.infoTargetSystemRepository = infoTargetSystemRepository;
    }

    @Override
    public void autoTransferFile() {
        List<InfoIsdnList> infoIsdnList = infoIsdnListRepository.findAll();
        for (InfoIsdnList isdnList : infoIsdnList) {
            String name = "%" + isdnList.getNameTargetSystem() + "%";
            InfoTargetSystem infoTargetSystem = infoTargetSystemRepository.findByNameTargetSystem(name);
            moveFileFromRemoteServer(infoTargetSystem.getIpTargetSystem(), infoTargetSystem.getPortTargetSystem(),
                                     isdnList.getUser(), isdnList.getPassword(), isdnList.getIsdnListId().toString(),
                                     isdnList.getNameTargetFolder());
        }
    }

    private void moveFileFromRemoteServer(String hostname, Integer port, String user, String password,
                                          String isdnListId, String targetFolder) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(hostname, port);
            ftpClient.login(user, password);
            String wD = ftpClient.printWorkingDirectory() + targetFolder;
            //logger.info(wD);
            FTPFile[] files = ftpClient.listFiles(wD);
            for (FTPFile file : files) {
                logger.info(file.getName());
                if (file.getName().endsWith(".txt")) {
                    if (checkExist(file.getName())) {
                        String remoteFile1 = wD+ "/" + file.getName();
                        File downloadFile1 = new File("/home/spr/import/" + isdnListId + "/" + file.getName());
                        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                        boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
                        outputStream1.close();
                    }
                }
            }
            ftpClient.disconnect();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //Check xem file này đã tồn tại trong thư mục used chưa
    private Boolean checkExist(String fileName) {
        Boolean isExist = null;
        List<File> files;
        try {
            try (Stream<Path> stream = Files.list(Paths.get("/home/spr/import/used"))) {
                files = stream.map(Path::toFile).collect(Collectors.toList());
            }
            if (files.size() > 0) {
                for (File file : files) {
                    if (file.getName().equalsIgnoreCase(fileName)) {
                        isExist = false;
                        break;
                    } else {
                        isExist = true;
                    }
                }
            } else {
                isExist = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;
    }
}
