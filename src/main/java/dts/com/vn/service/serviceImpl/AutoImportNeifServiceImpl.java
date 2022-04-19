package dts.com.vn.service.serviceImpl;

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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
                    // Kiểm tra xem tên file có phải isdn_list_id không
                    if (file.getName().endsWith(".txt")) {
                        logger.info(file.getName());
                        readFileTxtAndInsert(file.getName());
                        Path source = Paths.get("/home/spr/import-neif/" + file.getName());
                        Path newDir = Paths.get("/home/spr/import-neif/used");
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
        moveFileFromRemoteServer();
    }

    // Bước 1: Đọc file trong 1 thư mục chỉ định
    private void readFileTxtAndInsert(String fileName) {
        try {
            String line;
            List<String> listIsdnFromFile = new ArrayList<>();
            BufferedReader bufferreader = new BufferedReader(new FileReader(PATH + "/" + fileName));
            while ((line = bufferreader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    NeifInfo neifInfo = new NeifInfo();
                    String[] dataFromLine = line.replaceAll("#", "").split(",");
                    listIsdnFromFile.add(dataFromLine[2]);
                    neifInfo.setBonusAmount(dataFromLine[8]);
                    neifInfo.setCardNumber(dataFromLine[9]);
                    neifInfo.setInsertDate(convertDateToTimestamp(dataFromLine[5]));
                    neifInfo.setIsdn(dataFromLine[2]);
                    neifInfo.setMainAmount(dataFromLine[7]);
                    neifInfo.setNeifMessage(line.replaceAll("#", ""));
                    neifInfo.setProfile(dataFromLine[4]);
                    neifInfo.setRegDate(convertDateToTimestamp(dataFromLine[11] + " 00:00:00"));
                    neifInfo.setStatus("1");
                    insertToDbNeifInfo(neifInfo);
                }
            }
            insertToDbIsdnRetryExtend(listIsdnFromFile);
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
        // Nếu tồn tại bản ghi chứa thuê bao này thì update lại bản ghi
        if (neifInfoRepository.findDataByIsdn(neifInfo.getIsdn()) != null) {
            neifInfoRepository.updateNeifInfo(neifInfo.getBonusAmount(), neifInfo.getInsertDate(),
                                              neifInfo.getMainAmount(), neifInfo.getNeifMessage(), neifInfo.getProfile(),
                                              neifInfo.getRegDate(), neifInfo.getStatus(), neifInfo.getIsdn());
        }
        // Nếu không tồn tại bản ghi nào chứa thuê bao này thì tạo mới bản ghi
        else {
            neifInfoRepository.save(neifInfo);
        }
    }

    // Bước 3: Tìm trong bảng isdn_retry_extend các bản ghi trong vòng 30 ngày gần nhất, nếu có thì
    // update status của
    // bản ghi đó từ 1 thành 2
    private void insertToDbIsdnRetryExtend(List<String> listIsdn) {
        for (String isdn : listIsdn) {
            if (isdnRetryExtendRepository.findDataFromIsdnRetryExtendByIsdn(isdn) != null) {
                isdnRetryExtendRepository.updateNeifInfo(isdn);
            }
        }
    }

    //Chuyển file từ server họ sang thư mục home/spr/import-neif của mình
    private void moveFileFromRemoteServer() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("10.151.244.225", 21);
            ftpClient.login("relay_FO", "FO@ftp");
            String wD = ftpClient.printWorkingDirectory();
            FTPFile[] files = ftpClient.listFiles(wD);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
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
