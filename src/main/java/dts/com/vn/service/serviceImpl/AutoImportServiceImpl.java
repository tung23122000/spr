package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetailNew;
import dts.com.vn.repository.IsdnListRepository;
import dts.com.vn.repository.ListDetailNewRepository;
import dts.com.vn.service.AutoImportService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AutoImportServiceImpl implements AutoImportService {

    private static final Logger logger = LoggerFactory.getLogger(AutoImportServiceImpl.class);

    private final IsdnListRepository isdnListRepository;

    private final ListDetailNewRepository listDetailNewRepository;

    public AutoImportServiceImpl(IsdnListRepository isdnListRepository,
                                 ListDetailNewRepository listDetailNewRepository) {
        this.isdnListRepository = isdnListRepository;
        this.listDetailNewRepository = listDetailNewRepository;
    }

    @Override
    public void autoImport() {
        try {
            //Lấy ra tất cả các folder từ đường dẫn /home/spr/import
            List<File> files = Files.list(Paths.get("/home/spr/import"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            files.stream().close();
            if (files.size() > 0) {
                for (File file : files) {
                    Optional<IsdnList> isdnList = isdnListRepository.findById(Long.valueOf(file.getName()));
                    //Kiểm tra xem tên file có phải isdn_list_id không
                    if (isdnList.isPresent()) {
                        handlingFileTxt(file.getName(), isdnList.get().getIsDisplay());
                    } else {
                        logger.warn("Folder này không phải folder chứa danh sách thuê bao!");
                    }
                }
            } else {
                logger.warn("Không tồn tại folder nào trong thư mục này!");
            }
        } catch (IOException e) {
            logger.error("Không tìm thấy folder " + e.getMessage());
        }
    }

    //Lấy các file ở trong folder có tên trùng với isdn_list_id
    private void handlingFileTxt(String fileName, String isDisplay) {
        try {
            List<File> files = Files.list(Paths.get("/home/spr/import/" + fileName))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            files.stream().close();
            if (files.size() > 0) {
                for (File file : files) {
                    //Chỉ lấy những file có đuôi .txt, bỏ những file có đuôi "-used.txt" đã được insert
                    if (!file.getName().endsWith("-used.txt")&&file.getName().endsWith(".txt")) {
                        logger.info(file.getName());
                        readFileTxtAndInsertToDb(file.getName(), fileName, isDisplay);
                        String[] nowName = file.getName().split("\\.");
                        //Tạo tên mới cho file đã được insert
                        String nameAfterInsert = nowName[0] + "-used." + nowName[1];
                        String newFilePath = file.getAbsolutePath().replace(file.getName(), "") + nameAfterInsert;
                        File newFile = new File(newFilePath);
                        try {
                            //Thế chỗ file cũ bằng file mới
                            FileUtils.moveFile(file, newFile);
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    } else {
                        logger.warn("Không có file import nào trong folder " + fileName);
                    }
                }
            } else {
                logger.warn("Không có file nào trong folder " + fileName);
            }
        } catch (IOException e) {
            logger.error("Không tìm thấy folder " + e.getMessage());
        }
    }

    //Đọc các file txt đã được lọc và insert vào database
    private void readFileTxtAndInsertToDb(String fileName, String folderName, String isDisplay) {
        try {
            String line;
            List<String> listFromFile = new ArrayList<>();
            BufferedReader bufferreader = new BufferedReader(
                    new FileReader("/home/spr/import/" + folderName + "/" + fileName));
            while ((line = bufferreader.readLine()) != null) {
                if(line.trim().length() > 0) {
                    listFromFile.add(line);
                }
            }
            try {
                //Kiểm tra dữ liệu của file trước khi insert
                if(listFromFile.size()>0){
                    insertToDb(listFromFile, folderName, isDisplay, fileName);
                }else {
                    logger.warn("File import không có dữ liệu!");
                }
                //Đóng luồng sau khi insert thành công
                bufferreader.close();
            } catch (InterruptedException error) {
                logger.error(error.getMessage());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //Ghi dữ liệu từ file txt vào database
    private void insertToDb(List<String> listIsdnFromFile, String folderName,
                            String isDisplay, String fileName) throws InterruptedException, IOException {
        if (listIsdnFromFile.size() > 1000000) {
            int n = listIsdnFromFile.size() / 1000000;
            AtomicInteger counter = new AtomicInteger();
            Collection<List<String>> result = listIsdnFromFile.stream()
                    .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 1000000))
                    .values();
            ExecutorService executorService = Executors.newFixedThreadPool(n + 1);
            List<Callable<ListDetailNew>> callables = new ArrayList<>();
            for (List<String> item : result) {
                callables.add(() -> {
                    ListDetailNew listDetailNew = null;
                    LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
                    for (String s : item) {
                        if (s.replaceAll("\r", "").length() == 9) {
                            map.put(s.replaceAll("\r", ""), 1);
                        }
                    }
                    if (map.size() > 0) {
                        listDetailNew = new ListDetailNew(null, Long.valueOf(folderName), map, isDisplay);
                        listDetailNewRepository.save(listDetailNew);
                    }else{
                        logger.warn("Dữ liệu trong file không đúng định dạng!");
                    }
                    return listDetailNew;
                });
            }
            executorService.invokeAll(callables);
        } else {
            ListDetailNew listDetailNew;
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
            for (String s : listIsdnFromFile) {
                if (s.replaceAll("\r", "").length() == 9) {
                    map.put(s.replaceAll("\r", ""), 1);
                }
            }
            //	Tạo danh sách chi tiết
            if(map.size() > 0) {
                listDetailNew = new ListDetailNew(null, Long.valueOf(folderName), map, isDisplay);
                listDetailNewRepository.save(listDetailNew);
            }else {
                logger.warn("Dữ liệu trong file không đúng định dạng!");
            }
        }
    }

}
