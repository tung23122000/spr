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
import java.util.stream.Stream;

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
        List<File> files = null;
        try {
            // Lấy ra tất cả các folder từ đường dẫn /home/spr/import
            try (Stream<Path> stream = Files.list(Paths.get("/home/spr/import"))) {
                files = stream.map(Path::toFile).collect(Collectors.toList());
            }
            if (files.size() > 0) {
                for (File file : files) {
                    Optional<IsdnList> isdnList = isdnListRepository.findById(Long.valueOf(file.getName()));
                    // Kiểm tra xem tên file có phải isdn_list_id không
                    if (isdnList.isPresent()) {
                        handlingFileTxt(file.getName(), isdnList.get().getIsDisplay());
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

    // Lấy các file ở trong folder có tên trùng với isdn_list_id
    private void handlingFileTxt(String fileName, String isDisplay) {
        List<File> files = null;
        try {
            try (Stream<Path> stream = Files.list(Paths.get("/home/spr/import/" + fileName))) {
                files = stream.map(Path::toFile).collect(Collectors.toList());
            }
            if (files.size() > 0) {
                for (File file : files) {
                    // Chỉ lấy những file có đuôi .txt
                    if (!file.getName().endsWith("-used.txt") && file.getName().endsWith(".txt")) {
                        logger.info(file.getName());
                        readFileTxtAndInsertToDb(file.getName(), fileName, isDisplay);
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

    // Đọc các file txt đã được lọc và insert vào database
    private void readFileTxtAndInsertToDb(String fileName, String folderName, String isDisplay) {
        try {
            String line;
            List<String> listFromFile = new ArrayList<>();
            BufferedReader bufferreader =
                    new BufferedReader(new FileReader("/home/spr/import/" + folderName + "/" + fileName));
            while ((line = bufferreader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    listFromFile.add(line);
                }
            }
            try {
                // Kiểm tra dữ liệu của file trước khi insert
                if (listFromFile.size() > 0) {
                    insertToDb(listFromFile, folderName, isDisplay, fileName);
                }
            } catch (InterruptedException error) {
                logger.error(error.getMessage());
            }
            bufferreader.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    // Ghi dữ liệu từ file txt vào database
    private void insertToDb(List<String> listIsdnFromFile, String folderName, String isDisplay,
                            String fileName) throws InterruptedException, IOException {
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
                    }
                    return listDetailNew;
                });
            }
            executorService.invokeAll(callables);
        } else {
            ListDetailNew listDetailNew;
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
            for (String s : listIsdnFromFile) {
                if (s.replaceAll("\r", "").length() == 9||s.replaceAll("\r", "").length() == 10) {
                    map.put(s.replaceAll("\r", ""), 1);
                }
            }
            // Tạo danh sách chi tiết
            if (map.size() > 0) {
                listDetailNew = new ListDetailNew(null, Long.valueOf(folderName), map, isDisplay);
                listDetailNewRepository.save(listDetailNew);
            }
        }
    }

}
