package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.IsdnRetryExtend;
import dts.com.vn.entities.NeifInfo;
import dts.com.vn.repository.IsdnRetryExtendRepository;
import dts.com.vn.repository.NeifInfoRepository;
import dts.com.vn.service.AutoImportNeifService;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AutoImportNeifServiceImpl implements AutoImportNeifService {

    private static final Logger logger = LoggerFactory.getLogger(AutoImportServiceImpl.class);

    private final NeifInfoRepository neifInfoRepository;

    private static final String PATH = "/home/spr/import-neif";

    private final IsdnRetryExtendRepository isdnRetryExtendRepository;

    public AutoImportNeifServiceImpl(NeifInfoRepository neifInfoRepository,
                                     IsdnRetryExtendRepository isdnRetryExtendRepository) {
        this.neifInfoRepository = neifInfoRepository;
        this.isdnRetryExtendRepository = isdnRetryExtendRepository;
    }

    @Override
    public void autoImportNeif() {
        List<File> files = null;
        try {
            // Lấy ra tất cả các folder từ đường dẫn /home/spr/import-neif/
            try (Stream<Path> stream = Files.list(Paths.get(PATH))) {
                files = stream.map(Path::toFile).collect(Collectors.toList());
            }
            if (files.size() > 0) {
                for (File file : files) {
                    if (file.getName().endsWith(".txt")) {
                        readFileTxtAndInsert(file.getName());
                        Path source = Paths.get("/home/spr/import-neif/" + file.getName());
                        Path newDir = Paths.get("/home/spr/import-neif/used");
                        //Sau khi insert vào db thì chuyển file này vào thư mục used
                        Files.move(source, newDir.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        file.delete();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (files != null) {
                files.stream().close();
            }
        }
    }

    @Override
    public void transferFile() {
        moveFileFromRemoteServerHaNoi();
        moveFileFromRemoteServerHaNoiOCSGW();
        moveFileFromRemoteServerHCM();
    }

    // Bước 1: Đọc file trong 1 thư mục chỉ định
    private void readFileTxtAndInsert(String fileName) {
        try {
            String line;
            BufferedReader bufferreader = new BufferedReader(new FileReader(PATH + "/" + fileName));
            while ((line = bufferreader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    NeifInfo neifInfo = new NeifInfo();
                    String[] dataFromLine = line.replaceAll("#", "").split(",");
                    neifInfo.setBonusAmount(dataFromLine[8]);
                    neifInfo.setCardNumber(dataFromLine[9]);
                    neifInfo.setInsertDate(convertDateToTimestamp(dataFromLine[5]));
                    neifInfo.setIsdn(dataFromLine[2]);
                    neifInfo.setIpRemote(null);
                    neifInfo.setMainAmount(dataFromLine[7]);
                    neifInfo.setNeifMessage(line.replaceAll("#", ""));
                    neifInfo.setProfile(dataFromLine[4]);
                    neifInfo.setRegDate(convertDateToTimestamp(dataFromLine[11] + " 00:00:00"));
                    neifInfo.setStatus("1");
                    if(fileName.startsWith("FO_dump_")){
                        neifInfo.setIpRemote("10.151.244.225");
                    }
                    else if(fileName.startsWith("FLOW_ONE_dump")){
                        neifInfo.setIpRemote("10.3.20.40");
                    }
                    else{
                        neifInfo.setIpRemote("10.3.15.123");
                    }
                    insertToDbNeifInfo(neifInfo);
                    long check = Long.parseLong(dataFromLine[3]);
                    if(check == 10){
                        insertToDbIsdnRetryExtend(dataFromLine[2], dataFromLine[5]);
                    }
                }
            }
            bufferreader.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    // Chuyển format thời gian
    private Timestamp convertDateToTimestamp(String date) {
        DateTimeFormatter inSDF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter outSDF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(outSDF.format(inSDF.parse(date)));
    }

    // Bước 2: Ghi thông tin trong file vào bảng neif_info
    private void insertToDbNeifInfo(NeifInfo neifInfo) {
        neifInfoRepository.save(neifInfo);
    }

    // Bước 3: Tìm trong bảng isdn_retry_extend các bản ghi trong vòng 30 ngày gần nhất, nếu có thì
    // update status của
    // bản ghi đó từ 1 thành 2, thêm vào trường last_recharge_date
    private void insertToDbIsdnRetryExtend(String isdn, String lastRechargeDate) {
                isdnRetryExtendRepository.updateNeifInfo(isdn, convertDateToTimestamp(lastRechargeDate));
    }

    //Chuyển file từ server HCM họ sang thư mục home/spr/import-neif của mình
    private void moveFileFromRemoteServerHCM() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("10.151.244.225", 21);
            ftpClient.login("relay_FO", "FO@ftp");
            String wD = ftpClient.printWorkingDirectory();
            FTPFile[] files = ftpClient.listFiles(wD);
            for (FTPFile file : files) {
                if (file.getName().startsWith("FO_dump_") && !file.getName().endsWith("_used.txt")) {
                    if (checkExist(file.getName())) {
                        String remoteFile1 = "/" + wD + "/" + file.getName();
                        File downloadFile1 = new File("/home/spr/import-neif/" + file.getName());
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

    //Chuyển file từ server OCSGW HaNoi họ sang thư mục home/spr/import-neif của mình
    private void moveFileFromRemoteServerHaNoiOCSGW() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("10.3.20.40", 21);
            ftpClient.login("Flowftp", "Fo123456");
            String wD = ftpClient.printWorkingDirectory();
            FTPFile[] files = ftpClient.listFiles(wD);
            for (FTPFile file : files) {
                if (file.getName().startsWith("FLOW_ONE_dump") && !file.getName().endsWith("_used.txt")) {
                    if (checkExist(file.getName())) {
                        String remoteFile1 = "/" + wD + "/" + file.getName();
                        File downloadFile1 = new File("/home/spr/import-neif/" + file.getName());
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

    //Chuyển file từ server HaNoi1 họ sang thư mục home/spr/import-neif của mình
    private void moveFileFromRemoteServerHaNoi() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("10.3.15.123", 21);
            ftpClient.login("flowftp", "f10wFtp");
            String wD = ftpClient.printWorkingDirectory();
            FTPFile[] files = ftpClient.listFiles(wD);
            for (FTPFile file : files) {
                if (file.getName().startsWith("FO_dump_INHN") && !file.getName().endsWith("_used.txt")) {
                    if (checkExist(file.getName())) {
                        String remoteFile1 = "/" + wD + "/" + file.getName();
                        File downloadFile1 = new File("/home/spr/import-neif/" + file.getName());
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
            try (Stream<Path> stream = Files.list(Paths.get("/home/spr/import-neif/used"))) {
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
